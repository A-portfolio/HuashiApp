package net.muxi.huashiapp.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/4/19.
 * SharedPreferences存储类
 */
public class PreferenceUtil {

    private SharedPreferences mSharedPreferences;
    //上次登录的用户账号,密码
    public static final String STUDENT_ID = "sId";
    public static final String STUDENT_PWD = "sPwd";
    public static final String LIBRARY_ID = "libraryId";
    public static final String LIBRARY_PWD = "libraryPwd";
    public static final String COURSE_ID = "course_id";
    //课程表中的当前周
    public static final String FIRST_WEEK_DATE = "first_date";
    //当前出了成绩的科目数
    public static final String SCORES_NUM = "score_num";
    //校历的最近更新时间
    public static final String CALENDAR_UPDATE = "calendar_update";
    //校历的图片地址
    public static final String CALENDAR_ADDRESS = "calendar_address";
    //电费的查询参数
    public static final String ELE_QUERY_STRING = "ele_query";
    //更新前的 App 版本
    public static final String LAST_APP_VERSION = "last_app_version";
    //校历的尺寸
    public static final String CALENDAR_SIZE = "calendar_size";
    //首页产品更新时间
    public static final String PRODUCT_UPDATE = "product_update";
    //首页产品 json 数据
    public static final String PRODUCT_DATA = "product_data";

    public PreferenceUtil() {
        Context context = App.getContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }


    public String getString(String key,String def){
        return mSharedPreferences.getString(key,def);
    }


    //可用于用户上次使用后注销账号时移除账号
    public void clearString(String key){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public void saveInt(String key,int value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public void saveLong(String key,long value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }

    public void saveFloat(String key,float value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public float getFloat(String key){
        return mSharedPreferences.getFloat(key,-1);
    }

    public long getLong(String key){
        return mSharedPreferences.getLong(key,-1);
    }

    public int getInt(String key){
        return mSharedPreferences.getInt(key,-1);
    }

    public int getInt(String key,int def){
        return mSharedPreferences.getInt(key,def);
    }

    /**
     * 清楚所有的数据,在注销时使用
     */
    public void clearAllData(){
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
    }

}
