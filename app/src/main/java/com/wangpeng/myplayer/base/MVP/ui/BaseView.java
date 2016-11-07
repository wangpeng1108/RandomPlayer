package com.wangpeng.myplayer.base.MVP.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wangpeng.myplayer.base.MVP.model.Model;

/**
 * Created by WP on 16/11/5.
 */

public interface BaseView {
    int getContentView();
    void initView(Bundle savedInstanceState);
    void getData();
    void onViewClick(View view);

    void setMyTitle(CharSequence title);


}
