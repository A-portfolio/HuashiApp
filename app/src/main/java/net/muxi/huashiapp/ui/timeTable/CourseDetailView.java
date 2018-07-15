package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.RefreshTableEvent;
import com.muxistudio.appcommon.net.CampusFactory;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.utils.TimeTableUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/25
 */

public class CourseDetailView extends RelativeLayout {

    private Context mContext;
    private Space mSpaceCenter;
    private TextView mTvCourse;
    private TextView mTvStatus;
    private TextView mTvWeeks;
    private TextView mTvTeacher;
    private TextView mTvTime;
    private TextView mTvPlace;
    private LinearLayout mLayoutCancel;
    private LinearLayout mLayoutEdit;

    public CourseDetailView(Context context, Course course, int selectWeek) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_course_detail, this);
        initView();

        int color;
        if (TimeTableUtil.isThisWeek(selectWeek, course.getWeeks())) {
            color = getResources().getColor(R.color.primary_text_color);
        } else {
            color = getResources().getColor(R.color.grey);
            mTvWeeks.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_week_grey), null,
                    null, null);
            mTvTeacher.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_teacher_grey),
                    null, null, null);
            mTvTime.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_time_grey), null,
                    null, null);
            mTvPlace.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_place_grey),
                    null, null, null);
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
        mTvTime.setText(String.format("周%s%d-%d节",
                Constants.WEEKDAYS[TimeTableUtil.weekday2num(course.day)], course.start,
                course.during + course.start - 1));
        List<Integer> weekList = new ArrayList<>();
        String[] arrays = TextUtils.split(course.weeks, ",");
        for (String s : arrays) {
            try {
                weekList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mTvWeeks.setText(TimeTableUtil.getDisplayWeeks(weekList));

        mLayoutCancel.setOnClickListener(v -> {
            delCourse(course);
            if (mOnNegativeButtonClickListener != null) {
                mOnNegativeButtonClickListener.onClick(v);
            }
        });
        mLayoutEdit.setOnClickListener(v -> {
            CourseEditActivity.start(context, false, course);
            if (mOnEditButtonClickListener != null) {
                mOnEditButtonClickListener.onClick(v);
            }
        });
    }

    private void delCourse(Course course) {
        CampusFactory.getRetrofitService().deleteCourse(course.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(verifyResponseResponse -> {
                    if (verifyResponseResponse.code() == 200) {
                        Snackbar.make(((BaseAppActivity) mContext).getWindow().findViewById(
                                android.R.id.content),
                                R.string.tip_delete_course_ok, Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo, v -> {
                                    addCourse(course);
                                }).show();
                        HuaShiDao dao = new HuaShiDao();
                        dao.deleteCourse(course.id);
                        RxBus.getDefault().send(new RefreshTableEvent());
                    } else {
                        ((BaseAppActivity) mContext).showErrorSnackbarShort(R.string.tip_err_server);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ((BaseAppActivity) mContext).showErrorSnackbarShort(R.string.tip_err_server);
                });
    }

    public void addCourse(Course course) {
        CampusFactory.getRetrofitService()
                .addCourse(course)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(courseId -> {
                    if (courseId.id != 0) {
                        course.id = String.valueOf(courseId.id);
                        HuaShiDao dao = new HuaShiDao();
                        dao.insertCourse(course);
                        RxBus.getDefault().send(new RefreshTableEvent());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    addCourse(course);
                });
    }

    private OnClickListener mOnEditButtonClickListener;
    private OnClickListener mOnNegativeButtonClickListener;

    public void setOnEditButtonClickListener(OnClickListener listener) {
        mOnEditButtonClickListener = listener;
    }

    public void setOnNegativeButtonClickListener(OnClickListener listener) {
        mOnNegativeButtonClickListener = listener;
    }

    private void initView() {
        mSpaceCenter = findViewById(R.id.space_center);
        mTvCourse = findViewById(R.id.tv_course);
        mTvStatus = findViewById(R.id.tv_status);
        mTvWeeks = findViewById(R.id.tv_weeks);
        mTvTeacher = findViewById(R.id.tv_teacher);
        mTvTime = findViewById(R.id.tv_time);
        mTvPlace = findViewById(R.id.tv_place);
        mLayoutCancel = findViewById(R.id.layout_cancel);
        mLayoutEdit = findViewById(R.id.layout_edit);
    }
}
