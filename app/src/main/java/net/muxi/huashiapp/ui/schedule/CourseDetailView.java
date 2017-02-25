package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/25
 */

public class CourseDetailView extends RelativeLayout {

    @BindView(R.id.tv_course)
    TextView mTvCourse;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_weeks)
    TextView mTvWeeks;
    @BindView(R.id.tv_teacher)
    TextView mTvTeacher;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_place)
    TextView mTvPlace;
    @BindView(R.id.layout_cancel)
    LinearLayout mLayoutCancel;
    @BindView(R.id.layout_edit)
    LinearLayout mLayoutEdit;

    private Context mContext;

    public CourseDetailView(Context context, Course course, int selectWeek) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_course_detail, this);
        ButterKnife.bind(this);

        int color;
        if (TimeTableUtil.isThisWeek(selectWeek, course.getWeeks())) {
            color = getResources().getColor(R.color.primary_text_color);
        } else {
            color = getResources().getColor(R.color.grey);
            mTvWeeks.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_week_grey),null,null,null);
            mTvTeacher.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_teacher_grey),null,null,null);
            mTvTime.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_time_grey),null,null,null);
            mTvPlace.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_place_grey),null,null,null);
            mTvStatus.setBackgroundResource(R.drawable.shape_category_tag_disabled);
            mTvStatus.setText("非本周");
        }

        mTvCourse.setTextColor(color);
        mTvWeeks.setTextColor(color);
        mTvTeacher.setTextColor(color);
        mTvTime.setTextColor(color);
        mTvPlace.setTextColor(color);

        mTvCourse.setText(course.course);
        mTvTeacher.setText(course.teacher);
        mTvPlace.setText(course.place);
        mTvTime.setText(String.format("%d-%d节",course.start,course.during + course.start - 1));
        List<Integer> weekList = new ArrayList<>();
        String[] arrays = TextUtils.split(course.weeks,",");
        for (String s : arrays){
            try {
                weekList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mTvWeeks.setText(TimeTableUtil.getDisplayWeeks(weekList));
        
        mLayoutCancel.setOnClickListener(v -> {
            delCourse(course);
            if (mOnNegativeButtonClickListener != null){
                mOnNegativeButtonClickListener.onClick(v);
            }
        });
        mLayoutEdit.setOnClickListener(v -> {
            CourseEditActivity.start(context,false,course);
            if (mOnPositiveButtonClickListener != null){
                mOnPositiveButtonClickListener.onClick(v);
            }
        });
    }

    private void delCourse(Course course) {
        CampusFactory.getRetrofitService().deleteCourse(Base64Util.createBaseStr(App.sUser),course.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyResponseResponse -> {
                    if (verifyResponseResponse.code() == 200){
                        ((BaseActivity)mContext).showSnackbarShort(R.string.tip_delete_course_ok);
                    }
                });
    }

    private OnClickListener mOnPositiveButtonClickListener;
    private OnClickListener mOnNegativeButtonClickListener;

    public void setOnPositiveButtonClickListener(OnClickListener listener){
        mOnPositiveButtonClickListener = listener;
    }

    public void setOnNegativeButtonClickListener(OnClickListener listener){
        mOnNegativeButtonClickListener = listener;
    }

}
