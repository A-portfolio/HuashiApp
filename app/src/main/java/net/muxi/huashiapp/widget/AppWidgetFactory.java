package net.muxi.huashiapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.db.HuaShiDao;
import net.muxi.huashiapp.utils.TimeTableUtil;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.main.MainActivity;

import java.util.List;

/**
 * Created by ybao on 16/11/2.
 */

public class AppWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String INTENT_COURSES = "courses";
    //存放当天要显示的课程
    private List<Course> mCourseList;

    private Context mContext;

    public AppWidgetFactory(Context context, Intent intent) {
        mContext = context;
        loadTodayCourses();
    }

    @Override
    public void onCreate() {
        loadTodayCourses();
    }

    @Override
    public void onDataSetChanged() {
        loadTodayCourses();
    }

    public void loadTodayCourses(){
        HuaShiDao dao = new HuaShiDao();
        List<Course> allCourses = dao.loadAllCourses();
        mCourseList = TimeTableUtil.getTodayCourse(allCourses);
        Logger.d(mCourseList.size() + " today couse size");
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
        if (getCount() == 0) {
            return null;
        }
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_course);
        rv.setTextViewText(R.id.tv_course, mCourseList.get(i).getCourse());
        rv.setTextViewText(R.id.tv_place, mCourseList.get(i).getPlace());
        rv.setTextViewText(R.id.tv_time, String.format("%s上课",
                TimeTableUtil.getCourseTime(mCourseList.get(i).getStart(), true)));

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("ui","table");
        rv.setOnClickFillInIntent(R.id.iv_detail,intent);
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
