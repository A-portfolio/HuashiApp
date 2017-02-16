package net.muxi.huashiapp.ui.studyroom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/9.
 */

public class StudyTimePickerDialogFragment extends BottomDialogFragment {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.study_time_picker_view)
    StudyTimePickerView mStudyTimePickerView;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;


    public static StudyTimePickerDialogFragment newInstance(int week,int day){
        Bundle args = new Bundle();
        args.putInt("week",week);
        args.putInt("day",day);
        StudyTimePickerDialogFragment fragment = new StudyTimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_study_time, null);
        ButterKnife.bind(this,view);
        mStudyTimePickerView.setWeek(getArguments().getInt("week",0));
        mStudyTimePickerView.setDay(getArguments().getInt("day",0));

        mStudyTimePickerView.setOnValueChangeListener((week, day) -> {
            mTvTitle.setText(
                    String.format("第%d周周%s",week + 1, Constants.WEEKDAYS[day])
            );
        });

        Dialog dialog = createBottomDialog(view);
        mBtnEnter.setOnClickListener(v -> {
            dialog.dismiss();
            if (mOnPositiveButtonClickListener != null){
                mOnPositiveButtonClickListener.onPositiveButtonClick(
                        mStudyTimePickerView.getWeek(),
                        mStudyTimePickerView.getDay()
                );
            }
        });

        mBtnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        return dialog;

    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener){
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;

    }

    public interface OnPositiveButtonClickListener{
        void onPositiveButtonClick(int week,int day);
    }
}
