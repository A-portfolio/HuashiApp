package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.TimeTableUtil;

import java.util.List;

/**
 * Created by ybao on 16/7/27.
 * 点击查看当前位置的所有课程的 view
 */
public class CoursesView extends HorizontalScrollView{

    private List<Course> mCourses;
    private Context mContext;
    //内容 layout
    private LinearLayout mContentLayout;
    private TextView[] courseTvs;
    private int mWeek;
    private static final int COURSE_WIDTH = DimensUtil.dp2px(68);
    private static final int COURSE_HEIGHT = DimensUtil.dp2px(128);
    private static final int COURSE_MARGIN = DimensUtil.dp2px(8);

    public CoursesView(Context context,List<Course> courses,int week) {
        super(context);
        mContext = context;
        mCourses = courses;
        mWeek = week;
        courseTvs = new TextView[courses.size()];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            this.setElevation(DimensUtil.dp2px(8));
        }
        addLinearLayout();
        initView();
    }

    private void addLinearLayout() {
        mContentLayout = new LinearLayout(mContext);
        mContentLayout.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        this.addView(mContentLayout,params);
    }

    private void initView() {
        for (int i = 0;i < mCourses.size(); i++){
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    COURSE_WIDTH,
                    COURSE_HEIGHT
            );
            params.setMargins(COURSE_MARGIN,0,COURSE_MARGIN,0);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            String courseName = TimeTableUtil.simplifyCourse(mCourses.get(i).getCourse());
            if (TimeTableUtil.isThisWeek(mWeek,mCourses.get(i).getWeeks())) {
                textView.setText(courseName + "\n@" + mCourses.get(i).getPlace() +
                "\n" + mCourses.get(i).getTeacher());
            }else {
                textView.setText(courseName + "\n@" + mCourses.get(i).getPlace() +
                        "\n" + mCourses.get(i).getTeacher() + "[非本周]");
            }
            textView.setLayoutParams(params);
            if (TimeTableUtil.isThisWeek(mWeek,mCourses.get(i).getWeeks())){
                textView.setBackground(getResources().getDrawable(TimeTableUtil.getCourseBg(mCourses.get(i).getColor(),0)));
            }else {
                textView.setBackground(getResources().getDrawable(R.drawable.bg_simple_class_gray));
            }
            mContentLayout.addView(textView);
            mContentLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseActivity)mContext).onBackPressed();
                }
            });
        }

    }


}
