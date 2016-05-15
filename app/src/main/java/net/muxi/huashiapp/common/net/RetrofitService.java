package net.muxi.huashiapp.common.net;

import net.muxi.huashiapp.common.Api;
import net.muxi.huashiapp.common.data.MainLoginResponse;
import net.muxi.huashiapp.common.data.User;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ybao on 16/4/28.
 *
 */
public interface RetrofitService {

    @Headers("Content-Type:application/json")
    @POST(Api.MAIN_LOGIN)
    Observable<MainLoginResponse> mainLogin(@Body User user);
}
