package com.wangpeng.myplayer.base.MVP.presenter;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.service.PlayerService;

/**
 * Created by WP on 16/11/5.
 */

public interface Presenter<V,M> {

    public void init();

    public void getData(Model.OnResponse response);


}
