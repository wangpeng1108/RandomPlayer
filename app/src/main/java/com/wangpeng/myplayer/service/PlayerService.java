package com.wangpeng.myplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.wangpeng.myplayer.MyApplication;

import java.io.IOException;

/**
 * Created by WP on 2016/11/5.
 */
public class PlayerService extends Service {
    public MediaPlayer mediaPlayer;
    private MusicPlayerBinder musicPlayerBinder = new MusicPlayerBinder();
    private MyApplication myApplication;

    @Override
    public void onCreate() {
        myApplication = (MyApplication) getApplication();
    }

    public class MusicPlayerBinder extends Binder implements Player {

        @Override
        public MusicPlayerBinder getBinder() {
            return musicPlayerBinder;
        }
        public void play(String path) {
            mPlay(path);
        }
        public void restart(){
            mRestart();
        }
        @Override
        public void stop() {
            mStop();
        }
        @Override
        public boolean isPlaying() {
            return mIsPlaying();
        }
        @Override
        public int getDuration() {
            return mGetDuration();
        }
        @Override
        public int getCurrentDuration() {
            return mGetCurrentDuration();
        }
        @Override
        public void seekTo(int position) {
            mChanageSeek(position);
        }
        @Override
        public void pause() {
            mPause();
        }

        @Override
        public MediaPlayer getPlayer() {
            return getPlayerNow();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicPlayerBinder;
    }

    public int onStartCommand(Intent intent,int flags, int startId) {
        super.onStartCommand(intent,flags, startId);
        myApplication = (MyApplication) getApplication();
        return Service.START_NOT_STICKY;
    }

    /**
     * 是不是在播放
     * @return
     */
    public boolean mIsPlaying(){
        if(mediaPlayer!=null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 获取总的进度
     * @return
     */
    public int mGetDuration(){
        return  mediaPlayer.getDuration();
    }

    /**
     * 获取当前进度
     * @return
     */
    public int mGetCurrentDuration(){
        return mediaPlayer.getCurrentPosition();
    }

    public MediaPlayer getPlayerNow(){
        return mediaPlayer;
    }

    /**
     * 停止
     */
    public void mStop(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;

        }
    }

    public void mChanageSeek(int position){
        mediaPlayer.seekTo(position);
        if (mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }else mediaPlayer.pause();
    }

    public void mPlay(String url) {
        //System.out.println("开始播放："+url);
        if (mediaPlayer==null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer arg0) {
                    mediaPlayer.start();
                }
            });
        }
    }
    public void mRestart(){
        if(mediaPlayer!=null)mediaPlayer.start();
    }
    public void mPause() {
        if(mediaPlayer!=null)mediaPlayer.pause();
    }
}
