package net.muxi.huashiapp.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/5/12.
 */
public class HuaShiDao {

    private SQLiteDatabase db;
    private PreferenceUtil sp;

    public HuaShiDao() {
        db = DataBase.getInstance();
        sp = new PreferenceUtil();
    }


    //插入当前用户的搜索记录,如果当前用户没有登录则为 null
    public void insertSearchHistory(String book) {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        db.execSQL("INSERT INTO " + DataBase.TABLE_SEARCH_HISTORY +
                        " VALUES(NULL,?,? )",
                new String[]{libraryId, book});

    }


    public List<String> loadSearchHistory() {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        Log.d("tag", libraryId);
        List<String> records = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_SEARCH_HISTORY +
                        " WHERE " + DataBase.KEY_LIBRARY_USER_ID + " = ? " +
                        "ORDER BY " + DataBase.KEY_ID + " DESC",
                new String[]{libraryId});
        if (cursor.getCount() > 0) {
            int i = 0;
            while (cursor.moveToNext()) {
                records.add(cursor.getString(cursor.getColumnIndex(DataBase.KEY_BOOK)));
                i++;
            }
        }
        cursor.close();
        return records;
    }


    public void deleteAllHistory() {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        db.execSQL("DELETE * FROM " + DataBase.TABLE_SEARCH_HISTORY +
                " WHERE " + DataBase.KEY_LIBRARY_USER_ID + " = ? " +
                new String[]{libraryId});
    }

    public void insertCourse(Course course) {
        db.execSQL("INSERT INTO " + DataBase.TABLE_COURSE + " values (null,?,?,?,? ," + course.getStart() + "," + course.getDuring() + ",?,?) ",
                new String[]{
                        course.getCourse(),
                        course.getTeacher(),
                        course.getWeeks(),
                        course.getDay(),
                        course.getPlace(),
                        course.getRemind().toString()
                });
    }

    //获取指定周的课程
    public List<Course> loadCourse(String weeks) {
        String userId = sp.getString(PreferenceUtil.STUDENT_ID);
        Cursor cursor =
                db.rawQuery("SELECT * FROM " + DataBase.TABLE_COURSE,
                        null);
        List<Course> courses = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKS)).contains(weeks)) {
                    Course course = new Course();
                    course.setCourse(cursor.getString(cursor.getColumnIndex(DataBase.KEY_COURSE_NAME)));
                    course.setTeacher(cursor.getString(cursor.getColumnIndex(DataBase.KEY_TEACHER)));
                    course.setWeeks(cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKS)));
                    course.setDay(cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKDAY)));
                    course.setStart(cursor.getInt(cursor.getColumnIndex(DataBase.KEY_TIME)));
                    course.setDuring(cursor.getInt(cursor.getColumnIndex(DataBase.KEY_DURATION)));
                    course.setPlace(cursor.getString(cursor.getColumnIndex(DataBase.KEY_PLACE)));
                    course.setRemind(cursor.getString(cursor.getColumnIndex(DataBase.KEY_REMIND)));
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    //删除指定的课程
    public void deleteCourse(String id) {
        db.execSQL("DELETE FROM " + DataBase.TABLE_COURSE +
                        " WHERE " + DataBase.KEY_ID + " = ? ",
                new String[]{
                        id
                });
    }

    //删除所有的课程
    public void deleteAllCourse() {
        db.execSQL("DELETE FROM " + DataBase.TABLE_COURSE + ";");
    }


}
