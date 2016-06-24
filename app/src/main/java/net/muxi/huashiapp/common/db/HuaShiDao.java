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
        db = DateBase.getInstance();
        sp = new PreferenceUtil();
    }


    //插入当前用户的搜索记录,如果当前用户没有登录则为 null
    public void insertSearchHistory(String book) {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        db.execSQL("INSERT INTO " + DateBase.TABLE_SEARCH_HISTORY +
                        " VALUES(NULL,?,? )",
                new String[]{libraryId, book});

    }


    public List<String> loadSearchHistory() {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        Log.d("tag", libraryId);
        List<String> records = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + DateBase.TABLE_SEARCH_HISTORY +
                        " WHERE " + DateBase.KEY_LIBRARY_USER_ID + " = ? " +
                        "ORDER BY " + DateBase.KEY_ID + " DESC",
                new String[]{libraryId});
        if (cursor.getCount() > 0) {
            int i = 0;
            while (cursor.moveToNext()) {
//                records[i] = cursor.getString(cursor.getColumnIndex(DateBase.KEY_BOOK));
//                Log.d("cursor",records[i]);
                records.add(cursor.getString(cursor.getColumnIndex(DateBase.KEY_BOOK)));
                i++;
            }
        }
        cursor.close();
        return records;
    }


    public void deleteAllHistory() {
        String libraryId = sp.getString(PreferenceUtil.LIBRARY_ID);
        db.execSQL("DELETE * FROM " + DateBase.TABLE_SEARCH_HISTORY +
                " WHERE " + DateBase.KEY_LIBRARY_USER_ID + " = ? " +
                new String[]{libraryId});
    }

    //添加课程
//    public void insertCourse(String courseName, String teacher, String weeks, int day, int time, int duration, String place, String remind) {
//        String userId = sp.getString(PreferenceUtil.STUDENT_ID, "0");
//        db.execSQL("INSERT INTO " + DateBase.TABLE_COURSE + " values (null,?,?,?,?, " + day + "," + time + "," + duration + ",?,?) ",
//                new String[]{
//                        userId,
//                        courseName,
//                        teacher,
//                        weeks,
//                        place,
//                        remind
//                });
//        Log.d("insert", courseName);
//    }
    public void insertCourse(Course course) {
        String userId = sp.getString(PreferenceUtil.STUDENT_ID, "0");
        db.execSQL("INSERT INTO " + DateBase.TABLE_COURSE + " values (null,?,?,?,?, " + course.getDay() + "," + course.getStart() + "," + course.getDuring() + ",?,?) ",
                new String[]{
                        userId,
                        course.getCourseName(),
                        course.getTeacher(),
                        course.getWeeks(),
                        course.getPlace(),
                        course.getRemind()
                });
    }

    //获取指定周的课程
    public List<Course> loadCourse(String weeks) {
        String userId = sp.getString(PreferenceUtil.STUDENT_ID);
        Cursor cursor =
                db.rawQuery("SELECT * FROM " +
                                DateBase.TABLE_COURSE + " WHERE " +
                                DateBase.KEY_USER_ID + " =?",
                        new String[]{
                                userId,
                        });

        List<Course> courses = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DateBase.KEY_WEEKS)).contains(weeks)) {
                    Course course = new Course();
                    course.setCourseName(cursor.getString(cursor.getColumnIndex(DateBase.KEY_COURSE_NAME)));
                    course.setTeacher(cursor.getString(cursor.getColumnIndex(DateBase.KEY_TEACHER)));
                    course.setWeeks(cursor.getString(cursor.getColumnIndex(DateBase.KEY_WEEKS)));
                    course.setDay(cursor.getInt(cursor.getColumnIndex(DateBase.KEY_WEEKDAY)));
                    course.setStart(cursor.getInt(cursor.getColumnIndex(DateBase.KEY_TIME)));
                    course.setDuring(cursor.getInt(cursor.getColumnIndex(DateBase.KEY_DURATION)));
                    course.setPlace(cursor.getString(cursor.getColumnIndex(DateBase.KEY_PLACE)));
                    course.setRemind(cursor.getString(cursor.getColumnIndex(DateBase.KEY_REMIND)));
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    //删除指定的课程
    public void deleteCourse(String courseName, String teacher) {
        String userId = sp.getString(PreferenceUtil.STUDENT_ID);
        db.execSQL("DELETE * FROM " + DateBase.TABLE_COURSE +
                        " WHERE " + DateBase.KEY_COURSE_NAME + " = ? and" +
                        " WHERE " + DateBase.KEY_TEACHER + " =? ",
                new String[]{
                        courseName,
                        teacher
                });
    }

    //删除所有的课程
    public void deleteAllCourse() {
        String userId = sp.getString(PreferenceUtil.STUDENT_ID);
        db.execSQL("DELETE * FROM " + DateBase.TABLE_COURSE +
                        " WHERE " + DateBase.KEY_USER_ID + " =? ",
                new String[]{
                        userId
                });
    }


}
