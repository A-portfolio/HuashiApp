package net.muxi.huashiapp.net.ccnu;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}
