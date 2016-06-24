package net.muxi.huashiapp.common.net;

import net.muxi.huashiapp.common.Api;
import net.muxi.huashiapp.common.data.BookSearchResult;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.PersonalBook;
import net.muxi.huashiapp.common.data.VerifyResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by ybao on 16/4/28.
 *
 */
public interface RetrofitService {

//    @GET(Api.INFO_LOGIN)
//    Observable<VerifyResponse> mainLogin(@Header("Authorization") String verification);

    @GET(Api.INFO_LOGIN)
    Observable<Response<VerifyResponse>> mainLogin(@Header("Authorization") String authorization);

    @GET(Api.LIB_LOGIN)
    Observable<Response<VerifyResponse>> libLogin(@Header("Authorization") String verification);

    @GET(Api.LIB_SEARCH)
    Observable<BookSearchResult> searchBook();

    @GET(Api.LIB_MINE)
    Observable<PersonalBook> getPersonalBook(@Header("Authorizaiton") String verification);

    @GET(Api.SCHEDULE)
    Observable<List<Course>> getSchedule(@Header("Authorization") String verification);

}
