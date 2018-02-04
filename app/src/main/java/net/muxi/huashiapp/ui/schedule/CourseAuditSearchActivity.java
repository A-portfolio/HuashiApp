package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kolibreath on 18-2-2.
 */

public class CourseAuditSearchActivity extends ToolbarActivity{

    @BindView(R.id.et_course)
    EditText mEtSearch;
    @BindView(R.id.et_course_teacher)
    EditText mEtCourseTeacher;
    @BindView(R.id.et_course_subject)
    EditText mEtSubject;
    @BindView(R.id.btn_ensure)
    TextView mTvEnsure;
    @OnClick(R.id.btn_ensure)
    public void submit(){
        String name = mEtSearch.getText().toString();
        String teacher = mEtCourseTeacher.getText().toString();
        String subject = mEtSubject.getText().toString();
        Intent intent = new Intent(this,CourseAuditResultActivity.class);
        intent.putExtra("courseName",name);
        intent.putExtra("courseTeacher",teacher);
        intent.putExtra("courseSubject",subject);
        CourseAuditResultActivity.start(this,intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_search);
        ButterKnife.bind(this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CourseAuditSearchActivity.class);
        context.startActivity(starter);
    }

}