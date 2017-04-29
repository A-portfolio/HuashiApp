package net.muxi.huashiapp.net;

import net.muxi.huashiapp.common.data.ApartmentData;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.data.BookId;
import net.muxi.huashiapp.common.data.BookPost;
import net.muxi.huashiapp.common.data.BookSearchResult;
import net.muxi.huashiapp.common.data.BorrowedBook;
import net.muxi.huashiapp.common.data.CalendarData;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.ClassRoom;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.EleRequestData;
import net.muxi.huashiapp.common.data.Electricity;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.common.data.News;
import net.muxi.huashiapp.common.data.PatchData;
import net.muxi.huashiapp.common.data.ProductData;
import net.muxi.huashiapp.common.data.RenewData;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.data.SplashData;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.data.VersionData;
import net.muxi.huashiapp.common.data.WebsiteData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by ybao on 16/4/28.
 */
public interface RetrofitService {

    @GET("info/login/")
    Call<ResponseBody> mainLogin(@Header("Authorization") String verification);

    @GET("lib/login/")
    Observable<Response<VerifyResponse>> libLogin(@Header("Authorization") String verification);

    @GET("lib/search/")
    Observable<BookSearchResult> searchBook(@Query("keyword") String keyword,
            @Query("page") int page);

    @GET("lib/")
    Observable<Book> getBookDetail(@Query("id") String id);

    @GET("lib/me/")
    Observable<List<BorrowedBook>> getPersonalBook(@Header("Authorization") String verification);

    /**
     * 200 OK
     * 403 禁止访问
     * 404 无关注图书
     * 502 服务器端错误
     */
    @GET("lib/get_atten/")
    Observable<Response<List<AttentionBook>>> getAttentionBooks(
            @Header("Authorization") String verification);

    /**
     * 201 添加关注成功
     * 403 禁止访问
     * 409 已关注
     * 502 服务器端错误
     */
    @POST("lib/create_atten/")
    Observable<Response<VerifyResponse>> createAttentionBook(
            @Header("Authorization") String verification, @Body
            BookPost bookPost);

    /**
     * 200 OK
     * 403 禁止访问
     * 404 未找到图书
     * 502 服务器端错误
     */
//    @DELETE("lib/del_atten/")
    @HTTP(method = "DELETE", path = "lib/del_atten/", hasBody = true)
    Observable<Response<VerifyResponse>> delAttentionBook(
            @Header("Authorization") String verification,
            @Body BookId id);

    /**
     * 200 续借成功
     * 406 不到续借时间
     * 403 超过最大续借次数
     * 400 请求无效
     */
    @POST("lib/renew/")
    Observable<Response<VerifyResponse>> renewBook(@Header("Authorization") String verification,
            @Body RenewData renewData);

    @GET("table/")
    Observable<List<Course>> getSchedule(@Header("Authorization") String verification,
            @Query("sid") String sid);

    //添加课程
    @POST("table/")
    Observable<Response<VerifyResponse>> addCourse(@Header("Authorization") String authorization,
            @Body Course course);

    //删除课程
    @DELETE("table/{id}/")
    Observable<Response<VerifyResponse>> deleteCourse(@Header("Authorization") String authorization,
            @Path("id") String id);

    @PUT("table/{id}/")
    Observable<Response<VerifyResponse>> updateCourse(@Header("Authorization") String authorization,
            @Path("id") String id,
            @Body Course course);

    //URL: /api/grade/search/?xnm=2015&xqm=3
    @GET("grade/search/")
    Observable<List<Scores>> getScores(@Header("Authorization") String verification,
            @Query("xnm") String year,
            @Query("xqm") String term);

    @GET("/grade/detail/search")
    Observable<DetailScores> getDetailScores(@Header("Authorization") String verification,
            @Query("xnm") String year,
            @Query("xqm") String term,
            @Query("course") String course,
            @Query("jxb_id") String jxbId);

    @GET("webview_info/")
    Observable<List<News>> getNews();

    @GET("calendar/")
    Observable<CalendarData> getCalendar();

    @GET("banner/")
    Observable<List<BannerData>> getBanner();

    @GET("apartment/")
    Observable<List<ApartmentData>> getApartment();

    @POST("ele/")
    Observable<Response<Electricity>> getElectricity(@Body EleRequestData requestData);

    //查询余额  除了学号其他传固定值 http://console.ccnu.edu
    // .cn/ecard/getTrans?userId=2013211389&days=90&startNum=0&num=200
    @GET("http://console.ccnu.edu.cn/ecard/getTrans")
    Observable<List<CardData>> getCardBalance(@Query("userId") String sid,
            @Query("days") String day,
            @Query("startNum") String start,
            @Query("num") String num);

    @GET("app/latest/")
    Observable<VersionData> getLatestVersion();

    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @GET("patch/")
    Observable<List<PatchData>> getPatch();

    @GET("start/")
    Observable<SplashData> getSplash();

    @GET("product/")
    Observable<ProductData> getProduct();

    @GET("site/")
    Observable<List<WebsiteData>> getWebsite();

    @GET("classroom/get_classroom/")
    Observable<ClassRoom> getClassRoom(@Query("weekno") String week,
            @Query("weekday") String day,
            @Query("building") String area);

    @POST("http://120.77.8.149:8090/api/grade/search/")
    Observable<List<Scores>> getScores(@Body InfoCookie cookie,
            @Query("xnm") String year,
            @Query("xqm") String term);


}
