package com.wangpeng.myplayer.service;

import android.media.MediaPlayer;

import com.wangpeng.myplayer.base.MVP.ui.BaseView;

/**
 * Created by Howard on 2016/3/31.
 */
public interface Player {

    public Player getBinder();

    public void play(String path);

    public  void stop();

    public boolean isPlaying();

    public int getDuration();

    public int getCurrentDuration();

    public void seekTo(int position);

    public void pause();

    public MediaPlayer getPlayer();
}
