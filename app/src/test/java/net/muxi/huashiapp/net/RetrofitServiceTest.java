package net.muxi.huashiapp.net;

import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.common.data.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/9
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RetrofitServiceTest {

    private RetrofitService retrofitService;
    private User user;
    private User libuser;

    @Before
    public void setUp() throws Exception {
        retrofitService = CampusFactory.getRetrofitService();
        String usernum = "2014214629";
        String pwd = "fmc2014214629";
        String libPwd = "123456";
        user = new User();
        libuser = new User();
        user.sid = usernum;
        user.password = pwd;
        libuser.sid = usernum;
        libuser.password = libPwd;
    }

    @Test
    public void mainLogin() throws Exception {
//        retrofitService.mainLogin(Base64Util.createBaseStr(user))
//                .observeOn(Schedulers.immediate())
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(response -> {
//                    assertEquals(200,response.code());
//                },throwable -> fail());
    }

    @Test
    public void libLogin() throws Exception {

    }

    @Test
    public void searchBook() throws Exception {

    }

    @Test
    public void getBookDetail() throws Exception {

    }

    @Test
    public void getPersonalBook() throws Exception {

    }

    @Test
    public void getAttentionBooks() throws Exception {

    }

    @Test
    public void createAttentionBook() throws Exception {

    }

    @Test
    public void delAttentionBook() throws Exception {

    }

    @Test
    public void renewBook() throws Exception {

    }

    @Test
    public void getSchedule() throws Exception {

    }

    @Test
    public void addCourse() throws Exception {

    }

    @Test
    public void deleteCourse() throws Exception {

    }

    @Test
    public void updateCourse() throws Exception {

    }

    @Test
    public void getScores() throws Exception {

    }

    @Test
    public void getDetailScores() throws Exception {

    }

    @Test
    public void getNews() throws Exception {

    }

    @Test
    public void getCalendar() throws Exception {

    }

    @Test
    public void getBanner() throws Exception {

    }

    @Test
    public void getApartment() throws Exception {

    }

    @Test
    public void getElectricity() throws Exception {

    }

    @Test
    public void getCardBalance() throws Exception {

    }

    @Test
    public void getLatestVersion() throws Exception {

    }

    @Test
    public void downloadFile() throws Exception {

    }

    @Test
    public void getPatch() throws Exception {

    }

    @Test
    public void getSplash() throws Exception {

    }

    @Test
    public void getProduct() throws Exception {

    }

    @Test
    public void getWebsite() throws Exception {

    }

    @Test
    public void getClassRoom() throws Exception {

    }

}