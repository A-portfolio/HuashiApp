package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.TimeTableUtil;

import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/11/2.
 */

public class AppWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String INTENT_COURSES = "courses";
    //存放当天要显示的课程
    private List<Course> mCourseList;

    private List<Course> mAllCourseList;
    private Context mContext;

    public AppWidgetFactory(Context context, Intent intent) {
        mContext = context;
        mAllCourseList = intent.getParcelableArrayListExtra("course");
        setCourseListByStartTime();

        Logger.d("app widget factory ");
        Logger.d(mCourseList.size() + "");
    }

    private void setCourseListByStartTime() {
        Observable.from(TimeTableUtil.getTodayCourse(mAllCourseList))
            .toSortedList(new Func2<Course, Course, Integer>() {
                @Override public Integer call(Course course, Course course2) {
                    return ((Integer)course.getStart()).compareTo(course2.getStart());
                }
            })
            .observeOn(Schedulers.immediate())
            .subscribeOn(Schedulers.immediate())
            .subscribe(new Action1<List<Course>>() {
                @Override public void call(List<Course> courses) {
                    mCourseList = courses;
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
    }

    @Override
    public void onCreate() {
//        HuaShiDao dao = new HuaShiDao();
//        List<Course> allCourses = dao.loadAllCourses();
//        mCourseList =  TimeTableUtil.getTodayCourse(allCourses);

    }

    @Override
    public void onDataSetChanged() {
        Logger.d("data set change");
        HuaShiDao dao = new HuaShiDao();
        mAllCourseList = dao.loadAllCourses();
//        mCourseList.clear();
      setCourseListByStartTime();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCourseList == null) {
            return 0;
        }
        Logger.d(mCourseList.size() + "");
        return mCourseList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i >= mCourseList.size()){
            return null;
        }
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_course);
        rv.setTextViewText(R.id.tv_course, mCourseList.get(i).getCourse());
        rv.setTextViewText(R.id.tv_place, mCourseList.get(i).getPlace());
        rv.setTextViewText(R.id.tv_teacher, mCourseList.get(i).getTeacher());
        int start = mCourseList.get(i).getStart();
        rv.setTextViewText(R.id.tv_start, TimeTableUtil.getCourseTime(start, true));
        int end = mCourseList.get(i).getStart() + mCourseList.get(i).getDuring() - 1;
        rv.setTextViewText(R.id.tv_end, TimeTableUtil.getCourseTime(end, false));
        Logger.d("get remoteview");
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
