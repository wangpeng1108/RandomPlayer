package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.Model;

/**
 * Created by WP on 16/11/6.
 */

public interface IPlayerModel extends Model{
    public void getMusicInfo(String songId,Model.OnResponse response);

    public void getLrcContent(String lrcUrl,Model.OnResponse response);
}
