package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/5.
 */

public class CourseTimePickerDialogFragment extends BottomDialogFragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.course_time_picker_view)
    CourseTimePickerView mCourseTimePickerView;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;

    public static CourseTimePickerDialogFragment newInstance(int weekday, int start, int end) {
        Bundle args = new Bundle();
        args.putInt("weekday", weekday);
        args.putInt("start", start);
        args.putInt("end", end);
        CourseTimePickerDialogFragment fragment = new CourseTimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_course_time,
                null);
        ButterKnife.bind(this, view);
        Dialog dialog = createBottomDialog();
        dialog.setContentView(view);
        mCourseTimePickerView.setWeekday(getArguments().getInt("weekday", 0));
        mCourseTimePickerView.setStartTime(getArguments().getInt("start", 0));
        mCourseTimePickerView.setEndTime(getArguments().getInt("end", 1));

        mCourseTimePickerView.setOnValueChangeListener((weekday, start, end) -> {
            mTvTitle.setText(
                    String.format("周s%第d%至第d%节", Constants.WEEKDAYS[weekday], start + 1, end + 1));
        });

        mBtnEnter.setOnClickListener(v -> {
            dialog.dismiss();
            if (mOnPositiveButtonClickListener != null) {
                mOnPositiveButtonClickListener.onPositiveButtonClick(
                        mCourseTimePickerView.getWeekday(), mCourseTimePickerView.getStartTime(),
                        mCourseTimePickerView.getEndTime());
            }
        });

        mBtnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        return dialog;
    }

    public void setOnPositiveButtonClickListener(
            OnPositiveButtonClickListener onPositiveButtonClickListener) {
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }


    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(int weekday, int start, int end);
    }
}
