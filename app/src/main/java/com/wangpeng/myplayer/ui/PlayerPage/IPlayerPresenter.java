package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.presenter.Presenter;
import com.wangpeng.myplayer.service.Player;
import com.wangpeng.myplayer.service.PlayerService;

/**
 * Created by WP on 16/11/6.
 */

public interface IPlayerPresenter extends Presenter{
    public void changeMusic();

    public void undateProgress(int duration);

    public void getLrc(String url);

    public boolean playControl(String command);

    public void setBinder(Player binder);

    public void seekTo(int curr);
}
