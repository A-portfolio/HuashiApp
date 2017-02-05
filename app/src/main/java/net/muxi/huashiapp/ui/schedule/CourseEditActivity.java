package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/1.
 */
public class CourseEditActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.et_course)
    EditText mEtCourse;
    @BindView(R.id.divider1)
    View mDivider1;
    @BindView(R.id.iv_week_select)
    ImageView mIvWeekSelect;
    @BindView(R.id.tv_week)
    TextView mTvWeek;
    @BindView(R.id.et_week)
    TextView mEtWeek;
    @BindView(R.id.divider2)
    View mDivider2;
    @BindView(R.id.iv_course_time)
    ImageView mIvCourseTime;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.et_time)
    TextView mEtTime;
    @BindView(R.id.divider3)
    View mDivider3;
    @BindView(R.id.iv_course_place)
    ImageView mIvCoursePlace;
    @BindView(R.id.et_place)
    EditText mEtPlace;
    @BindView(R.id.divider4)
    View mDivider4;
    @BindView(R.id.iv_course_teacher)
    ImageView mIvCourseTeacher;
    @BindView(R.id.et_teacher)
    EditText mEtTeacher;
    @BindView(R.id.divider5)
    View mDivider5;
    @BindView(R.id.btn_ensure)
    Button mBtnEnsure;

    private Course mCourse;
    private HuaShiDao dao;
    private PreferenceUtil sp;

    //是否是新添加课程
    private boolean isAdd = true;

    //上课的周 存储形式为 1,3,4,5,
    private ArrayList<Integer> mWeeks = new ArrayList<>();
    private int mWeekday;
    private int start;
    private int duration;

    private String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

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
        ButterKnife.bind(this);

        isAdd = getIntent().getBooleanExtra("is_add", true);
        if (!isAdd) {
            mCourse = getIntent().getParcelableExtra("course");
            String[] arrays = mCourse.getWeeks().split(",");
            for (String s : arrays) {
                mWeeks.add(Integer.parseInt(s));
            }
            for (int i = 0; i < 7; i++) {
                if (mCourse.getDay().equals(Constants.WEEKDAYS_XQ[i])) {
                    mWeekday = i;
                }
            }
            start = mCourse.getStart();
            duration = mCourse.getDuring();
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
        //填入数据,debug
        addData();
        dao = new HuaShiDao();
    }

    private void initView() {
        if (isAdd) {
            mBtnEnsure.setText("添加课程");
            mEtWeek.setText("1-18周");
            mEtTime.setText("周一1-2节");
        } else {
            mBtnEnsure.setText("完成编辑");
            mEtCourse.setText(mCourse.getCourse());
            mEtWeek.setText(mCourse.getWeeks());
            mEtTime.setText(String.format("周s%d%-d%节", Constants.WEEKDAYS[mWeekday], start,
                    start + duration - 1));
            mEtTeacher.setText(mCourse.getTeacher());
        }
    }

    private void addData() {
        for (int i = 1; i < 19; i++) {
            mWeeks.add(i);
        }
    }

    public void addCourse(final Course course, final int id) {
        if (NetStatus.isConnected() == true) {
            showProgressBarDialog(true, getString(R.string.tip_adding_course));
            CampusFactory.getRetrofitService().addCourse(Base64Util.createBaseStr(App.sUser), course)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<Response<VerifyResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            ToastUtil.showShort(getString(R.string.tip_adding_fail));
                            showProgressBarDialog(false);
                        }

                        @Override
                        public void onNext(Response<VerifyResponse> verifyResponseResponse) {
                            showProgressBarDialog(false);
                            if (verifyResponseResponse.code() == 201) {
                                ZhugeUtils.sendEvent("课程添加", "成功添加课程");
                                ZhugeUtils.sendEvent("课程提醒状态", course.getRemind());
                                int newId = id;
                                dao.insertCourse(course);
                                Logger.d("add course success");
                                ToastUtil.showShort("添加成功");
                                Intent intent = new Intent();
                                CourseEditActivity.this.setResult(RESULT_OK, intent);
                                //添加的课程 id 自增
                                newId++;
                                sp.saveInt(PreferenceUtil.COURSE_ID, newId);
                                Intent intent1 = new Intent();
                                intent1.setAction("android.intent.action.WidgetProvider");
                                sendBroadcast(intent1);
                                CourseEditActivity.this.finish();
                            } else {
                                ToastUtil.showShort(getString(R.string.tip_adding_fail));
                            }
                        }
                    });
        } else {
            ToastUtil.showLong(getString(R.string.tip_check_net));
        }
    }

    /**
     * 判断是否和已存在的课程冲突
     *
     * @param newCourse 新添加的课程
     */
    private boolean isConflict(Course newCourse) {
        List<Course> courses = dao.loadCourse(newCourse.getDay());
        for (int i = 0, size = courses.size(); i < size; i++) {
            Course course = courses.get(i);
            if ((course.getStart() <= newCourse.getStart() &&
                    (course.getStart() + course.getDuring()) > newCourse.getStart())
                    || (course.getStart() >= newCourse.getStart() &&
                    course.getStart() < (newCourse.getStart() + newCourse.getDuring()))) {
                String[] localCourse = course.getWeeks().split(",");
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

    @OnClick({R.id.tv_week, R.id.et_week, R.id.tv_time, R.id.et_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_week:
            case R.id.et_week:
                SelectDialogFragment selectDialogFragment = SelectDialogFragment.newInstance(
                        mWeeks);
                selectDialogFragment.show(getSupportFragmentManager(), "select_weeks");
                selectDialogFragment.setOnPositiveButtonClickListener((weeks, displayWeeks) -> {
                    mEtWeek.setText(displayWeeks);
                    mWeeks = weeks;
                });
                break;
            case R.id.tv_time:
            case R.id.et_time:
                CourseTimePickerDialogFragment pickerDialogFragment =
                        CourseTimePickerDialogFragment.newInstance(mWeekday, start - 1,
                                start + duration - 2);
                pickerDialogFragment.show(getSupportFragmentManager(), "picker_time");
                pickerDialogFragment.setOnPositiveButtonClickListener((weekday, start1, end) -> {
                    mWeekday = weekday;
                    start = start1 + 1;
                    duration = end - start1 + 1;
                    mEtTime.setText(String.format("周s%d%-d%节", Constants.WEEKDAYS[mWeekday], start,
                            start + duration - 1));
                });
                break;
        }
    }

    @OnClick(R.id.btn_ensure)
    public void onClick() {
//        addCourse(mCourse,);
    }
}
