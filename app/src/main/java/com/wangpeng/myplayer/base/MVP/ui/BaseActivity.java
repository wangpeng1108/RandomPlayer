package com.wangpeng.myplayer.base.MVP.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.wangpeng.myplayer.MyApplication;
import com.wangpeng.myplayer.base.MVP.model.Model;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by WP on 16/11/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView,View.OnClickListener,Model.OnResponse,SeekBar.OnSeekBarChangeListener{
    private Unbinder unbinder;
    public MyApplication application;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        application = (MyApplication)getApplication();
        unbinder = ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE,(int)alphaValue);
            //getDrawer().setClipToPadding(false);
            //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }else
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView(savedInstanceState);
        getData();
    }

    @Override
    public void getData(){};
    @Override
    public void onViewClick(View view){};
    @Override
    public void onSuccess(Object o){};
    @Override
    public void onFailed(String msg){};

    @Override
    public void onClick(View view) {
        onViewClick(view);
    }

    @Override
    public void setMyTitle(CharSequence title) {
        setTitle(title);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
