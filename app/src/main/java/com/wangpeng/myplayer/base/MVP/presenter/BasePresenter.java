package com.wangpeng.myplayer.base.MVP.presenter;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.ui.BaseView;

/**
 * Created by WP on 16/11/5.
 */

public abstract class BasePresenter<V extends BaseView,M extends Model> implements Presenter{
    public V view;
    public M model;

    public BasePresenter(V v){
        view = v;
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public abstract void getData();

    @Override
    public void onDestroy() {

    }
}
