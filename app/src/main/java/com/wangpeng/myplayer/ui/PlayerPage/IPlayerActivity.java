package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.ui.BaseActivity;
import com.wangpeng.myplayer.base.MVP.ui.BaseView;
import com.wangpeng.myplayer.beans.LrcContent;
import com.wangpeng.myplayer.beans.SongRankingBean;

import java.util.List;

/**
 * Created by WP on 16/11/6.
 */

public interface IPlayerActivity extends BaseView{
    void onPlayComplate();

    /**
     * 初始化总长度
     * @param duration 长度
     */
    void startProgressBar(int duration);

    /**
     * 更新当前已播放长度
     * @param curr 长度
     */
    void setCurrProgress(int curr);

    void showSnack(CharSequence msg);

    void setLrc(List<LrcContent> lrc);

    void setSongImg(String imgUrl);

    void prePlay();

    void startPlay();

    void pausePlay();


}
