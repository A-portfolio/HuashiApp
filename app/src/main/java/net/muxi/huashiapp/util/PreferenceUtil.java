package net.muxi.huashiapp.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/4/19.
 * SharedPreferences存储类
 */
public class PreferenceUtil {

    //上次登录的用户账号,密码
    public static final String STUDENT_ID = "sId";
    public static final String STUDENT_PWD = "sPwd";
    public static final String LIBRARY_ID = "libraryId";
    public static final String LIBRARY_PWD = "libraryPwd";
    public static final String COURSE_ID = "course_id";

    //课程表
    //课表中的第一周的第一天日期
    public static final String FIRST_WEEK_DATE = "first_date";
    //是否是第一次进入课程表
    public static final String IS_FIRST_ENTER_TABLE = "is_first_enter_table";

    //成绩
    //当前出了成绩的科目数
    public static final String SCORES_NUM = "score_num";

    //校历
    //校历的最近更新时间
    public static final String CALENDAR_UPDATE = "calendar_update";
    //校历的图片地址
    public static final String CALENDAR_ADDRESS = "calendar_address";
    //校历的尺寸
    public static final String CALENDAR_SIZE = "calendar_size";

    //电费的查询参数
    public static final String ELE_QUERY_STRING = "ele_query";

    //空闲教室的查询参数
    public static final String STUDY_ROOM_QUERY_STRING = "study_room_query";
    //更新前的 App 版本
    public static final String LAST_APP_VERSION = "last_app_version";

    //首页产品更新时间
    public static final String PRODUCT_UPDATE = "product_update";
    //首页产品 json 数据
    public static final String PRODUCT_DATA = "product_data";
    //是否是初次选择周数
    public static final String FIRST_SELECT_WEEK = "first_select_week";
    //保存的上一次不再提醒的版本号
    public static final String LAST_NOT_REMIND_VERSION = "not_remind_version";
    //提醒更新的状态,在有新版本的时候自动开启
    public static final String REMIND_UPDATE = "remind_update";
    //最新的通知公告日期
    public static final String LATEST_NEWS_DATE = "latest_news_date";

    //图书馆
    //关注图书的列表
    public static final String ATTENTION_BOOK_IDS = "attention_book_ids";
    //借阅图书的列表
    public static final String BORROW_BOOK_IDS = "borrow_book_ids";

    //判断是否是第一次启动应用
    public static final String APP_FIRST_OPEN = "first_open";

    //是否第一次进入主界面
    public static final String IS_FIRST_ENTER_MAIN = "is_first_enter_main";


    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean def) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getBoolean(key, def);
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getString(key, "");
    }


    public static String getString(String key, String def) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getString(key, def);
    }


    //可用于用户上次使用后注销账号时移除账号
    public static void clearString(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.remove(key);
        editor.apply();
    }


    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getFloat(key, -1);
    }

    public static long getLong(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getLong(key, -1);
    }

    public static int getInt(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getInt(key, -1);
    }

    public static int getInt(String key, int def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getInt(key, def);
    }

    /**
     * 清楚所有的数据,在注销时使用
     */
    public void clearAllData() {
        // TODO: 17/1/27 clear data
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
    }


}
