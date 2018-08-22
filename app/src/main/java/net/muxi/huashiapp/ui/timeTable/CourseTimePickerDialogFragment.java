package net.muxi.huashiapp.ui.timeTable;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.widgets.BottomDialogFragment;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/5.
 */

public class CourseTimePickerDialogFragment extends BottomDialogFragment {

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    private TextView mTvTitle;
    private CourseTimePickerView mCourseTimePickerView;
    private Button mBtnCancel;
    private Button mBtnEnter;

    public static CourseTimePickerDialogFragment newInstance(int weekday, int start, int end) {
        Bundle args = new Bundle();
        args.putInt("weekday", weekday);
        args.putInt("start", start);
        args.putInt("ending", end);
        CourseTimePickerDialogFragment fragment = new CourseTimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_course_time,
                null);
        initView(view);
        mCourseTimePickerView.setWeekday(getArguments().getInt("weekday", 0));
        mCourseTimePickerView.setStartTime(getArguments().getInt("start", 0));
        mCourseTimePickerView.setEndTime(getArguments().getInt("ending", 1));
        mCourseTimePickerView.setOnValueChangeListener((weekday, start, end) -> {
            mTvTitle.setText(
                    String.format("周%s第%d至第%d节", Constants.WEEKDAYS[weekday], start + 1, end + 1));
        });

        Dialog dialog = createBottomDialog(view);

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

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
        mCourseTimePickerView = view.findViewById(R.id.course_time_picker_view);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnEnter = view.findViewById(R.id.btn_confirm);
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(int weekday, int start, int end);
    }
}
