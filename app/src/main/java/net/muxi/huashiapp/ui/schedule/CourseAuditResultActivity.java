package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.net.CampusFactory;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 18-2-2.
 */

//显示结果
public class CourseAuditResultActivity extends ToolbarActivity {
    @BindView(R.id.rv_audit_course)
    RecyclerView rvCourse;
    @BindView(R.id.iv_error_view)
    ImageView ivErrorView;

    //针对选课部分的监听
    private View.OnClickListener listener = v -> {

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_course);
        ButterKnife.bind(this);
        //传递过来的数据
        setTitle("搜索结果");
        searchAuditCourse(getQueryMap(getIntent()));
    }

    public static void start(Context context,Intent intent) {
        context.startActivity(intent);
    }

    private void renderCourse(AuditCourse auditCourse){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        CourseAuditAdapter adapter = new CourseAuditAdapter(auditCourse);
        rvCourse.setLayoutManager(manager);
        rvCourse.setAdapter(adapter);
    }

    private HashMap<String,String > getQueryMap(Intent intent){
        String courseName = intent.getStringExtra("courseName");
        String courseSubject = intent.getStringExtra("courseSubject");
        String courseTeacher = intent.getStringExtra("courseTeacher");
        HashMap<String,String > map = new HashMap<>();
        if(!TextUtils.isEmpty(courseName)){
            map.put("name", courseName);
        }
        if(!TextUtils.isEmpty(courseTeacher)){
            map.put("t", courseTeacher);
        }
        if(!TextUtils.isEmpty(courseSubject)){
            map.put("s", courseSubject);
        }
        return map;
    }

    private void searchAuditCourse(HashMap<String,String> map){
        //api文档中只有name teacher subject三个部分
        showLoading();
        CampusFactory.getRetrofitService().getAuditCourse(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(auditCourse -> {
                        if(!auditCourse.getRes().isEmpty()) {
                            renderCourse(auditCourse);
                            hideLoading();
                        }
                    }
                        ,throwable->{
                    if(((HttpException)throwable).code()==500) {
                        hideLoading();
                        ivErrorView.setImageResource(R.mipmap.ic_launcher);
                        ivErrorView.setVisibility(View.VISIBLE);
                    }
                        },()->{});
    }


}
