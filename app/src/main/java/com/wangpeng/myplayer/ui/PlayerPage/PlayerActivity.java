package com.wangpeng.myplayer.ui.PlayerPage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangpeng.myplayer.MyApplication;
import com.wangpeng.myplayer.R;
import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.presenter.Presenter;
import com.wangpeng.myplayer.base.MVP.ui.BaseActivity;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.beans.PlayBean;
import com.wangpeng.myplayer.beans.SongRankingBean;
import com.wangpeng.myplayer.config.Configs;
import com.wangpeng.myplayer.service.Player;
import com.wangpeng.myplayer.service.PlayerService;
import com.wangpeng.myplayer.utils.TimeUtil;
import com.wangpeng.myplayer.view.LrcView;
import com.wangpeng.myplayer.view.MyCircleImage;
import com.wangpeng.myplayer.view.VisualizerView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WP on 16/11/4.
 */

public class PlayerActivity extends BaseActivity implements IPlayerActivity,LrcView.OnLrcClick{
    @BindView(R.id.playpage_play)
    CheckBox pause;
    @BindView(R.id.playpage_favor)
    CheckBox favor;
    @BindView(R.id.playpage_next)
    Button next;
    @BindView(R.id.playpage_lrcview)
    LrcView lrcView;


    @BindView(R.id.playpage_playtime_tv)
    TextView currPlayedTime;
    @BindView(R.id.playpage_duration_tv)
    TextView songDuration;
    @BindView(R.id.playpage_progressbar)
    AppCompatSeekBar seekBar;
    @BindView(R.id.playpage_backimg)
    MyCircleImage backImg;
    @BindView(R.id.playpage_content)
    RelativeLayout mLayout;

    private IPlayerPresenter presenter;
    private SongRankingBean songs = null;
    private ServiceConnection mServiceConnection = null;
    private PlayerService.MusicPlayerBinder binder = null;
    VisualizerView visualizerView;
    Visualizer mVisualizer;
    Equalizer mEqualizer;
    private static final float VISUALIZER_HEIGHT_DIP = 300f;//频谱View高度
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        if(visualizerView!=null)
            visualizerView.setVisibility(View.GONE);
        lrcView.setOnClickListener(this);
        lrcView.setClick(this);

        mServiceConnection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = ((PlayerService.MusicPlayerBinder)iBinder).getBinder();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent service = new Intent(PlayerActivity.this,
                PlayerService.class);
        this.bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    public void onViewClick(View view){
        if(view==pause){
            if(binder.isPlaying()){
                binder.pause();
                pause.setChecked(false);
                backImg.stopRotate();
            }else{
                binder.restart();
                pause.setChecked(true);
            }
        }else if(view==next){
            binder.stop();
            changeMusic();
            pause.setChecked(true);
        }else if(view==backImg){
            backImg.setVisibility(View.GONE);
            lrcView.setVisibility(View.VISIBLE);
            if(visualizerView!=null)
            visualizerView.setVisibility(View.GONE);
        }else if(view==visualizerView){
            backImg.setVisibility(View.VISIBLE);
            lrcView.setVisibility(View.GONE);
            if(visualizerView!=null)
                visualizerView.setVisibility(View.GONE);
        }else{
            System.out.println("other");
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        binder.seekTo(seekBar.getProgress());
    }

    @Override
    public void onPlayComplate() {
        changeMusic();
    }

    @Override
    public void startProgressBar(int duration) {
        System.out.println("Activity开始progress");
        seekBar.setMax(duration);
        songDuration.setText(TimeUtil.timeToString(duration));
        presenter.undateProgress(duration, binder);
    }

    @Override
    public void setCurrProgress(int curr) {
        currPlayedTime.setText(TimeUtil.timeToString(curr));
        seekBar.setProgress(curr);
        lrcView.setIndex(lrcView.getIndexByLrcTime(curr));
    }

    @Override
    public void showSnack(CharSequence msg) {
        Snackbar.make(lrcView,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setLrc(List<LrcContent> lrc) {
        lrcView.setLrcLists(lrc);
    }

    @Override
    public void setSongImg(String imgUrl) {
        if(imgUrl!=null&&imgUrl.length()>0)
            Picasso.with(this).load(imgUrl).fit().into(backImg);
    }

    @Override
    public void startPlay() {
        backImg.startRotate();
    }

    private void changeMusic(){
        if(songs==null||songs.getSong_list().size()==0){getData();return;}
        backImg.setCurrentAngle(0);
        backImg.stopRotate();
        Picasso.with(this).load(R.drawable.playback).into(backImg);
        int position = new Random(System.currentTimeMillis()).nextInt(Configs.MUSICCOUNT);
        String songId = songs.getSong_list().get(position).getSong_id();
        pause.setChecked(true);
        presenter.changeMusic(binder,songId);

    }

    @Override
    public void getData() {
        presenter.getData(this);
    }

    @Override
    public void onSuccess(Object o) {
        songs = (SongRankingBean)o;
        changeMusic();
    }
    private void setupEqualizeFxAndUi() {

        TextView kongge = new TextView(this);
        kongge.setText("");
        kongge.setTextSize(10);
        //mLayout.addView(kongge);

        mEqualizer = new Equalizer(0, binder.getPlayer().getAudioSessionId());
        mEqualizer.setEnabled(true);// 启用均衡器

        // 通过均衡器得到其支持的频谱引擎
        short bands = mEqualizer.getNumberOfBands();

        // getBandLevelRange 是一个数组，返回一组频谱等级数组，
        // 第一个下标为最低的限度范围
        // 第二个下标为最大的上限,依次取出
        final short minEqualizer = mEqualizer.getBandLevelRange()[0];
        final short maxEqualizer = mEqualizer.getBandLevelRange()[1];
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (short i = 0; i < bands; i++) {
            final short band = i;

            TextView freqTextView = new TextView(this);
            freqTextView.setTextColor(Color.WHITE);
            freqTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            // 取出中心频率
            freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000) + "HZ");
            layout.addView(freqTextView);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(this);
            minDbTextView.setTextColor(Color.WHITE);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEqualizer / 100) + " dB");

            TextView maxDbTextView = new TextView(this);
            maxDbTextView.setTextColor(Color.WHITE);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEqualizer / 100) + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 40);


            layoutParams.weight = 1;
            SeekBar seekbar = new SeekBar(this);
            seekbar.setLayoutParams(layoutParams);
            seekbar.setPadding(15, 0, 15, 0);
            seekbar.setThumb(getResources().getDrawable(R.drawable.seek_bar_dian_selector));
            seekbar.setThumbOffset(20);
            seekbar.setMax(maxEqualizer - minEqualizer);
            seekbar.setProgress(maxEqualizer);


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel(band, (short) (progress + minEqualizer));
                }
            });
            row.addView(minDbTextView);
            row.addView(seekbar);
            row.addView(maxDbTextView);

            layout.addView(row);

        }
        //mLayout.addView(layout);
        TextView eqTextView = new TextView(this);
        eqTextView.setTextColor(Color.WHITE);
        eqTextView.setText("均衡器");
        eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        eqTextView.setTextSize(20);

        //mLayout.addView(eqTextView);

    }
    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi() {
        if(visualizerView==null) {
            visualizerView = new VisualizerView(this);

            visualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,//宽度
                    (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density//高度
                    )));
            visualizerView.setPadding(10, 50, 10, 50);
            mLayout.setGravity(Gravity.CENTER);
            //将频谱View添加到布局
            mLayout.addView(visualizerView);
            //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
            mVisualizer = new Visualizer(binder.getPlayer().getAudioSessionId());
            //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            //设置允许波形表示，并且捕获它
            visualizerView.setVisualizer(mVisualizer);
            visualizerView.setOnClickListener(this);
            setupEqualizeFxAndUi();//添加均衡器到界面
        }
        visualizerView.setEnabled(true);
        //mVisualizer.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        this.unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onLrcClick(LrcView view) {
        setupVisualizerFxAndUi();
        if(visualizerView!=null)
            visualizerView.setVisibility(View.VISIBLE);
        backImg.setVisibility(View.GONE);
        lrcView.setVisibility(View.GONE);
    }
}
