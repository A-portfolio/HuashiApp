package com.muxistudio.appcommon.user;

import android.text.TextUtils;

import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.common.util.PreferenceUtil;


/**
 * Created by fengminchao on 18/3/19
 * user info manager class
 */

public class UserAccountManager {

    //信息门户的 user，也就作为主 user
    private User mInfoUser = new User();
    //图书馆的 user
    private User mLibUser = new User();

    private String mPHPSESSID;
    private EncryptionUser encryptionUser;
    private static class SingletonHolder {
        private static final UserAccountManager INSTANCE = new UserAccountManager();
    }

    public UserAccountManager(){
        encryptionUser =new EncryptionUser();
    }
    public static UserAccountManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * init user info if user has login before
     */
    public void initUser() {
        mInfoUser.setSid(PreferenceUtil.getString(PreferenceUtil.STUDENT_ID, ""));
        mInfoUser.setPassword(PreferenceUtil.getString(PreferenceUtil.STUDENT_PWD, ""));

        try {


            if (mInfoUser.sid.length() != 0) {
                mInfoUser.setSid(encryptionUser.decryptAES(mInfoUser.sid));
            }
            if (mInfoUser.password.length() != 0) {
                mInfoUser.setPassword(encryptionUser.decryptAES(mInfoUser.password));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mLibUser.setSid(PreferenceUtil.getString(PreferenceUtil.LIBRARY_ID, ""));
        mLibUser.setPassword(PreferenceUtil.getString(PreferenceUtil.LIBRARY_PWD, ""));
    }

    public User getLibUser() {
        return mLibUser;
    }

    public User getInfoUser() {
        return mInfoUser;
    }

    public boolean isInfoUserLogin(){
      return !TextUtils.isEmpty(getInfoUser().sid) && !TextUtils.isEmpty(getInfoUser().password);
    }

    /**
     * 用户换账号登录交给此方法处理
     *
     * @param infoUser
     */
    public void saveInfoUser(User infoUser) {
        try {
            PreferenceUtil.saveString(PreferenceUtil.STUDENT_ID, encryptionUser.encryptAES(infoUser.getSid()));
            PreferenceUtil.saveString(PreferenceUtil.STUDENT_PWD, encryptionUser.encryptAES(infoUser.getPassword()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        mInfoUser.sid = infoUser.sid;
        mInfoUser.password = infoUser.password;
    }

    @Deprecated
    public void saveLibUser(User libUser) {
        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_ID, libUser.getSid());
        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_PWD, libUser.getPassword());
        mLibUser.sid = libUser.sid;
        mLibUser.password = libUser.password;
    }

    public void logoutInfoUser() {
        PreferenceUtil.clearString(PreferenceUtil.STUDENT_ID);
        PreferenceUtil.clearString(PreferenceUtil.STUDENT_PWD);
        PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
        PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
        PreferenceUtil.clearString(PreferenceUtil.PHPSESSID);
        mInfoUser.setSid("");
        mInfoUser.setPassword("");
        //原先数据表没设计好，课程那块应该加个用户 id 字段，有空可以加
        HuaShiDao dao = new HuaShiDao();
        dao.deleteAllCourse();
    }

    @Deprecated
    public void logoutLibUser() {
        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_ID);
        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_PWD);
        PreferenceUtil.clearString(PreferenceUtil.ATTENTION_BOOK_IDS);
        PreferenceUtil.clearString(PreferenceUtil.BORROW_BOOK_IDS);
        PreferenceUtil.clearString(PreferenceUtil.PHPSESSID);
        mLibUser.setSid("");
        mLibUser.setPassword("");
        CcnuCrawler2.clear();
    }

    public boolean isInfoLogin() {
        return (mInfoUser != null && !TextUtils.isEmpty(mInfoUser.sid));
    }

    @Deprecated
    public boolean isLibLogin() {
        String phpSess = PreferenceUtil.getString(PreferenceUtil.PHPSESSID);
      return !TextUtils.isEmpty(phpSess);
    }

    public String getPHPSESSID() {
        return mPHPSESSID;
    }

    public void setPHPSESSID(String PHPSESSID) {
        mPHPSESSID = PHPSESSID;
    }
}
