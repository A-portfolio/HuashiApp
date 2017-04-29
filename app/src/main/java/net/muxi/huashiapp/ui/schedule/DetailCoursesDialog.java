package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.event.RefreshTableEvent;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

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
        args.putInt("select_week", selectWeek);
        DetailCoursesDialog fragment = new DetailCoursesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.FullScreenDialogStyle);
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

        mSubscription = RxBus.getDefault().toObservable(RefreshTableEvent.class)
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
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
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
