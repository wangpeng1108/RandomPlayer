package com.wangpeng.myplayer.ui.PlayerPage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.wangpeng.myplayer.R;
import com.wangpeng.myplayer.base.MVP.ui.BaseActivity;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.service.PlayerService;
import com.wangpeng.myplayer.utils.TimeUtil;
import com.wangpeng.myplayer.view.LrcView;
import com.wangpeng.myplayer.view.MyCircleImage;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

/**
 * Created by WP on 16/11/4.
 */

public class PlayerActivity extends BaseActivity implements IPlayerActivity,LrcView.OnLrcClick{
    @BindView(R.id.playpage_play)
    CheckBox pause; //暂停/播放
    @BindView(R.id.playpage_favor)
    CheckBox favor; //喜欢
    @BindView(R.id.playpage_next)
    Button next;    //下首
    @BindView(R.id.playpage_lrcview)
    LrcView lrcView;    //歌词
    @BindView(R.id.playpage_playtime_tv)
    TextView currPlayedTime;    //当前播放时间
    @BindView(R.id.playpage_duration_tv)
    TextView songDuration;  //总时长
    @BindView(R.id.playpage_progressbar)
    AppCompatSeekBar seekBar;   //进度条
    @BindView(R.id.playpage_backimg)
    MyCircleImage backImg;  //专辑图片

    private IPlayerPresenter presenter; //Presenter实例
    private ServiceConnection mServiceConnection = null;    //播放服务连接

    @Override
    public int getContentView() {
        return R.layout.activity_playing;
    }

    @Override
    public void initView(Bundle savedInstanceState)
    {
        presenter = new PlayerPresenter(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);
        favor.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        backImg.setOnClickListener(this);
        lrcView.setVisibility(View.GONE);
        lrcView.setOnClickListener(this);
        lrcView.setClick(this);
        mServiceConnection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                presenter.setBinder(((PlayerService.MusicPlayerBinder)iBinder).getBinder());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent service = new Intent(this,
                PlayerService.class);
        bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onViewClick(View view){
        if(view==pause){
            if(presenter.playControl("isPlaying")){
                pausePlay();
            }else{
                presenter.playControl("restart");
                pause.setChecked(true);
                backImg.startRotate();
            }
        }else if(view==next){
            presenter.playControl("stop");
            presenter.changeMusic();
            pause.setChecked(true);
        }else if(view==backImg){
            backImg.setVisibility(View.GONE);
            backImg.stopRotate();
            lrcView.setVisibility(View.VISIBLE);
        }else{
            System.out.println("other");
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        presenter.seekTo(seekBar.getProgress());
    }

    @Override
    public void onPlayComplate() {
        presenter.changeMusic();
    }

    @Override
    public void startProgressBar(int duration) {
        seekBar.setMax(duration);
        songDuration.setText(TimeUtil.timeToString(duration));
        presenter.undateProgress(duration);
    }

    @Override
    public void setCurrProgress(int curr) {
        currPlayedTime.setText(TimeUtil.timeToString(curr));
        seekBar.setProgress(curr);
        if(lrcView.getVisibility()==View.VISIBLE)
            lrcView.setIndex(lrcView.getIndexByLrcTime(curr));
    }

    @Override
    public void showSnack(CharSequence msg) {
        Snackbar.make(lrcView,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setLrc(List<LrcContent> lrc) {
        lrcView.setLrcLists(lrc);
        if(backImg.getVisibility()==View.GONE)
            lrcView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSongImg(String imgUrl) {
        if(imgUrl!=null&&imgUrl.length()>0)
            if(backImg.getVisibility()==View.VISIBLE){
                Glide.with(this).load(imgUrl).dontAnimate().centerCrop().into(backImg);
            }else{
                Glide.with(this).load(imgUrl).dontAnimate().centerCrop().into(backImg);
            }
    }

    @Override
    public void prePlay() {
        backImg.setCurrentAngle(0);
        backImg.stopRotate();
        Glide.with(this).load(R.drawable.playback).centerCrop().into(backImg);
        lrcView.setVisibility(View.GONE);
        lrcView.setIndex(0);
        lrcView.setLrcLists(null);
    }

    @Override
    public void startPlay() {
        if(backImg.getVisibility()==View.VISIBLE)
            backImg.startRotate();
        pause.setChecked(true);
    }

    @Override
    public void pausePlay() {
        pause.setChecked(false);
        backImg.stopRotate();
        presenter.playControl("pause");
    }



    @Override
    public void getData() {
        presenter.getData();
    }


    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLrcClick(LrcView view) {
        if(presenter.playControl("isPlaying"))
            backImg.startRotate();
        backImg.setVisibility(View.VISIBLE);
        lrcView.setVisibility(View.GONE);
    }
}
