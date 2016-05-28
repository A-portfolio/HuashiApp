package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/1.
 */
public class AddCourseFragment extends BaseFragment implements View.OnClickListener {

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
    @Bind(R.id.layout_course_add)
    LinearLayout mLayoutCourseAdd;
    @Bind(R.id.btn_add)
    Button mBtnAdd;
    @Bind(R.id.tv_course_remind)
    TextView mTvCourseRemind;

    private HuaShiDao dao;
    private List<String> mWeeks;
    private String weekday;
    private int time;
    private int duration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        ButterKnife.bind(this, view);
        mWeeks = new ArrayList<>();

        //填入数据,debug
        addData();
        dao = new HuaShiDao();
        mBtnAdd.setOnClickListener(this);
        mTvWeekSelect.setOnClickListener(this);
        mTvCourseTime.setOnClickListener(this);
        ((ScheduleActivity) getActivity()).setTitle("添加课程");
        return view;

    }

    private void addData() {
        for (int i = 0; i < 18; i++) {
            mWeeks.add("第" + i + "周");
        }
        weekday = "星期一";
        time = 3;
        duration = 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getActivity().getActionBar();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                ToastUtil.showShort("fen");

                dao.insertCourse(mEditCourseName.getText().toString(),
                        mEditTeacherName.getText().toString(),
                        mWeeks,
                        weekday,
                        time,
                        duration,
                        mEditCoursePlace.getText().toString(),
                        //待修改
                        "true");
                App.getCurrentActivity().onBackPressed();
                break;
            case R.id.tv_week_select:
                WeeksDialog weeksDialog = new WeeksDialog((ScheduleActivity) App.getCurrentActivity(),
                        mTvWeekSelect.getText().toString(), new WeeksDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String strWeek) {

                    }
                });
                weeksDialog.show();

        }

    }






    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add_course).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    //
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_empty,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
