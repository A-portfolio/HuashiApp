package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.AuditCourse;
import com.muxistudio.appcommon.net.CampusFactory;

import com.muxistudio.appcommon.utils.CommonTextUtils;
import net.muxi.huashiapp.R;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 18-2-2.
 */

//显示结果
public class
CourseAuditResultActivity extends ToolbarActivity {

    private RecyclerView mRvAuditCourse;
    private ImageView mIvErrorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_course);
        initView();
        //传递过来的数据
        setTitle("搜索结果");
        searchAuditCourse(getQueryMap(getIntent()));
    }

    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private void renderCourse(AuditCourse auditCourse) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        CourseAuditAdapter adapter = new CourseAuditAdapter(auditCourse);
        mRvAuditCourse.setLayoutManager(manager);
        mRvAuditCourse.setAdapter(adapter);
    }

    private HashMap<String, String> getQueryMap(Intent intent) {
        String courseName = intent.getStringExtra("courseName");
        String courseSubject = intent.getStringExtra("courseSubject");
        String courseTeacher = intent.getStringExtra("courseTeacher");
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(courseName)) {
            map.put("name", courseName);
        }
        if (!TextUtils.isEmpty(courseTeacher)) {
            map.put("t", courseTeacher);
        }
        if (!TextUtils.isEmpty(courseSubject)) {
            map.put("s", courseSubject);
        }
        return map;
    }

    private void searchAuditCourse(HashMap<String, String> map) {
        //api文档中只有name teacher subject三个部分
        showLoading(CommonTextUtils.generateRandomCourseText());
        CampusFactory.getRetrofitService().getAuditCourse(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(auditCourse -> {
                    if (!auditCourse.getRes().isEmpty()) {
                        renderCourse(auditCourse);
                        hideLoading();
                    } else {
                        showErrorMessage();
                    }
                }, throwable -> {
                    //可能回应发一个异常
                    try {
                        showErrorMessage();
                    } catch (Exception e) {
                        showErrorMessage();
                        e.printStackTrace();
                    }
                    throwable.printStackTrace();
                }, () -> {
                });
    }

    private void showErrorMessage() {
        hideLoading();
        mIvErrorView.setImageResource(R.drawable.audit_not_found);
        mIvErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        mRvAuditCourse = findViewById(R.id.rv_audit_course);
        mIvErrorView = findViewById(R.id.iv_error_view);
    }
}
