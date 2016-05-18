package net.muxi.huashiapp.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/5/12.
 */
public class DateBase extends SQLiteOpenHelper{

    private static DateBase instance;

    private static final String DB_NAME = "huashi_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_SEARCH_HISTORY = "search_history";
    public static final String TABLE_COURSE = "course";
    public static final String TABLE_SCORE = "score";
    public static final String TABLE_INFORMATION = "info";

    public static final String KEY_ID = "id";

    public static final String KEY_BOOK = "book";
    public static final String KEY_LIBRARY_USER_ID = "library_id";

    public DateBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public static SQLiteDatabase getInstance(){
        if (instance == null){
            instance = new DateBase(App.getContext(),DB_NAME,null,DB_VERSION);
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

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

