package net.muxi.huashiapp.ui.timeTable;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.event.RefreshTableEvent;
import com.muxistudio.common.util.DimensUtil;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/25
 */

public class DetailCoursesDialog extends DialogFragment {

    private List<Course> mCourseList;
    private int mSelectWeek;
    private Subscription mSubscription;
    private RelativeLayout mRootLayout;
    private ScrollView mScrollView;
    private LinearLayout mLayoutCourses;

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
        mSelectWeek = getArguments().getInt("select_week", 1);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_courses, null);
        initView(view);
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

    private void initView(View view) {
        mRootLayout = view.findViewById(R.id.root_layout);
        mScrollView = view.findViewById(R.id.scrollView);
        mLayoutCourses = view.findViewById(R.id.layout_courses);
    }
}
