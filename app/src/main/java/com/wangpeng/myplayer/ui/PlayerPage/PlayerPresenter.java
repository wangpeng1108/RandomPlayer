package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.presenter.BasePresenter;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.beans.PlayBean;
import com.wangpeng.myplayer.beans.SongRankingBean;
import com.wangpeng.myplayer.config.Configs;
import com.wangpeng.myplayer.service.Player;
import com.wangpeng.myplayer.service.PlayerService;
import com.wangpeng.myplayer.utils.LrcUtil;
import java.util.List;
import java.util.Random;

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
    private SongRankingBean songs = null;
    private Player binder = null;

    public PlayerPresenter(IPlayerActivity baseView) {
        super(baseView);
        this.view = baseView;
    }

    @Override
    public void init() {
        model = new PlayerModel();

    }

    @Override
    public void getData() {
        model.getData(new Model.OnResponse() {
            @Override
            public void onSuccess(Object o) {
                songs = ((SongRankingBean)o);
                changeMusic();
            }

            @Override
            public void onFailed(String msg) {
                view.showSnack("获取数据失败！");
            }
        });
    }

    @Override
    public void changeMusic() {
        if(songs==null||songs.getSong_list().size()==0){getData();return;}
        int position = new Random(System.currentTimeMillis()).nextInt(Configs.MUSICCOUNT);
        String songId = songs.getSong_list().get(position).getSong_id();
        if(songId==null||songId.length()==0){changeMusic();return;}
        view.prePlay();
        model.getMusicInfo(songId, new Model.OnResponse() {
            @Override
            public void onSuccess(Object o) {
                curr=0;
                PlayBean song = (PlayBean)o;
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
    public void undateProgress(final int duration) {
        rx.Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                while(curr<=binder.getCurrentDuration()&&binder.getPlayer()!=null&&(curr=binder.getCurrentDuration())<duration){
                    subscriber.onNext(curr);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(-1);
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
                e.printStackTrace();
                //view.showSnack("播放条滚动出错"+e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                if(integer==-1){view.onPlayComplate();return;}
                view.setCurrProgress(integer);
            }
        });
    }

    @Override
    public void getLrc(String url) {
        model.getLrcContent(url, new Model.OnResponse() {
            @Override
            public void onSuccess(Object o) {
                List<LrcContent> lrcContents = LrcUtil.parseLrcStr((String)o);
                view.setLrc(lrcContents);
                return;
            }

            @Override
            public void onFailed(String msg) {
                view.showSnack(msg);
            }
        });
        view.setLrc(null);
    }

    @Override
    public boolean playControl(String command) {
        switch (command){
            case "isPlaying":
                return binder.isPlaying();
            case "pause":
                binder.pause();break;
            case "stop":
                binder.stop();break;
            case "restart":
                if(binder.getPlayer()==null)
                    changeMusic();
                else
                    binder.restart();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void setBinder(Player binder) {
        this.binder=binder;
    }

    @Override
    public void seekTo(int curr) {
        binder.seekTo(curr);
    }

}
