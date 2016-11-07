package com.wangpeng.myplayer.ui.PlayerPage;

import com.squareup.picasso.Picasso;
import com.wangpeng.myplayer.MyApplication;
import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.presenter.BasePresenter;
import com.wangpeng.myplayer.base.MVP.ui.BaseView;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.beans.PlayBean;
import com.wangpeng.myplayer.service.PlayerService;
import com.wangpeng.myplayer.utils.LrcUtil;

import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by WP on 16/11/6.
 */

public class PlayerPresenter extends BasePresenter implements IPlayerPresenter{
    int curr = 0;
    private IPlayerActivity view;
    private IPlayerModel model;
    public PlayerPresenter(IPlayerActivity baseView) {
        super(baseView);
        this.view = baseView;
    }

    @Override
    public void init() {
        model = new PlayerModel();
    }

    @Override
    public void getData(Model.OnResponse response) {
        model.getData(response);
    }

    @Override
    public void changeMusic(final PlayerService.MusicPlayerBinder binder,String songId) {
        model.getMusicInfo(songId, new Model.OnResponse() {
            @Override
            public void onSuccess(Object o) {
                curr=0;
                PlayBean song = (PlayBean)o;
                System.out.println(song.getSonginfo().getPic_big());
                view.setSongImg(song.getSonginfo().getPic_huge());
                view.setMyTitle(song.getSonginfo().getTitle());
                String musicUrl = song.getBitrate().getShow_link();
                if(binder!=null&&musicUrl!=null)
                {
                    binder.stop();
                    binder.play(musicUrl);
                    view.startPlay();
                    view.startProgressBar(binder.getDuration());
                    getLrc(song.getSonginfo().getLrclink());
                }
            }

            @Override
            public void onFailed(String msg) {
                view.showSnack(msg);
            }
        });
    }

    @Override
    public void undateProgress(final int duration, final PlayerService.MusicPlayerBinder binder) {
        System.out.println("presenter开始更新progress");
        rx.Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                while(curr<=binder.getCurrentDuration()&&(curr=binder.getCurrentDuration())<duration){
                    subscriber.onNext(curr);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                view.onPlayComplate();
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("滚动条播放完毕");
                view.onPlayComplate();
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("播放条滚动出错");
            }

            @Override
            public void onNext(Integer integer) {
                view.setCurrProgress(integer);
            }
        });
    }

    @Override
    public void getLrc(String url) {
        model.getLrcContent(url, new Model.OnResponse() {
            @Override
            public void onSuccess(Object o) {
                //System.out.println(o.toString());
                List<LrcContent> lrcContents = LrcUtil.parseLrcStr((String)o);
                view.setLrc(lrcContents);
            }

            @Override
            public void onFailed(String msg) {
                view.showSnack(msg);
            }
        });
    }
}
