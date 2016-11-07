package com.wangpeng.myplayer.base.MVP.presenter;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.service.PlayerService;

/**
 * Created by WP on 16/11/5.
 */

public interface Presenter<V,M> {

    /**
     * 初始化Presenter
     */
    public void init();

    /**
     * 获取数据
     */
    public void getData();

    /**
     * 销毁前回调
     */
    public void onDestroy();
}
