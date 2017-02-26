package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.event.DeleteCourseOkEvent;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/25
 */

public class DetailCoursesDialog extends DialogFragment {

    @BindView(R.id.layout_courses)
    LinearLayout mLayoutCourses;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;

    private List<Course> mCourseList;
    private int mSelectWeek;

    private Subscription mSubscription;

//    private static final int MAX_CLICK_DISTANCE = DimensUtil.dp2px(15);
//
//    private float startX, startY;

    public static DetailCoursesDialog newInstance(List<Course> courseList, int selectWeek) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("course_list", (ArrayList) courseList);
        args.putInt("select_week", 1);
        DetailCoursesDialog fragment = new DetailCoursesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.CenterDialogStyle);
        mCourseList = getArguments().getParcelableArrayList("course_list");
        for (Course course : mCourseList) {
            Logger.d(course.course);
        }
        mSelectWeek = getArguments().getInt("select_week", 1);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_courses, null);
        ButterKnife.bind(this, view);
        addCourseViews(mCourseList, view);
        mRootLayout.setOnClickListener(v -> {
            dismiss();
        });

        mSubscription = RxBus.getDefault().toObservable(DeleteCourseOkEvent.class)
                .subscribe(deleteCourseOkEvent -> {
                    dismiss();
                },throwable -> throwable.printStackTrace());

        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void delCourse(Course course) {
        CampusFactory.getRetrofitService().deleteCourse(Base64Util.createBaseStr(App.sUser),
                course.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyResponseResponse -> {
                    if (verifyResponseResponse.code() == 200) {
                        ((BaseActivity) getContext()).showSnackbarShort(
                                R.string.tip_delete_course_ok);
                    } else {
                        ((BaseActivity) getContext()).showErrorSnackbarShort(
                                R.string.tip_err_server);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ((BaseActivity) getContext()).showErrorSnackbarShort(R.string.tip_err_server);
                });
    }


    private void addCourseViews(List<Course> courseList, View view) {
        if (courseList == null) {
            return;
        }
        for (Course course : courseList) {
            CourseDetailView courseDetailView = new CourseDetailView(getContext(), course,
                    mSelectWeek);

            courseDetailView.setOnEditButtonClickListener(v -> dismiss());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, DimensUtil.dp2px(4), 0, DimensUtil.dp2px(4));
            mLayoutCourses.addView(courseDetailView, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
