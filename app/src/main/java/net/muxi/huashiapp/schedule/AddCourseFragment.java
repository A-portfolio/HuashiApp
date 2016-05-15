package net.muxi.huashiapp.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/1.
 */
public class AddCourseFragment extends BaseFragment implements View.OnClickListener {

//    EditText mEditCourseName;
//    EditText mEditTeacherName;
//    TextView mTvWeekSelect;
//    TextView mTvCourseTime;
//    EditText mEditCoursePlace;
//    Button btn;
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
    @Bind(R.id.btn)
    Button mBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

//        mEditCourseName = (EditText) view.findViewById(R.id.edit_course_name);
//        mEditCourseName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showShort("edit");
//            }
//        });
//        mEditTeacherName = (EditText) view.findViewById(R.id.edit_teacher_name);
//        mTvWeekSelect = (TextView) view.findViewById(R.id.tv_week_select);
//        btn = (Button) view.findViewById(R.id.btn);
//        btn.setOnClickListener(this);
//        mTvCourseTime = (TextView) view.findViewById(R.id.tv_course_time);
//        mEditCoursePlace = (EditText) view.findViewById(R.id.edit_course_place);
        ButterKnife.bind(this, view);
        mEditCourseName.setText("feng");
        mBtn.setOnClickListener(this);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getActivity().getActionBar();

    }


    @Override
    public void onClick(View v) {
        if (v == mBtn) {
            ToastUtil.showShort("fen");
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

}
