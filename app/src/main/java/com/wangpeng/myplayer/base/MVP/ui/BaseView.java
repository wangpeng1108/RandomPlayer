package com.wangpeng.myplayer.base.MVP.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wangpeng.myplayer.base.MVP.model.Model;

/**
 * Created by WP on 16/11/5.
 */

public interface BaseView {
    /**
     * 设置Layout
     * @return
     */
    int getContentView();

    /**
     * 初始化view
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 获取数据
     */
    void getData();

    /**
     * 点击事件
     * @param view
     */
    void onViewClick(View view);

    /**
     * 设置活动标题
     * @param title
     */
    void setMyTitle(CharSequence title);


}
