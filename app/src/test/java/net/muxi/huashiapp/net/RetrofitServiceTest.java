package net.muxi.huashiapp.net;

import net.muxi.huashiapp.BuildConfig;
import com.muxistudio.appcommon.data.User;

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
    public void setUp() {
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
    public void mainLogin() {
//        retrofitService.mainLogin(Base64Util.createBaseStr(user))
//                .observeOn(Schedulers.immediate())
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(response -> {
//                    assertEquals(200,response.code());
//                },throwable -> fail());
    }

    @Test
    public void libLogin() {

    }

    @Test
    public void searchBook() {

    }

    @Test
    public void getBookDetail() {

    }

    @Test
    public void getPersonalBook() {

    }

    @Test
    public void getAttentionBooks() {

    }

    @Test
    public void createAttentionBook() {

    }

    @Test
    public void delAttentionBook() {

    }

    @Test
    public void renewBook() {

    }

    @Test
    public void getSchedule() {

    }

    @Test
    public void addCourse() {

    }

    @Test
    public void deleteCourse() {

    }

    @Test
    public void updateCourse() {

    }

    @Test
    public void getScores() {

    }

    @Test
    public void getDetailScores() {

    }

    @Test
    public void getNews() {

    }

    @Test
    public void getCalendar() {

    }

    @Test
    public void getBanner() {

    }

    @Test
    public void getApartment() {

    }

    @Test
    public void getElectricity() {

    }

    @Test
    public void getCardBalance() {

    }

    @Test
    public void getLatestVersion() {

    }

    @Test
    public void downloadFile() {

    }

    @Test
    public void getPatch() {

    }

    @Test
    public void getSplash() {

    }

    @Test
    public void getProduct() {

    }

    @Test
    public void getWebsite() {

    }

    @Test
    public void getClassRoom() {

    }

}