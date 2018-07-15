package net.muxi.huashiapp.ui.timeTable;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomDialogFragment;
import com.muxistudio.common.util.DimensUtil;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;


import static net.muxi.huashiapp.utils.TimeTableUtil.isContinuOusWeeks;
import static net.muxi.huashiapp.utils.TimeTableUtil.isDoubleWeeks;
import static net.muxi.huashiapp.utils.TimeTableUtil.isSingleWeeks;

/**
 * Created by ybao on 17/2/1.
 */

public class SelectDialogFragment extends BottomDialogFragment {

    private Context mContext;

    private TextView[] mTvWeeks;
    private ArrayList<Integer> weekList;

    private PositiveButtonClickListener mPositiveButtonClickListener;

    public static final int WEEK_HEIGHT = DimensUtil.dp2px(24);
    public static final int WEEK_WIDTH = DimensUtil.dp2px(24);

    public static final int WEEK_MARGIN_VERTICAL = DimensUtil.dp2px(16);
    public static final int WEEK_MARGIN_HORIZONTAL = DimensUtil.dp2px(12);

    public static final int WEEK_LENGTH = 21;
    private TextView mTvSingleWeek;
    private TextView mTvDoubleWeek;
    private TextView mTvAllWeek;
    private GridLayout mGridLayout;
    private TextView mBtnCancel;
    private TextView mBtnEnter;

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
                    setMultiWeekTextChecked(mTvSingleWeek, true);
                } else {
                    setAllWeeks(false);
                    setMultiWeekTextChecked(mTvSingleWeek, false);
                }
            }
        });

        mTvDoubleWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvDoubleWeek.getBackground() == null) {
                    setDoubleWeeks();
                    setMultiSelectButtonBg();
                    setMultiWeekTextChecked(mTvDoubleWeek, true);
                } else {
                    setAllWeeks(false);
                    setMultiWeekTextChecked(mTvDoubleWeek, false);
                }
            }
        });
        mTvAllWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvAllWeek.getBackground() == null) {
                    setAllWeeks(true);
                    setMultiSelectButtonBg();
                    setMultiWeekTextChecked(mTvAllWeek, true);
                } else {
                    setAllWeeks(false);
                    setMultiWeekTextChecked(mTvAllWeek, false);
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
            if (weekList.contains(i + 1)) {
                mTvWeeks[i].setBackgroundResource(R.drawable.bg_selected_week);
                mTvWeeks[i].setTextColor(Color.WHITE);
            }
            mGridLayout.addView(mTvWeeks[i], params);
            GridLayout.LayoutParams tvWeekLayoutParams = ((GridLayout.LayoutParams) mTvWeeks[i].getLayoutParams());
            tvWeekLayoutParams.leftMargin = WEEK_MARGIN_HORIZONTAL;
            tvWeekLayoutParams.rightMargin = WEEK_MARGIN_HORIZONTAL;
            tvWeekLayoutParams.topMargin = WEEK_MARGIN_VERTICAL;
            tvWeekLayoutParams.bottomMargin = WEEK_MARGIN_VERTICAL;

            mTvWeeks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getBackground() == null) {
                        view.setBackgroundResource(R.drawable.bg_selected_week);
                        ((TextView) view).setTextColor(Color.WHITE);
                    } else {
                        view.setBackground(null);
                        ((TextView) view).setTextColor(getResources().getColor(R.color.hintColor));
                    }
                    setMultiWeekTextChecked(mTvSingleWeek, false);
                    setMultiWeekTextChecked(mTvAllWeek, false);
                    setMultiWeekTextChecked(mTvDoubleWeek, false);
                }
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        weekList = getArguments().getIntegerArrayList("weeks");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_multi_week, null);
        initView(view);
        initWeekLayout();
        Dialog dialog = createBottomDialog(view);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            this.dismiss();
        } else if (id == R.id.btn_enter) {
            this.dismiss();
            if (mPositiveButtonClickListener != null) {
                mPositiveButtonClickListener.onPositiveButtonClickListener(getWeekList(),
                        getDisplayWeeks());
            }
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
            s = String.format("%d-%d周单", start, end);
        } else if (isDoubleWeeks(weekList)) {
            start = weekList.get(0) - 1;
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周双", start, end);
        } else if (isContinuOusWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周", start, end);
        } else {
            s = TextUtils.join(",", weekList);
            s += "周";
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
        setMultiWeekTextChecked(mTvSingleWeek, false);
        setMultiWeekTextChecked(mTvDoubleWeek, false);
        setMultiWeekTextChecked(mTvAllWeek, false);
    }

    public void setSingleWeeks() {
        for (int i = 0; i < 21; i++) {
            if (i % 2 == 0) {
                setWeekTextChecked(mTvWeeks[i], true);
            } else {
                setWeekTextChecked(mTvWeeks[i], false);
            }
        }
    }

    public void setDoubleWeeks() {
        for (int i = 0; i < WEEK_LENGTH; i++) {
            if (i % 2 == 0) {
                setWeekTextChecked(mTvWeeks[i], false);
            } else {
                setWeekTextChecked(mTvWeeks[i], true);
            }
        }
    }

    public void setAllWeeks(boolean b) {
        for (int i = 0; i < WEEK_LENGTH; i++) {
            if (b) {
                setWeekTextChecked(mTvWeeks[i], true);
            } else {
                setWeekTextChecked(mTvWeeks[i], false);
            }
        }
    }

    /**
     * 设置周 tv 的颜色和背景
     *
     * @param textView
     * @param checked
     */
    public void setWeekTextChecked(TextView textView, boolean checked) {
        if (checked) {
            textView.setBackgroundResource(R.drawable.bg_selected_week);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setBackground(null);
            textView.setTextColor(getResources().getColor(R.color.hintColor));
        }
    }

    /**
     * 设置多选周tv 的颜色和背景
     *
     * @param textView
     * @param checked
     */
    public void setMultiWeekTextChecked(TextView textView, boolean checked) {
        if (checked) {
            textView.setBackgroundResource(R.drawable.bg_multi_select);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setBackground(null);
            textView.setTextColor(getResources().getColor(R.color.hintColor));
        }
    }

    public void setOnPositiveButtonClickListener(
            PositiveButtonClickListener positiveButtonClickListener) {
        mPositiveButtonClickListener = positiveButtonClickListener;
    }

    private void initView(View view) {
        mTvSingleWeek = view.findViewById(R.id.tv_single_week);
        mTvDoubleWeek = view.findViewById(R.id.tv_double_week);
        mTvAllWeek = view.findViewById(R.id.tv_all_week);
        mGridLayout = view.findViewById(R.id.grid_layout);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnEnter = view.findViewById(R.id.btn_enter);
        mBtnCancel.setOnClickListener(v -> onClick(v));
        mBtnEnter.setOnClickListener(v -> onClick(v));
    }

    public interface PositiveButtonClickListener {
        void onPositiveButtonClickListener(ArrayList<Integer> weeks, String displayWeeks);
    }
}
