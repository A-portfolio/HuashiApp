package net.muxi.huashiapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxi.material_dialog.MaterialDialog;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.util.ZhugeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/1.
 */
public class AddCourseActivity extends ToolbarActivity
        implements View.OnClickListener, CourseTimeDialog.NoticeDialogListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_course_name)
    EditText mEditCourseName;
    @BindView(R.id.edit_teacher_name)
    EditText mEditTeacherName;
    @BindView(R.id.edit_course_place)
    EditText mEditCoursePlace;
    @BindView(R.id.tv_course_remind)
    TextView mTvCourseRemind;
    @BindView(R.id.layout_course_remind)
    RelativeLayout mLayoutCourseRemind;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.switch_remind)
    SwitchCompat mSwitchRemind;
    @BindView(R.id.btn_course_week)
    TextView mBtnCourseWeek;
    @BindView(R.id.btn_course_time)
    TextView mBtnCourseTime;

    private HuaShiDao dao;
    private PreferenceUtil sp;
    //上课的周 存储形式为 1,3,4,5,
    private int mDay;
    private List<Integer> mWeeks;
    private int courseTime;
    private int duration;
    // TODO: 16/6/21 change the mRemind
    private boolean mRemind = false;

    private String[] days = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        setTitle("添加课程");
        mWeeks = new ArrayList<>();

        //填入数据,debug
        addData();
        dao = new HuaShiDao();
        sp = new PreferenceUtil();
        mBtnEnter.setOnClickListener(this);
        mBtnCourseWeek.setOnClickListener(this);
        mBtnCourseTime.setOnClickListener(this);
    }

    private void addData() {
        for (int i = 1; i < 19; i++) {
            mWeeks.add(i);
        }
    }


    //将上课的周转为1,2,3,4,5的这种形式
    private String transToSimpleStr(List<Integer> list) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            s += list.get(i) + ",";
        }
        if (s.length() > 0) {
            s.substring(0, s.length() - 1);
        }
        return s;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter:
                Logger.d("btn_enter has clicked");
                if (!isEmpty()) {
                    final Course course = setCourse();
                    final int id = sp.getInt(PreferenceUtil.COURSE_ID, 1);
                    course.setId(id + "");

                    if (isConflict(course)) {
                        final MaterialDialog dialog = new MaterialDialog(AddCourseActivity.this);
                        dialog.setTitle(getResources().getString(R.string.course_conflict_title))
                                .setButtonColor(getResources().getColor(R.color.colorPrimary))
                                .setPositiveButton("添加", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        addCourse(course, id);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.btn_negative), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    } else {
                        addCourse(course, id);
                    }

                } else {
                    ToastUtil.showLong(App.getContext().getString(R.string.tip_complete_course));
                }
                break;

            case R.id.btn_course_week:
                WeeksDialog weeksDialog = new WeeksDialog(this,
                        mBtnCourseWeek.getText().toString(), new WeeksDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(List<Integer> list) {
                        mBtnCourseWeek.setText(transList(list));
                        mWeeks.clear();
                        mWeeks.addAll(list);
                    }
                });
                weeksDialog.show();
                break;

            case R.id.btn_course_time:
                String str = mBtnCourseTime.getText().toString();
                int weekday = 0;
                int startTime = 0;
                int endTime = 0;
                if (str.equals("添加上课时间")) {
                } else if (!str.contains("-")) {
                    weekday = getWeekdayValue(str);
                    startTime = getOneTime(str) - 1;
                    endTime = startTime;
                } else if (str != "添加上课时间") {
                    weekday = getWeekdayValue(str);
                    startTime = getStartTime(str) - 1;
                    endTime = getEndTime(str) - 1;
                }
                final CourseTimeDialog courseTimeDialog = new CourseTimeDialog(this,weekday,startTime,endTime);
                courseTimeDialog.setNoticeDialogListener(this);
                courseTimeDialog.show();
        }
    }

    public void addCourse(final Course course, final int id) {
        if (NetStatus.isConnected() == true) {
            User user = new User();
            user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
            user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
            Logger.d(course.getId() + "");
            showProgressBarDialog(true,getString(R.string.tip_adding_course));
            CampusFactory.getRetrofitService().addCourse(Base64Util.createBaseStr(user), course)
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
                                ZhugeUtils.sendEvent("课程添加","成功添加课程");
                                ZhugeUtils.sendEvent("课程提醒状态",course.getRemind());
                                int newId = id;
                                dao.insertCourse(course);
                                Logger.d("add course success");
                                ToastUtil.showShort("添加成功");
                                Intent intent = new Intent();
                                AddCourseActivity.this.setResult(RESULT_OK, intent);
                                //添加的课程 id 自增
                                newId ++;
                                sp.saveInt(PreferenceUtil.COURSE_ID, newId);
                                Intent intent1 = new Intent();
                                intent1.setAction("android.intent.action.WidgetProvider");
                                sendBroadcast(intent1);
                                AddCourseActivity.this.finish();
                            }else {
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
     * @return
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
                    for (int k = 0;k < mWeeks.size();k ++) {
                        if (localCourse[j].equals(String.valueOf(mWeeks.get(k)))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //插入的课程赋值
    private Course setCourse() {
        Course course = new Course();
        course.setCourse(mEditCourseName.getText().toString());
        course.setTeacher(mEditTeacherName.getText().toString());
        course.setWeeks(transToSimpleStr(mWeeks));
        course.setDay(days[mDay - 1]);
        course.setStart(courseTime);
        course.setDuring(duration);
        course.setPlace(mEditCoursePlace.getText().toString());
        course.setRemind(String.valueOf(mSwitchRemind.isChecked()));
        return course;
    }

    //判断是否有未填充的数据
    private boolean isEmpty() {
        if (mEditCourseName.getText().toString().equals("") ||
                mEditTeacherName.getText().toString().equals("") ||
                mBtnCourseWeek.getText().toString().equals("选择上课周") ||
                mBtnCourseTime.getText().toString().equals("添加上课时间") ||
                mEditCoursePlace.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onDialogPositiveClick(String weekday, int startTime, int endTime) {
        switch (weekday) {
            case "星期一":
                mDay = 1;
                break;
            case "星期二":
                mDay = 2;
                break;
            case "星期三":
                mDay = 3;
                break;
            case "星期四":
                mDay = 4;
                break;
            case "星期五":
                mDay = 5;
                break;
            case "星期六":
                mDay = 6;
                break;
            case "星期日":
                mDay = 7;
                break;
            default:
                mDay = 7;
                break;
        }
        courseTime = startTime + 1;
        duration = endTime - startTime + 1;
        String s = weekday;
        if (startTime == endTime) {
            s += " 第" + (startTime + 1) + "节";
        } else {
            s += "第" + (startTime + 1) + "-" + (endTime + 1) + "节";
        }

        mBtnCourseTime.setText(s);
    }

    @Override
    public void onDialogNegativeClick() {
    }


    //获取星期的 value
    private int getWeekdayValue(String str) {
        String s = str.substring(2, 3);
        String[] weekdays = new String[]{
                "一",
                "二",
                "三",
                "四",
                "五",
                "六",
                "日"
        };
        for (int i = 0; i < 7; i++) {
            if (s.equals(weekdays[i])) {
                return i;
            }
        }
        return -1;
    }


    public String transList(List<Integer> list) {
        if (list.size() == 0) {
            return new String("选择上课周");
        }
        String s = "";
        if (list.size() == 1) {
            s = list.get(0) + "周";
            return s;
        }
        if (isSingleWeeks(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周(单)";
            return s;
        }
        if (isDoubleWeeks(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周(双)";
            return s;
        }
        if (isContinuous(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周";
            return s;
        }
        int n = list.get(0);
        int j = 1;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) - n == 1) {
                j++;
                n++;
            } else {
                s += (j > 1) ? (n - j + 1 + "-" + n + ",") : n + ",";
                n = list.get(i);
                j = 1;
            }
        }
        if (j != 1) {
            s += (n - j + 1 + "-" + n + "周");
        } else if (j == 1) {
            s += n + "周";
        }
        return s;
    }


    private boolean isContinuous(List<Integer> list) {
        if (list.get(list.size() - 1) - list.get(0) == list.size() - 1) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isDoubleWeeks(List<Integer> list) {
        boolean b = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 != 0) {
                b = false;
                break;
            }
        }
        if (list.get(list.size() - 1) - list.get(0) != 2 * (list.size() - 1)) {
            b = false;
        }
        return b;
    }

    private boolean isSingleWeeks(List<Integer> list) {
        boolean b = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 != 1) {
                b = false;
                break;
            }
        }
        if (list.get(list.size() - 1) - list.get(0) != 2 * (list.size() - 1)) {
            b = false;
        }
        return b;
    }


    //获取只有一小节课程的时间
    private int getOneTime(String str) {
        int start = str.indexOf("第");
        int end = str.indexOf("节");
        return Integer.valueOf(str.substring(start + 1, end));
    }


    //获取开始上课节次的value
    private int getStartTime(String str) {
        int start = str.indexOf("第");
        int end = str.indexOf("-");
        int startTime = Integer.valueOf(str.substring(start + 1, end));
        return startTime;
    }


    //获取结束上课的节次 value
    private int getEndTime(String str) {
        int start = str.indexOf("-");
        int end = str.indexOf("节");
        int endTime = Integer.valueOf(str.substring(start + 1, end));
        return endTime;
    }


}
