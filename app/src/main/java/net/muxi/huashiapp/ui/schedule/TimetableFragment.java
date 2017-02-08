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
    @BindView(R.id.table_menu_view)
    TableMenuView mTableMenuView;


    /**
     * 本学期所有的课程
     */
    private List<Course> mCourses;

    //false表示初始状态
    private boolean selectedIvStatus = false;
    private int selectedWeek;
    private int curWeek;

    private HuaShiDao dao;

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
        dao = new HuaShiDao();

        initData();
        initView();
        initListener();

        return view;
    }

    private void initData() {
        curWeek = TimeTableUtil.getCurWeek();
        selectedWeek = curWeek;
        mCourses = dao.loadAllCourses();
    }

    private void initView() {
        if (mCourses.size() == 0) {
            loadTable();
        } else {
            renderCourseView(mCourses);
        }

        Logger.d(TimeTableUtil.getCurWeek() + "");
        renderCurweekView(TimeTableUtil.getCurWeek());
        renderSelectedWeekView(TimeTableUtil.getCurWeek());
    }

    private void initListener() {
        mWeekSelectedView.setOnWeekSelectedListener(week -> {
            rotateSelectView();
            selectedWeek = week;
            renderCourseView(mCourses);
            renderSelectedWeekView(week);
        });
        mIvMenu.setOnClickListener(v -> {
            mTableMenuView.show();
            mTableMenuView.setCurweek(curWeek);
        });
        mTimetable.getTableContent().setOnCourseClickListener(course -> {

        });
    }

    public void loadTable() {
        CampusFactory.getRetrofitService().getSchedule(Base64Util.createBaseStr(App.sUser),
                App.sUser.sid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(courseList -> {
                    courseList.remove(0);
                    updateDB(courseList);
                    mCourses = courseList;
                    renderCourseView(courseList);
                }, throwable -> throwable.printStackTrace());
    }

    /**
     * 渲染显示的课程： 更改日期，显示课程
     */
    private void renderCourseView(List<Course> courseList) {
        mTimetable.getWeekLayout().setWeekDate(selectedWeek - curWeek);
        mTimetable.getTableContent().updateCourses(courseList, selectedWeek);
    }

    private void updateDB(List<Course> courseList) {
        dao.deleteAllCourse();
        for (Course course : courseList) {
            dao.insertCourse(course);
        }
    }

    private void rotateSelectView() {
        if (!selectedIvStatus) {
            mIvSelectWeek.setRotation(180);
        }else {
            mIvSelectWeek.setRotation(0);
        }
        selectedIvStatus = !selectedIvStatus;
    }

//    private List<Course> getCourseListInSelectedWeek(int week) {
//        List<Course> courseList = new ArrayList<>();
//        for (int i = 0; i < mCourses.size(); i++) {
//            if (TimeTableUtil.isThisWeek(week + 1, mCourses.get(i).getWeeks())) {
//                courseList.add(mCourses.get(i));
//            }
//        }
//        int thisWeekCourseSize = courseList.size();
//        for (int i = 0; i < mCourses.size(); i++) {
//            boolean flag = false;
//            if (!TimeTableUtil.isThisWeek(week + 1, mCourses.get(i).getWeeks())) {
//                for (int j = 0; j < thisWeekCourseSize; j++) {
//                    if (mCourses.get(i).getDay().equals(courseList.get(j).getDay()) &&
//                            mCourses.get(i).getStart() == courseList.get(j).getStart() &&
//                            mCourses.get(i).getDuring() == courseList.get(j).getDuring()) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    courseList.add(mCourses.get(i));
//                }
//            }
//        }
//        return courseList;
//    }

    /**
     * @param week 从1开始计数
     */
    public void renderSelectedWeekView(int week) {
        mTvSelectWeek.setText(String.format("第%d周", week));
    }

    /**
     * @param week 从1开始计数
     */
    public void renderCurweekView(int week) {
        mTvCurrentWeek.setText("当前周设置为" + week);
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
                if (!selectedIvStatus) {
                    mWeekSelectedView.slideDown();
                    mWeekSelectedView.setSelectedWeek(selectedWeek);
                } else {
                    mWeekSelectedView.slideUp();
                }
                rotateSelectView();
                break;
        }
    }
}
