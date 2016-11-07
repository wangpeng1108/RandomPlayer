package com.wangpeng.myplayer.ui.PlayerPage.Retrofit;

import com.wangpeng.myplayer.beans.PlayBean;
import com.wangpeng.myplayer.beans.SongRankingBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by WP on 16/11/6.
 */

public interface DataService {
    public static final String BASEURL="http://tingapi.ting.baidu.com/";

    @GET("v1/restserver/ting")
    Observable<SongRankingBean> getSongRanking (@Query("format") String format, @Query("callback")String callback, @Query("from") String from,
                                                @Query("method") String method , @Query("type") int type, @Query("size") int size,
                                                @Query("offset") int offset);

    @GET("v1/restserver/ting")
    Observable<PlayBean> getPlay (@Query("format") String format, @Query("callback")String callback, @Query("from") String from,
                            @Query("method") String method , @Query("songid") String songid);

    @GET("{URL}")
    Call<String> getLrc(@Path("URL") String url);
}
