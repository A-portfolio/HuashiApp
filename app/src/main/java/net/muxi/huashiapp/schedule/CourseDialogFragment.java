package net.muxi.huashiapp.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.Logger;

/**
 * Created by ybao on 16/5/28.
 */
public class CourseDialogFragment extends DialogFragment implements NumberPicker.OnValueChangeListener {

    private NumberPicker mNumPickerWeekday;
    private NumberPicker mNumPickerStart;
    private NumberPicker mNumPickerEnd;

    //星期
    private int mWeekday;
    //开始的节次
    private int mStartTime;
    //结束的节次
    private int mEndTime;

    private String[] weeks = new String[]{
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六",
            "星期日"
    };
    private String[] startTimes = new String[14];
    private String[] endTimes = new String[14];

    public static final String WEEKDAY = "weekday";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    private NoticeDialogListener mNoticeDialogListener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(String weekday, int startTime, int endTime);

        void onDialogNegativeClick();
    }

    public CourseDialogFragment() {
        super();
    }


    public static CourseDialogFragment newInstance(int weekday, int startTime, int endTime) {
        Bundle args = new Bundle();
        CourseDialogFragment fragment = new CourseDialogFragment();
        args.putInt(WEEKDAY, weekday);
        args.putInt(START_TIME, startTime);
        args.putInt(END_TIME, endTime);
        fragment.setArguments(args);
        Logger.d("weekday:" + weekday + "  startTime:" + startTime + "  endTime:" + endTime);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekday = getArguments().getInt(WEEKDAY);
        mStartTime = getArguments().getInt(START_TIME);
        mEndTime = getArguments().getInt(END_TIME);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mNoticeDialogListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement NoticeDialogListener");
        }

        Window window = activity.getWindow();
        window.getDecorView().setBackgroundColor(App.getContext().getResources().getColor(android.R.color.transparent));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater infalter = getActivity().getLayoutInflater();
        View view = infalter.inflate(R.layout.fragment_dialog_course, null);
        mNumPickerWeekday = (NumberPicker) view.findViewById(R.id.num_picker_weekday);
        mNumPickerStart = (NumberPicker) view.findViewById(R.id.num_picker_start);
        mNumPickerEnd = (NumberPicker) view.findViewById(R.id.num_picker_end);
        setupNumberPicker();

//        int textViewId = App.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//        TextView tv = (TextView) view.findViewById(textViewId);
//        tv.setTextColor(App.getContext().getResources().getColor(R.color.colorAccent));

        builder.setView(view)
                .setMessage("请选择上课时间")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNoticeDialogListener.onDialogPositiveClick(getWeekday(mNumPickerWeekday.getValue()),
                                mNumPickerStart.getValue(),
                                mNumPickerEnd.getValue());
                        dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNoticeDialogListener.onDialogNegativeClick();
                        dismiss();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(App.getContext().getResources().getColor(R.color.colorAccent));
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(App.getContext().getResources().getColor(R.color.colorAccent));
            }
        });
        return alertDialog;
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int pickerId = picker.getId();
        switch (pickerId) {
            case R.id.num_picker_start:
                if (newVal > mNumPickerEnd.getValue()) {
                    mNumPickerEnd.setValue(newVal);
                }
                break;
            case R.id.num_picker_end:
                if (newVal < mNumPickerStart.getValue()) {
                    mNumPickerEnd.setValue(oldVal);
                }
                break;
        }
    }


    //初始化numberpicker
    private void setupNumberPicker() {
        mNumPickerWeekday.setMinValue(0);
        mNumPickerWeekday.setMaxValue(6);
        mNumPickerWeekday.setDisplayedValues(weeks);
        mNumPickerWeekday.setWrapSelectorWheel(false);
        mNumPickerWeekday.setValue(mWeekday);

        for (int i = 0; i < 14; i++) {
            startTimes[i] = "第" + (i + 1) + "节";
            endTimes[i] = "第" + (i + 1) + "节";
        }
        mNumPickerStart.setMinValue(0);
        mNumPickerStart.setMaxValue(13);
        mNumPickerStart.setDisplayedValues(startTimes);
        mNumPickerStart.setWrapSelectorWheel(false);
        mNumPickerStart.setValue(mStartTime);

        mNumPickerEnd.setMinValue(0);
        mNumPickerEnd.setMaxValue(13);
        mNumPickerEnd.setDisplayedValues(endTimes);
        mNumPickerEnd.setWrapSelectorWheel(false);
        mNumPickerEnd.setValue(mEndTime);

        mNumPickerStart.setOnValueChangedListener(this);
        mNumPickerEnd.setOnValueChangedListener(this);
    }

    //获取星期的String形式
    private String getWeekday(int weekdayValue) {
        return weeks[weekdayValue];
    }


}
