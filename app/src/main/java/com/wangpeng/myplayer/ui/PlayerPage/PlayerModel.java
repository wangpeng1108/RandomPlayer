package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.BaseModel;
import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.beans.PlayBean;
import com.wangpeng.myplayer.beans.SongRankingBean;
import com.wangpeng.myplayer.config.Configs;
import com.wangpeng.myplayer.ui.PlayerPage.Retrofit.DataService;
import com.wangpeng.myplayer.ui.PlayerPage.Retrofit.PlayerRetrofit;

import java.sql.SQLOutput;

import javax.xml.transform.Source;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WP on 16/11/6.
 */

public class PlayerModel extends BaseModel implements IPlayerModel {
    @Override
    public void getData(final OnResponse response) {
        Retrofit retrofit = new PlayerRetrofit().getGsonRetrofit();
        DataService service = retrofit.create(DataService.class);
        service.getSongRanking("json","","webapp_music","baidu.ting.billboard.billList", 2, Configs.MUSICCOUNT, 0)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongRankingBean>() {
                    @Override
                    public void onNext(SongRankingBean value) {
                        System.out.println("获取到长度："+value.getSong_list().size());
                        response.onSuccess(value);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("完成！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("获取时出错："+e.getMessage());
                        response.onFailed(e.getMessage());
                    }

                });
    }

    @Override
    public void getMusicInfo(String songId,final OnResponse response) {
        Retrofit retrofit = new PlayerRetrofit().getGsonRetrofit();
        DataService service = retrofit.create(DataService.class);
        service.getPlay("json", "", "webapp_music", "baidu.ting.song.playAAC",songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlayBean>() {
                    @Override
                    public void onNext(PlayBean value) {
                        response.onSuccess(value);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("完成！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.println("获取时出错："+e.getMessage());
                        response.onFailed(e.getMessage());
                    }

                });
    }

    @Override
    public void getLrcContent(String lrcUrl, final OnResponse response) {
        System.out.println("开始获取歌词："+lrcUrl);
        DataService service = new PlayerRetrofit().getStringRetrofit().create(DataService.class);
        Call<String> call = service.getLrc(lrcUrl.substring("http://musicdata.baidu.com/data2/lrc/".length()));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> res) {
                if(res.body()!=null)
                    response.onSuccess(res.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("获取出错");
            }
        });
        /*Retrofit retrofit = new PlayerRetrofit().getStringRetrofit();
        DataService service = retrofit.create(DataService.class);
        service.getLrc(lrcUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("歌词获取完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        response.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                        response.onSuccess(s);
                    }
                });*/
    }
}
