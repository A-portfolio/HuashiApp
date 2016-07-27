package net.muxi.huashiapp.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/5/12.
 */
public class DataBase extends SQLiteOpenHelper{

    private static DataBase instance;

    private static final String DB_NAME = "huashi_db";
    private static final int DB_VERSION = 1;

    //图书馆搜索的历史记录
    public static final String TABLE_SEARCH_HISTORY = "search_history";
    //课程的本地数据库
    public static final String TABLE_COURSE = "course";
    public static final String TABLE_BANNER = "banner";

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

    //banner的属性
    public static final String KEY_URL = "url";
    public static final String KEY_UPDATE = "update_time";
    public static final String KEY_IMG = "img";
    public static final String KEY_FILENAME = "filename";


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public static SQLiteDatabase getInstance(){
        if (instance == null){
            instance = new DataBase(App.getContext(),DB_NAME,null,DB_VERSION);
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
                KEY_REMIND + " TEXT); ";
        db.execSQL(createCourseTable);

        String createBannerTable = "CREATE TABLE IF NOT EXISTS " + TABLE_BANNER +
                " ( "  + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_URL + " TEXT, " +
                KEY_UPDATE + " TEXT, " +
                KEY_IMG + " TEXT, " +
                KEY_FILENAME + " TEXT); ";
        db.execSQL(createBannerTable);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropSearchHistory = "DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY;
        String dropCourse = " DROP TABLE IF EXISTS " + TABLE_COURSE;
        String dropBanner = "DROP TABLE IF EXISTS " + TABLE_BANNER;
        db.execSQL(dropSearchHistory);
        db.execSQL(dropCourse);
        db.execSQL(dropBanner);
    }
}

