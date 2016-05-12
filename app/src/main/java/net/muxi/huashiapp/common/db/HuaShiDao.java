package net.muxi.huashiapp.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.muxi.huashiapp.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/5/12.
 */
public class HuaShiDao {

    private SQLiteDatabase db;


    public HuaShiDao() {
        db = DateBase.getInstance();
    }


    //插入当前用户的搜索记录,如果当前用户没有登录则为 null
    public void insertSearchHistory(String book) {
        String libraryId = App.sLibrarayUser.getLibraryId();
        db.execSQL("INSERT INTO " + DateBase.TABLE_SEARCH_HISTORY +
                        " VALUES(NULL,?,? )",
                new String[]{libraryId, book});

    }


    public List<String> loadSearchHistory() {
        String libraryId = App.sLibrarayUser.getLibraryId();
        List<String> list = new ArrayList<String>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + DateBase.TABLE_SEARCH_HISTORY +
                        "WHERE" + DateBase.KEY_LIBRARY_USER_ID + " = ? " +
                        "ORDER BY " + DateBase.KEY_ID + "DESC",
                new String[]{libraryId});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(DateBase.KEY_BOOK)));
            }
            cursor.close();
        }
        return list;
    }


    public void deleteAllHistory() {
        String libraryId = App.sLibrarayUser.getLibraryId();
        db.execSQL("DELETE * FROM " + DateBase.TABLE_SEARCH_HISTORY +
                " WHERE " + DateBase.KEY_LIBRARY_USER_ID + " = ? " +
                new String[]{libraryId});
    }


}
