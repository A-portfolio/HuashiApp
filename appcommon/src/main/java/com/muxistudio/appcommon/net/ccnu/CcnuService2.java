package com.muxistudio.appcommon.net.ccnu;

import com.muxistudio.appcommon.data.CardDailyUse;
import com.muxistudio.appcommon.data.CardDataEtp;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kolibreath on 17-12-10.
 */

public interface CcnuService2 {
    //学校部分的登录
    @POST("https://account.ccnu.edu.cn/cas/login;jsessionid={jsession}")
    @FormUrlEncoded
    Call<ResponseBody> performCampusLogin(@Path("jsession") String jsession, @Field("username")
            String usrname, @Field("password") String password
            , @Field("lt") String valueOfLt, @Field("execution") String valueOfExe,
                                          @Field("_eventId") String submit, @Field("submit") String login);

    //教务系统的登录
    //需要携带cookie cookie没有放在header里面
    @GET("http://xk.ccnu.edu.cn/ssoserver/login?ywxt=jw&url=xtgl/index_initMenu.html")
    Call<ResponseBody> performSystemLogin();

    //模拟登录的第一步:获取第一个Location
        @GET("http://202.114.34.15/reader/hwthau.php")
    Call<ResponseBody> performLibLogin1();
    //模拟登录的第二步 获取第二个Location
    @GET("https://account.ccnu.edu.cn/cas/login?service=http%3A%2F%2F202.114.34.15%2Freader%2Fhwthau.php")
    Call<ResponseBody> performLibLogin2();
    //模拟登陆的第三步 获取第三个Location
    @GET("http://202.114.34.15/reader/hwthau.php?ticket={ticket}")
    Call<ResponseBody> performLibLogin3(@Path("ticket")String ticket, @Header("token")String tokjne);

    @GET("http://weixin.ccnu.edu.cn/App/weixin/CardInfoAjax")
    rx.Observable<CardDataEtp> getCardData(@HeaderMap Map<String, String> headers );

    /**
     *
     * @param headers
     * @param page
     * @param pageSize 尽可能的大一些,可以取出更多的数据
     * @param startTime 2018-04-28 这样的格式
     * @param endTime   2018-04-28 这样的格式
     * @return
     */
    @GET("http://weixin.ccnu.edu.cn/App/weixin/queryTrans")
    Observable<CardDailyUse> getCardDailyData(@HeaderMap Map<String,String> headers,
                                              @Query("page") int page,
                                              @Query("pageSize") int pageSize,
                                              @Query("startTime") String startTime,
                                              @Query("endTime") String endTime);
}
