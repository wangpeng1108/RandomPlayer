package com.wangpeng.myplayer.base.MVP.model;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by WP on 16/11/5.
 */

public interface Model {
    public interface OnResponse<T>{
        public void onSuccess(T t);
        public void onFailed(String msg);
    }

    public void getData(OnResponse response);
}
