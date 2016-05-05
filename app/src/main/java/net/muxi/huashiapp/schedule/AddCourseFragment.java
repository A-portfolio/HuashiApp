package net.muxi.huashiapp.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.util.ToastUtil;

/**
 * Created by ybao on 16/5/1.
 */
public class AddCourseFragment extends BaseFragment {

    EditText mEditCourseName;
    EditText mEditTeacherName;
    TextView mTvWeekSelect;
    TextView mTvCourseTime;
    EditText mEditCoursePlace;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container,false);
        mEditCourseName = (EditText) view.findViewById(R.id.edit_course_name);
        mEditCourseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort("edit");
            }
        });
        mEditTeacherName = (EditText) view.findViewById(R.id.edit_teacher_name);
        mTvWeekSelect = (TextView) view.findViewById(R.id.tv_week_select);
        mTvCourseTime = (TextView) view.findViewById(R.id.tv_course_time);
        mEditCoursePlace = (EditText) view.findViewById(R.id.edit_course_place);
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getActivity().getActionBar();

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
    }

}
