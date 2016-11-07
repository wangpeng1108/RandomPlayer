package com.wangpeng.myplayer.base.MVP.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by WP on 16/11/4.
 */

public abstract class BaseFragment extends Fragment{

    protected Context context;
    protected View mRootView;
    private Unbinder unbinder;

    /**
     * 设置布局
     * @return
     */
    public abstract int getContentViewId();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView =inflater.inflate(getContentViewId(),container,false);
        unbinder = ButterKnife.bind(this,mRootView);//绑定fragment
        this.context = getActivity();
        initAllMembersView(savedInstanceState);
        return mRootView;
    }

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();//解绑
    }

}