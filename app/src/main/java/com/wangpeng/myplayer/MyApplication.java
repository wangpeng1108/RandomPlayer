package com.wangpeng.myplayer;

import android.app.Application;

import com.wangpeng.myplayer.service.PlayerService;

/**
 * Created by WP on 16/11/4.
 */

public class MyApplication extends Application {
    public PlayerService musicPlayerService=null;
    public void setMusicPlayerService (PlayerService service){
        musicPlayerService=service;
    }

}
