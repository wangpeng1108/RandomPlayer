package com.wangpeng.myplayer;

import com.wangpeng.myplayer.base.MVP.model.Model;
import com.wangpeng.myplayer.beans.SongRankingBean;
import com.wangpeng.myplayer.ui.PlayerPage.PlayerModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest implements Model.OnResponse{
    @Test
    public void addition_isCorrect() throws Exception {
        PlayerModel playerModel = new PlayerModel();
        playerModel.getData(this);
    }


    @Override
    public void onSuccess(Object o) {
        System.out.println("测试获取数据："+((SongRankingBean)o).getSong_list().size());
    }

    @Override
    public void onFailed(String msg) {
        System.out.println("测试出错："+msg);
    }
}