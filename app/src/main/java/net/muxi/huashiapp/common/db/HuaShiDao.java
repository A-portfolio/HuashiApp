package net.muxi.huashiapp.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.muxi.huashiapp.common.data.ApartmentData;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.WebsiteData;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ybao on 16/5/12.
 */
public class HuaShiDao {

    private SQLiteDatabase db;

    public HuaShiDao() {
        db = DataBase.getInstance();
    }

    //插入当前用户的搜索记录,如果当前用户没有登录则为 null
    public void insertSearchHistory(String book) {
        String libraryId = PreferenceUtil.getString(PreferenceUtil.LIBRARY_ID, "0");
        db.execSQL("INSERT INTO " + DataBase.TABLE_SEARCH_HISTORY +
                        " VALUES(NULL,?,? )",
                new String[]{libraryId, book});

    }

    public List<String> loadSearchHistory() {
        String libraryId = PreferenceUtil.getString(PreferenceUtil.LIBRARY_ID, "0");
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
        if (cursor != null) {
            cursor.close();
        }
        return records;
    }

    public void deleteAllHistory() {
        String libraryId = PreferenceUtil.getString(PreferenceUtil.LIBRARY_ID);
        db.execSQL("DELETE FROM " + DataBase.TABLE_SEARCH_HISTORY +
                " WHERE " + DataBase.KEY_LIBRARY_USER_ID + " = ? ",
                new String[]{libraryId});
    }

    public void insertCourse(Course course) {
        db.execSQL("INSERT INTO " + DataBase.TABLE_COURSE + " values (?,?,?,?,? ," + course.start + "," + course.during + ",?,?,?) ",
                new String[]{
                        course.id,
                        course.course,
                        course.teacher,
                        course.weeks,
                        course.day,
                        course.place,
                        String.valueOf(course.remind),
                        String.valueOf(course.color)
                });
    }

    public void updateCourse(Course course) {
        deleteCourse(course.id);
        insertCourse(course);
    }

    /**
     * 获取指定星期的课程
     *
     * @param weekday 星期
     * @return
     */
    public List<Course> loadCourse(String weekday) {
        Cursor cursor =
                db.rawQuery("SELECT * FROM " + DataBase.TABLE_COURSE +
                                " WHERE " + DataBase.KEY_WEEKDAY + " = ? ",
                        new String[]{
                                weekday
                        });
        List<Course> courses = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.id = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_ID)));
                course.course = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_COURSE_NAME)));
                course.teacher = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_TEACHER)));
                course.weeks = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKS)));
                course.day = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKDAY)));
                course.start = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_TIME)));
                course.during = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_DURATION)));
                course.place = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_PLACE)));
                course.remind = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_REMIND)));
                course.color = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_COLOR)));
                courses.add(course);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return courses;
    }

    public List<Course> loadAllCourses() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_COURSE, null);
        List<Course> courses = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.id = cursor.getString(cursor.getColumnIndex(DataBase.KEY_ID));
                course.course = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_COURSE_NAME)));
                course.teacher = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_TEACHER)));
                course.weeks = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKS)));
                course.day = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_WEEKDAY)));
                course.start = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_TIME)));
                course.during = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_DURATION)));
                course.place = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_PLACE)));
                course.remind = (cursor.getString(cursor.getColumnIndex(DataBase.KEY_REMIND)));
                course.color = (cursor.getInt(cursor.getColumnIndex(DataBase.KEY_COLOR)));
                courses.add(course);
            }
        }
        if (cursor != null) {
            cursor.close();
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

    public List<BannerData> loadBannerData() {
        List<BannerData> bannerDatas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_BANNER + " ", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                BannerData bannerData = new BannerData();
                bannerData.setUrl(cursor.getString(cursor.getColumnIndex(DataBase.KEY_URL)));
                bannerData.setImg(cursor.getString(cursor.getColumnIndex(DataBase.KEY_IMG)));
                bannerData.setFilename(cursor.getString(cursor.getColumnIndex(DataBase.KEY_FILENAME)));
                bannerData.setUpdate(Long.parseLong(cursor.getString(cursor.getColumnIndex(DataBase.KEY_UPDATE))));
                bannerData.setNum(cursor.getString(cursor.getColumnIndex(DataBase.KEY_NUM)));
                bannerDatas.add(bannerData);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return bannerDatas;
    }

    public void insertBannerData(BannerData bannerData) {
        db.execSQL("INSERT INTO " + DataBase.TABLE_BANNER + " VALUES(null,?,?,?,?,?)",
                new String[]{
                        bannerData.getUrl(),
                        String.valueOf(bannerData.getUpdate()),
                        bannerData.getImg(),
                        bannerData.getFilename(),
                        bannerData.getNum()
                });
    }

    public void deleteAllBannerData() {
        db.execSQL("DELETE FROM " + DataBase.TABLE_BANNER + ";");
    }

    public void insertApart(ApartmentData data) {
        String phone = new String();
        for (String phoneStr : data.getPhone()) {
            phone = phone + phoneStr + " ";
        }
        db.execSQL("insert into " + DataBase.TABLE_APARTMENT + " values(null,?,?,?) ",
                new String[]{
                        data.getApartment(),
                        phone,
                        data.getPlace()
                });
    }

    public List<ApartmentData> loadApart() {
        List<ApartmentData> apartmentDatas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_APARTMENT + " ", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ApartmentData data = new ApartmentData();
                data.setApartment(cursor.getString(cursor.getColumnIndex(DataBase.KEY_APART)));
                List<String> phones = getPhoneList(cursor.getString(cursor.getColumnIndex(DataBase.KEY_TELE)));
                data.setPhone(phones);
                data.setPlace(cursor.getString(cursor.getColumnIndex(DataBase.KEY_APART_PLACE)));
                apartmentDatas.add(data);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return apartmentDatas;
    }

    public void deleteApartData() {
        db.execSQL("delete from " + DataBase.TABLE_APARTMENT + ";");
    }


    /**
     * 解析电话字符串
     *
     * @param phone
     * @return
     */
    public List<String> getPhoneList(String phone) {
        List<String> list = Arrays.asList(phone.split(" "));
        return list;
    }

    public void insertSite(WebsiteData data) {
        db.execSQL("insert into " + DataBase.TABLE_WEBSITE + " values(null,?,?) ",
                new String[]{
                        data.getSite(),
                        data.getUrl()
                });

    }

    public List<WebsiteData> loadSite() {
        List<WebsiteData> websiteDatas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.TABLE_WEBSITE, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                WebsiteData data = new WebsiteData();
                data.setSite(cursor.getString(cursor.getColumnIndex(DataBase.KEY_SITE)));
                data.setUrl(cursor.getString(cursor.getColumnIndex(DataBase.KEY_SITE_URL)));
                websiteDatas.add(data);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return websiteDatas;
    }

    public void deleteWebsite() {
        db.execSQL("delete from " + DataBase.TABLE_WEBSITE + ";");
    }


}
