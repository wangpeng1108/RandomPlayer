package com.wangpeng.myplayer.ui.PlayerPage;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.base.MVP.presenter.Presenter;
import com.wangpeng.myplayer.service.PlayerService;

/**
 * Created by WP on 16/11/6.
 */

public interface IPlayerPresenter extends Presenter{
    public void changeMusic(PlayerService.MusicPlayerBinder binder,String songId);

    public void undateProgress(int duration, PlayerService.MusicPlayerBinder binder);

    public void getLrc(String url);
}
