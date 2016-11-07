package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.Model;

/**
 * Created by WP on 16/11/6.
 */

public interface IPlayerModel extends Model{
    /**
     * 获取歌曲详情
     * @param songId 歌曲ID
     * @param response  反馈回调
     */
    public void getMusicInfo(String songId,Model.OnResponse response);
    /**
     * 获取歌词
     * @param lrcUrl 歌词地址
     * @param response  反馈回调
     */
    public void getLrcContent(String lrcUrl,Model.OnResponse response);
}
