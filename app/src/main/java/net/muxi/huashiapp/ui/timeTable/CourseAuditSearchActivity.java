package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;


/**
 * Created by kolibreath on 18-2-2.
 */

public class CourseAuditSearchActivity extends ToolbarActivity {

    private EditText mEtCourse;
    private ImageView mIvCourseTeacher;
    private EditText mEtCourseTeacher;
    private EditText mEtCourseSubject;
    private TextView mBtnEnsure;

    public void submit() {
        String name = mEtCourse.getText().toString();
        String teacher = mEtCourseTeacher.getText().toString();
        String subject = mEtCourseSubject.getText().toString();
        Intent intent = new Intent(this, CourseAuditResultActivity.class);
        intent.putExtra("courseName", name);
        intent.putExtra("courseTeacher", teacher);
        intent.putExtra("courseSubject", subject);
        CourseAuditResultActivity.start(this, intent);
        MobclickAgent.onEvent(this, "course_audit");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_search);
        initView();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CourseAuditSearchActivity.class);
        context.startActivity(starter);
    }

    private void initView() {
        mEtCourse = findViewById(R.id.et_course);
        mIvCourseTeacher = findViewById(R.id.iv_course_teacher);
        mEtCourseTeacher = findViewById(R.id.et_course_teacher);
        mEtCourseSubject = findViewById(R.id.et_course_subject);
        mBtnEnsure = findViewById(R.id.btn_ensure);
        mBtnEnsure.setOnClickListener(v -> submit());
    }
}