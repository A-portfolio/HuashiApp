package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.TimeTableUtil;
import net.muxi.huashiapp.widget.TimeTable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/1/25.
 */

public class TimetableFragment extends BaseFragment {

    @BindView(R.id.timetable)
    TimeTable mTimetable;
    @BindView(R.id.week_selected_view)
    WeekSelectedView mWeekSelectedView;
    @BindView(R.id.tv_select_week)
    TextView mTvSelectWeek;
    @BindView(R.id.iv_select_week)
    ImageView mIvSelectWeek;
    @BindView(R.id.tv_current_week)
    TextView mTvCurrentWeek;
    @BindView(R.id.iv_menu)
    ImageButton mIvMenu;
    @BindView(R.id.tool_layout)
    RelativeLayout mToolLayout;
    /**
     * 本学期所有的课程
     */
    private List<Course> mCourses;

    private int selectedWeek;
    private int curWeek;

    public static TimetableFragment newInstance() {
        Bundle args = new Bundle();
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Logger.d("createview");
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, view);
        mWeekSelectedView.setOnWeekSelectedListener(week -> {
//            renderCourseView();
        });
        mIvMenu.setOnClickListener(v -> {

        });
        return view;
    }

    public void loadTable() {
        CampusFactory.getRetrofitService().getSchedule(Base64Util.createBaseStr(App.sUser),
                App.sUser.sid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(courseList -> {
                    if (isDiff(courseList)) {
                        updateDB(courseList);
                        renderCourseView(courseList);
                    }
                }, throwable -> throwable.printStackTrace());
    }

    private void renderCourseView(List<Course> courseList) {

    }

    private void updateDB(List<Course> courseList) {

    }

    private List<Course> getCourseListInSelectedWeek(int week) {
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < mCourses.size(); i++) {
            if (TimeTableUtil.isThisWeek(week + 1, mCourses.get(i).getWeeks())) {
                courseList.add(mCourses.get(i));
            }
        }
        int thisWeekCourseSize = courseList.size();
        for (int i = 0; i < mCourses.size(); i++) {
            boolean flag = false;
            if (!TimeTableUtil.isThisWeek(week + 1, mCourses.get(i).getWeeks())) {
                for (int j = 0;j < thisWeekCourseSize;j ++){
                   if (mCourses.get(i).getDay().equals(courseList.get(j).getDay()) &&
                   mCourses.get(i).getStart() == courseList.get(j).getStart() &&
                   mCourses.get(i).getDuring() == courseList.get(j).getDuring()){
                       flag = true;
                       break;
                   }
                }
                if (!flag){
                    courseList.add(mCourses.get(i));
                }
            }
        }
        return courseList;
    }

    /**
     * 比对服务器和本地数据库的课程列表差异
     */
    private boolean isDiff(List<Course> courseList) {

        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @OnClick({R.id.tv_select_week, R.id.iv_select_week})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_week:
            case R.id.iv_select_week:
                mWeekSelectedView.slideDown();
                mWeekSelectedView.setSelectedWeek(selectedWeek);
                break;
        }
    }
}
