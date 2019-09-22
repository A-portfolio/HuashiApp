package com.muxistudio.appcommon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muxistudio.common.base.Global;
import com.muxistudio.common.util.Logger;


/**
 * Created by ybao on 16/5/12.
 */
public class DataBase extends SQLiteOpenHelper {

    private static DataBase instance;

    private static final String DB_NAME = "huashi_db";
    private static final int DB_VERSION = 5;

    //图书馆搜索的历史记录
    public static final String TABLE_SEARCH_HISTORY = "search_history";
    //课程的本地数据库
    public static final String TABLE_COURSE = "course";
    public static final String TABLE_BANNER = "banner";
    public static final String TABLE_APARTMENT = "apartment";
    public static final String TABLE_LIB = "lib";
    public static final String TABLE_WEBSITE = "website";

    public static final String KEY_ID = "id";

    //图书的属性
    public static final String KEY_BOOK = "book";
    public static final String KEY_LIBRARY_USER_ID = "library_id";

    //课程的属性
    public static final String KEY_COURSE_NAME = "course";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_WEEKS = "weeks";
    public static final String KEY_WEEKDAY = "day";
    public static final String KEY_TIME = "start";
    public static final String KEY_DURATION = "during";
    public static final String KEY_PLACE = "place";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_COLOR = "color";

    //banner的属性
    public static final String KEY_URL = "url";
    public static final String KEY_UPDATE = "update_time";
    public static final String KEY_IMG = "img";
    public static final String KEY_FILENAME = "filename";
    public static final String KEY_NUM = "num";


    //apartment 的属
    public static final String KEY_APART = "apart";
    public static final String KEY_TELE = "tele";
    public static final String KEY_APART_PLACE = "place";

    //website的属性
    public static final String KEY_SITE = "site";
    public static final String KEY_SITE_URL = "url";

    //library key
    public static final String KEY_BOOK_NAME = "book_name";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_BID = "bid";
    public static final String KEY_INTRO = "book_intro";
    public static final String KEY_BOOK_ID = "book_id";
    public static final String KEY_SEARCH = "search";

    /**
     * pid : String
     * deviceId : String
     * type : String
     * mainCat : String
     * subCat : String
     * value : String
     * timestamp : Int
     * extra : String
     */
    //StatisticsData
    public static final String S_PID="pid";
    public static final String S_DEVICEID="deviceId";
    public static final String S_TYPE="type";
    public static final String S_MAINCAT="mainCat";
    public static final String S_SUBCAT="subCat";
    public static final String S_VALUE="value";
    public static final String S_TIMESTAMP="timestamp";
    public static final String S_EXTRA="extra";


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance==null)
                    instance = new DataBase(Global.getApplication(), DB_NAME, null, DB_VERSION);
            }
        }
        return instance.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSearchHistory = "CREATE TABLE IF NOT EXISTS " + TABLE_SEARCH_HISTORY +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_LIBRARY_USER_ID + " TEXT, " +
                KEY_BOOK + " TEXT);";
        db.execSQL(createSearchHistory);

        String createCourseTable = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE +
                " ( " + KEY_ID + " TEXT, " +
                KEY_COURSE_NAME + " TEXT, " +
                KEY_TEACHER + " TEXT, " +
                KEY_WEEKS + " TEXT, " +
                KEY_WEEKDAY + " TEXT, " +
                KEY_TIME + " INTEGER, " +
                KEY_DURATION + " INTEGER, " +
                KEY_PLACE + " TEXT, " +
                KEY_REMIND + " TEXT, " +
                KEY_COLOR + " INTEGER); ";
        db.execSQL(createCourseTable);

        String createBannerTable = "CREATE TABLE IF NOT EXISTS " + TABLE_BANNER +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_URL + " TEXT, " +
                KEY_UPDATE + " TEXT, " +
                KEY_IMG + " TEXT, " +
                KEY_FILENAME + " TEXT, "+
                KEY_NUM + " TEXT); ";

        db.execSQL(createBannerTable);

        String createApartmentTable = "CREATE TABLE IF NOT EXISTS " + TABLE_APARTMENT +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_APART + " TEXT, " +
                KEY_TELE + " TEXT, " +
                KEY_APART_PLACE + " TEXT); ";
        db.execSQL(createApartmentTable);

        String createWebsiteTable = "CREATE TABLE IF NOT EXISTS " + TABLE_WEBSITE +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_SITE + " TEXT, " +
                KEY_SITE_URL + " TEXT); ";
        db.execSQL(createWebsiteTable);

        //        String createLibTable = "create table if not exists " + TABLE_LIB +
        //                " ( " + KEY_ID + " integer primary key autoincrement, " +
        //                 KEY_BOOK_NAME + " text, " +
        //                KEY_SEARCH + " text, " +
        //                KEY_AUTHOR + " text, " +
        //                KEY_BID + " text, " +
        //                KEY_INTRO + " text, " +
        //                KEY_BOOK_ID + " text);";
        //        db.execSQL(createLibTable);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //String dropSearchHistory = "DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY;
        //String dropCourse = " DROP TABLE IF EXISTS " + TABLE_COURSE;
        //String dropBanner = "DROP TABLE IF EXISTS " + TABLE_BANNER;
        //String dropApart = " DROP TABLE IF EXISTS " + TABLE_APARTMENT;
        //String dropSite = " DROP TABLE IF EXISTS " + TABLE_WEBSITE;
        //        String dropLib = "DROP TABLE IF EXISTS " + TABLE_LIB;
        //        db.execSQL(dropSearchHistory);
        //        db.execSQL(dropCourse);
        //        db.execSQL(dropBanner);
        //        db.execSQL(dropApart);
        //        db.execSQL(dropSite);
        //        db.execSQL(dropLib);
        if (newVersion >= 2) {
            String clearAllCourse = "delete from course;";
            db.execSQL(clearAllCourse);
        }

        if (newVersion >= 3) {

            String dropWebsite = "drop table if exists " + TABLE_WEBSITE;
            db.execSQL(dropWebsite);

            String createWebsiteTable = "CREATE TABLE IF NOT EXISTS " + TABLE_WEBSITE +
                    " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_SITE + " TEXT, " +
                    KEY_SITE_URL + " TEXT); ";
            db.execSQL(createWebsiteTable);

        }

        if (newVersion >= 4) {

//            String createTempBannerTable = "alter table TABLE_BANNER rename to _temp_banner";
//
//            String insertData = "insert into TABLE_BANNER select *,'' from _temp_banner";

//            db.execSQL(createTempBannerTable);
//            db.execSQL(insertData);


            String dropBanner = "drop table if exists " + TABLE_BANNER;
            db.execSQL(dropBanner);


            String createBannerTable = "CREATE TABLE IF NOT EXISTS " + TABLE_BANNER +
                    " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_URL + " TEXT, " +
                    KEY_UPDATE + " TEXT, " +
                    KEY_IMG + " TEXT, " +
                    KEY_FILENAME + " TEXT, "+
                    KEY_NUM + " TEXT); ";

            db.execSQL(createBannerTable);

        }

        if (newVersion>=5){
            //埋点统计数据：
            /*
            private String pid;
            private String deviceId;
            private String type;
            private String mainCat;
            private String subCat;
            private String value;
            private String timestamp;
            private String extra;
             */
            db.execSQL("create table if not exists "+"StatisticsData"+
                      "(pid text," +
                      "deviceId text," +
                      "type text," +
                      "mainCat text," +
                      "subCat text," +
                      "value text," +
                      "timestamp text," +
                      "extra text," +
                      "id integer primary key autoincrement);"
            );


        }
        Logger.d("database update");
    }
}

