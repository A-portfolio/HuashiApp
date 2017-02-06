package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;


import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 17/2/1.
 */

public class SelectDialogFragment extends BottomDialogFragment {

    @BindView(R.id.tv_single_week)
    TextView mTvSingleWeek;
    @BindView(R.id.tv_double_week)
    TextView mTvDoubleWeek;
    @BindView(R.id.tv_all_week)
    TextView mTvAllWeek;
    @BindView(R.id.grid_layout)
    GridLayout mGridLayout;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private Context mContext;

    private TextView[] mTvWeeks;
    private ArrayList<Integer> weekList;

    private PositiveButtonClickListener mPositiveButtonClickListener;

    public static final int WEEK_HEIGHT = 24 * 3;
    public static final int WEEK_WIDTH = 24 * 3;

    public static final int WEEK_MARGIN_VERTICAL = 16 * 3;
    public static final int WEEK_MARGIN_HORIZONTAL = 12 * 3;
    public static final int DIALOG_MARGIN = 8 * 3;

    public static final int WEEK_LENGTH = 21;

    public static SelectDialogFragment newInstance(ArrayList<Integer> weekList) {
        Bundle args = new Bundle();
        args.putIntegerArrayList("weeks", weekList);
        SelectDialogFragment fragment = new SelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initWeekLayout() {
        mTvSingleWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvSingleWeek.getBackground() == null) {
                    setSingleWeeks();
                    setMultiSelectButtonBg();
                    mTvSingleWeek.setBackgroundResource(R.drawable.bg_multi_select);
                } else {
                    setAllWeeks(false);
                    mTvSingleWeek.setBackground(null);
                }
            }
        });

        mTvDoubleWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvDoubleWeek.getBackground() == null) {
                    setDoubleWeeks();
                    setMultiSelectButtonBg();
                    mTvDoubleWeek.setBackgroundResource(R.drawable.bg_multi_select);
                } else {
                    setAllWeeks(false);
                    mTvDoubleWeek.setBackground(null);
                }
            }
        });
        mTvAllWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvAllWeek.getBackground() == null) {
                    setAllWeeks(true);
                    setMultiSelectButtonBg();
                    mTvAllWeek.setBackgroundResource(R.drawable.bg_multi_select);
                } else {
                    setAllWeeks(false);
                    mTvAllWeek.setBackground(null);
                }
            }
        });
        mTvWeeks = new TextView[21];
        for (int i = 0; i < 21; i++) {
            mTvWeeks[i] = new TextView(mContext);
            mTvWeeks[i].setText((i + 1) + "");
            mTvWeeks[i].setGravity(Gravity.CENTER);
            mTvWeeks[i].setTextColor(getResources().getColor(R.color.colorBottomDialogText));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WEEK_WIDTH,
                    WEEK_HEIGHT);
            params.setMargins(WEEK_MARGIN_HORIZONTAL, WEEK_MARGIN_VERTICAL, WEEK_MARGIN_HORIZONTAL,
                    WEEK_MARGIN_VERTICAL);
            if (weekList.contains(i + 1)) {
                mTvWeeks[i].setBackgroundResource(R.drawable.bg_selected_week);
            }
            mGridLayout.addView(mTvWeeks[i], params);
            mTvWeeks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getBackground() == null) {
                        view.setBackgroundResource(R.drawable.bg_selected_week);
                    } else {
                        view.setBackground(null);
                    }
                    mTvSingleWeek.setBackground(null);
                    mTvDoubleWeek.setBackground(null);
                    mTvAllWeek.setBackground(null);
                }
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        weekList = getArguments().getIntegerArrayList("weeks");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_multi_week, null);
        ButterKnife.bind(this, view);
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        Dialog dialog = new Dialog(getContext(), R.style.BottomDialogStyle);
        Dialog dialog = createBottomDialog();
        initWeekLayout();
        dialog.setContentView(view);
//        dialog.setContentView(view);

//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams wmlp = window.getAttributes();
//        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(wmlp);
//        window.setBackgroundDrawableResource(R.drawable.bg_bottom_dialog);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @OnClick({R.id.btn_cancel, R.id.btn_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                this.dismiss();
                break;
            case R.id.btn_enter:
                this.dismiss();
                if (mPositiveButtonClickListener != null) {
                    mPositiveButtonClickListener.onPositiveButtonClickListener(getWeekList(),
                            getDisplayWeeks());
                }
                break;
        }
    }

    public String getDisplayWeeks() {
        String s;
        int start;
        int end;
        List<Integer> weekList = getWeekList();
        if (isSingleWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1) + 1;
            s = String.format("%d-%d周单",start,end);
        } else if (isDoubleWeeks(weekList)) {
            start = weekList.get(0) - 1;
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周双",start,end);
        } else if (isContinuOusWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周",start,end);
        }else {
            s = TextUtils.join(",",weekList);
        }
        return s;
    }

    public ArrayList<Integer> getWeekList() {
        weekList.clear();
        for (int i = 0; i < 21; i++) {
            if (mTvWeeks[i].getBackground() != null) {
                weekList.add(i + 1);
            }
        }
        return weekList;
    }

    /**
     * 设置所有多选按钮的背景
     */
    public void setMultiSelectButtonBg() {
        mTvSingleWeek.setBackground(null);
        mTvDoubleWeek.setBackground(null);
        mTvAllWeek.setBackground(null);
    }

    public boolean isSingleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 1) {
            for (int i = 0; i < weekList.size() - 1; i++) {
                if (weekList.get(i + 1) - weekList.get(i) != 2) {
                    b = false;
                    break;
                }
            }
            return b;
        } else {
            return false;
        }
    }

    public boolean isDoubleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 0) {
            for (int i = 0; i < weekList.size() - 1; i++) {
               if (weekList.get(i + 1) - weekList.get(i) != 2){
                   b = false;
                   break;
               }
            }
            return b;
        }else {
            return false;
        }
    }

    public boolean isContinuOusWeeks(List<Integer> weekList) {
        if (weekList.size() < 2){
            return false;
        }
        if (weekList.get(weekList.size() - 1) - weekList.get(0) == weekList.size() - 1){
            return true;
        }else {
            return false;
        }
    }

    public void setSingleWeeks() {
        for (int i = 0; i < 21; i++) {
            if (i % 2 == 0) {
                mTvWeeks[i].setBackgroundResource(R.drawable.bg_selected_week);
            } else {
                mTvWeeks[i].setBackground(null);
            }
        }
    }

    public void setDoubleWeeks() {
        for (int i = 0; i < WEEK_LENGTH; i++) {
            if (i % 2 == 0) {
                mTvWeeks[i].setBackground(null);
            } else {
                mTvWeeks[i].setBackgroundResource(R.drawable.bg_selected_week);
            }
        }
    }

    public void setAllWeeks(boolean b) {
        for (int i = 0; i < WEEK_LENGTH; i++) {
            if (b) {
                mTvWeeks[i].setBackgroundResource(R.drawable.bg_selected_week);
            } else {
                mTvWeeks[i].setBackground(null);
            }
        }
    }

    public void setOnPositiveButtonClickListener(
            PositiveButtonClickListener positiveButtonClickListener) {
        mPositiveButtonClickListener = positiveButtonClickListener;
    }


    public interface PositiveButtonClickListener {
        void onPositiveButtonClickListener(ArrayList<Integer> weeks, String displayWeeks);
    }
}
