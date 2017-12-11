package net.muxi.huashiapp.net.ccnu;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/4/24
 */

public interface CcnuService {


    @POST("http://portal.ccnu.edu.cn/loginAction.do")
    @FormUrlEncoded
    Call<ResponseBody> loginInfo(@Field("userName") String name, @Field("userPass") String pass);

    @Headers("timeout:4")
    @GET("http://portal.ccnu.edu.cn/roamingAction.do?appId=XK")
    Call<ResponseBody> updateCookie();
    @Headers("timeout:4")
    @GET("http://122.204.187.6/xtgl/login_tickitLogin.html")
    Call<ResponseBody> updateFinalCookie();

}
