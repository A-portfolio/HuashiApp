package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.RefreshTableEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.provider.ScheduleWidgetProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.muxi.huashiapp.utils.TimeTableUtil.isContinuOusWeeks;
import static net.muxi.huashiapp.utils.TimeTableUtil.isDoubleWeeks;
import static net.muxi.huashiapp.utils.TimeTableUtil.isSingleWeeks;

/**
 * Created by ybao on 16/5/1.
 */
//添加新课程也是这个Activity
public class CourseEditActivity extends ToolbarActivity {

    private Course mCourse;
    private HuaShiDao dao;

    //是否是新添加课程
    private boolean isAdd = true;

    //上课的周 存储形式为 1,3,4,5,
    private ArrayList<Integer> mWeeks = new ArrayList<>();
    private int mWeekday;
    private int start;
    private int duration;

    private String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private EditText mEtCourse;
    private ImageView mIvWeekSelect;
    private TextView mTvWeek;
    private TextView mEtWeek;
    private ImageView mIvCourseTime;
    private TextView mTvTime;
    private TextView mEtTime;
    private ImageView mIvCoursePlace;
    private EditText mEtPlace;
    private ImageView mIvCourseTeacher;
    private EditText mEtCourseTeacher;
    private TextView mBtnEnsure;

    public static void start(Context context, boolean isAdd, Course course) {
        Intent starter = new Intent(context, CourseEditActivity.class);
        starter.putExtra("is_add", isAdd);
        starter.putExtra("course", course);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        setTitle("");
        isAdd = getIntent().getBooleanExtra("is_add", true);
        if (!isAdd) {
            mCourse = getIntent().getParcelableExtra("course");
            String[] arrays = mCourse.weeks.split(",");
            for (String s : arrays) {
                mWeeks.add(Integer.parseInt(s));
            }
            for (int i = 0; i < 7; i++) {
                if (mCourse.day.equals(Constants.WEEKDAYS_XQ[i])) {
                    mWeekday = i;
                }
            }
            start = mCourse.start;
            duration = mCourse.during;
        } else {
            mCourse = new Course();
            for (int i = 1; i < 19; i++) {
                mWeeks.add(i);
            }
            mWeekday = 0;
            start = 1;
            duration = 2;
        }

        initView();
        dao = new HuaShiDao();
    }

    private void initView() {

        mEtCourse = findViewById(R.id.et_course);
        mIvWeekSelect = findViewById(R.id.iv_week_select);
        mTvWeek = findViewById(R.id.tv_week);
        mEtWeek = findViewById(R.id.et_week);
        mIvCourseTime = findViewById(R.id.iv_course_time);
        mTvTime = findViewById(R.id.tv_time);
        mEtTime = findViewById(R.id.et_time);
        mIvCoursePlace = findViewById(R.id.iv_course_place);
        mEtPlace = findViewById(R.id.et_place);
        mIvCourseTeacher = findViewById(R.id.iv_course_teacher);
        mEtCourseTeacher = findViewById(R.id.et_course_teacher);
        mBtnEnsure = findViewById(R.id.btn_ensure);
        mTvWeek.setOnClickListener(v -> onClick(v));
        mTvTime.setOnClickListener(v -> onClick(v));
        mEtTime.setOnClickListener(v -> onClick(v));
        mEtWeek.setOnClickListener(v -> onClick(v));
        mBtnEnsure.setOnClickListener(v -> onClick(v));

        if (isAdd) {
            mBtnEnsure.setText("添加课程");
            mEtWeek.setText("1-18周");
            mEtTime.setText("周一1-2节");
        } else {
            mBtnEnsure.setText("完成编辑");
            mEtCourse.setText(mCourse.course);
            mEtPlace.setText(mCourse.place);
            mEtWeek.setText(getDisplayWeeks());
            mEtTime.setText(String.format("周%s%d-%d节", Constants.WEEKDAYS[mWeekday], start,
                    start + duration - 1));
            mEtCourseTeacher.setText(mCourse.teacher);
        }
    }

    public void addCourse() {
        CampusFactory.getRetrofitService().addCourse(mCourse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(courseId -> {
                    if (courseId.id != 0) {
                        mCourse.id = String.valueOf(courseId.id);
                        dao.insertCourse(mCourse);
                        showSnackbarShort("添加课程成功");
                        Intent intent = new Intent(this, ScheduleWidgetProvider.class);
                        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                        sendBroadcast(intent);
                        RxBus.getDefault().send(new RefreshTableEvent());
                        finish();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    showErrorSnackbarShort(R.string.tip_school_server_error);
                });
    }

    /**
     * 判断是否和已存在的课程冲突
     *
     * @param newCourse 新添加的课程
     */
    private boolean isConflict(Course newCourse) {
        List<Course> courses = dao.loadCourse(newCourse.day);
        for (int i = 0, size = courses.size(); i < size; i++) {
            Course course = courses.get(i);
            if ((course.start <= newCourse.start &&
                    (course.start + course.during) > newCourse.start)
                    || (course.start >= newCourse.start &&
                    course.start < (newCourse.start + newCourse.during))) {
                String[] localCourse = course.weeks.split(",");
                for (int j = 0; j < localCourse.length; j++) {
                    for (int k = 0; k < mWeeks.size(); k++) {
                        if (localCourse[j].equals(String.valueOf(mWeeks.get(k)))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_week || id == R.id.et_week){
            SelectDialogFragment selectDialogFragment = SelectDialogFragment.newInstance(
                    mWeeks);
            selectDialogFragment.show(getSupportFragmentManager(), "select_weeks");
            selectDialogFragment.setOnPositiveButtonClickListener((weeks, displayWeeks) -> {
                mEtWeek.setText(displayWeeks);
                mWeeks = weeks;
            });
        }else if (id == R.id.tv_time || id == R.id.et_time){
                CourseTimePickerDialogFragment pickerDialogFragment =
                        CourseTimePickerDialogFragment.newInstance(mWeekday, start - 1,
                                start + duration - 2);
                pickerDialogFragment.show(getSupportFragmentManager(), "picker_time");
                pickerDialogFragment.setOnPositiveButtonClickListener((weekday, start1, end) -> {
                    mWeekday = weekday;
                    start = start1;
                    duration = end - start1 + 1;
                    mEtTime.setText(String.format("周%s%d-%d节", Constants.WEEKDAYS[mWeekday], start,
                            start + duration - 1));
                });
        }else if (id == R.id.btn_ensure){
            mCourse.course = mEtCourse.getText().toString();
            mCourse.teacher = mEtCourseTeacher.getText().toString();
            mCourse.place = mEtPlace.getText().toString();
            mCourse.weeks = TextUtils.join(",", mWeeks);
            mCourse.day = Constants.WEEKDAYS_XQ[mWeekday];
            mCourse.start = start;
            mCourse.during = duration;
            mCourse.remind = "false";
            if (TextUtils.isEmpty(mCourse.id)) {
                mCourse.id = generateId();
            }
            if (mCourse.hasNullValue()) {
                showSnackbarShort(R.string.course_complete_course);
                return;
            }
            if (isAdd) {
                MobclickAgent.onEvent(this, "course_add");
                addCourse();
            } else {
                MobclickAgent.onEvent(this, "course_edit");
                updateCourse();
            }
        }
    }

    public String getDisplayWeeks() {
        String s;
        int start;
        int end;
        List<Integer> weekList = mWeeks;
        if (isSingleWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1) + 1;
            s = String.format("%d-%d周单", start, end);
        } else if (isDoubleWeeks(weekList)) {
            start = weekList.get(0) - 1;
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周双", start, end);
        } else if (isContinuOusWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周", start, end);
        } else {
            s = TextUtils.join(",", weekList);
            s += "周";
        }
        return s;
    }

    private String generateId() {
        List<Integer> lists = new ArrayList<>();
        for (Course course : dao.loadAllCourses()) {
            if (Integer.valueOf(course.id) < 1000) {
                lists.add(Integer.valueOf(course.id));
            }
        }
        if (lists.size() == 0) {
            return "1";
        }
        Collections.sort(lists);
        return String.valueOf(lists.get(lists.size() - 1) + 1);
    }

    private void updateCourse() {
        CampusFactory.getRetrofitService().updateCourse(mCourse.id, mCourse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyResponseResponse -> {
                    switch (verifyResponseResponse.code()) {
                        case 200:
                            dao.updateCourse(mCourse);
                            showSnackbarShort("修改课程成功");
                            Intent intent = new Intent(this, ScheduleWidgetProvider.class);
                            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                            sendBroadcast(intent);
                            RxBus.getDefault().send(new RefreshTableEvent());
                            finish();
                            break;
                        case 404:
                            showErrorSnackbarShort(R.string.course_not_found);
                            break;
                        default:
                            showErrorSnackbarShort(R.string.tip_school_server_error);
                    }
                });
    }

}
