package com.wangpeng.myplayer.ui.PlayerPage.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by WP on 16/11/6.
 */

public class PlayerRetrofit {
    public Retrofit getGsonRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(DataService.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public Retrofit getStringRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://musicdata.baidu.com/data2/lrc/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}
