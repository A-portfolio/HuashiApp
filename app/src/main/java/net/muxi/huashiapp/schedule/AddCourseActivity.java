package net.muxi.huashiapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/1.
 */
public class AddCourseActivity extends ToolbarActivity
        implements View.OnClickListener, CourseDialogFragment.NoticeDialogListener {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_course_name)
    EditText mEditCourseName;
    @Bind(R.id.edit_teacher_name)
    EditText mEditTeacherName;
    @Bind(R.id.tv_week_select)
    TextView mTvWeekSelect;
    @Bind(R.id.tv_course_time)
    TextView mTvCourseTime;
    @Bind(R.id.edit_course_place)
    EditText mEditCoursePlace;
    @Bind(R.id.tv_course_remind)
    TextView mTvCourseRemind;
    @Bind(R.id.layout_course_add)
    LinearLayout mLayoutCourseAdd;
    @Bind(R.id.btn_add)
    Button mBtnAdd;

    private HuaShiDao dao;
    private PreferenceUtil sp;
    private List<Integer> mWeeks;
    //上课的周 存储形式为 1,3,4,5,
    private int mDay;
    private int courseTime;
    private int duration;
    // TODO: 16/6/21 change the mRemind
    private boolean mRemind = false;


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
        mBtnAdd.setOnClickListener(this);
        mTvWeekSelect.setOnClickListener(this);
        mTvCourseTime.setOnClickListener(this);
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
        if (s.length() > 0){
            s.substring(0,s.length() - 1);
        }
        return s;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_add:
                if (!isEmpty()) {
                    if (NetStatus.isConnected() == true){
                        User user = new User();
                        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
                        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
                        int id = sp.getInt(PreferenceUtil.COURSE_ID,0);
                        Course course = new Course();
//                        course.set
//                        CampusFactory.getRetrofitService().addCourse(Base64Util.createBaseStr(user),)
                    }
                    inserCourse();
                    Intent intent = new Intent();
                    AddCourseActivity.this.setResult(RESULT_OK, intent);
                    this.finish();
                } else {
                    ToastUtil.showLong(App.getContext().getString(R.string.tip_complete_course));
                }
                break;

            case R.id.tv_week_select:
                WeeksDialog weeksDialog = new WeeksDialog(this,
                        mTvWeekSelect.getText().toString(), new WeeksDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(List<Integer> list) {
                        mTvWeekSelect.setText(transList(list));
                        mWeeks.clear();
                        mWeeks.addAll(list);
                    }
                });
                weeksDialog.show();
                break;

            case R.id.tv_course_time:
                String str = mTvCourseTime.getText().toString();
                int weekday = 0;
                int startTime = 0;
                int endTime = 0;
                if (str.equals("请添加上课时间")) {
                } else if (!str.contains("-")) {
                    weekday = getWeekdayValue(str);
                    startTime = getOneTime(str) - 1;
                    endTime = startTime;
                } else if (str != "请添加上课时间") {
                    weekday = getWeekdayValue(str);
                    startTime = getStartTime(str) - 1;
                    endTime = getEndTime(str) - 1;
                }
                CourseDialogFragment dialogFragment = CourseDialogFragment.newInstance(weekday, startTime, endTime);
                dialogFragment.show(getFragmentManager(), "CourseDialogFragment");
                break;

        }

    }

    private void inserCourse() {
        Course course = new Course();
        course.setCourseName(mEditCourseName.getText().toString());
        course.setTeacher(mEditTeacherName.getText().toString());
        course.setWeeks(transToSimpleStr(mWeeks));
        course.setDay(mDay);
        course.setStart(courseTime);
        course.setDuring(duration);
        course.setPlace(mEditCoursePlace.getText().toString());
        dao.insertCourse(course);
    }

    //判断是否有未填充的数据
    private boolean isEmpty() {
        if (mEditCourseName.getText().toString().equals("") &&
                mEditTeacherName.getText().toString().equals("") &&
                mTvWeekSelect.getText().toString().equals("请选择上课周数") &&
                mTvCourseTime.getText().toString().equals("请添加上课时间") &&
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
            case "星期天":
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

        mTvCourseTime.setText(s);
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
                "天"
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
            return new String("请选择上课周数");
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
