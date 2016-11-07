package com.wangpeng.myplayer.base.MVP.model;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by WP on 16/11/5.
 */

public interface Model {
    /**
     * 获取数据回调
     * @param <T>   T:返回值类型
     */
    public interface OnResponse<T>{
        public void onSuccess(T t);
        public void onFailed(String msg);
    }

    /**
     * 获取数据
     * @param response 回调接口
     */
    public void getData(OnResponse response);
}
