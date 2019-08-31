package net.muxi.huashiapp.ui.timeTable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muxistudio.common.util.DimensUtil;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 17/1/29.
 * 选择周次的 view
 */
//手动选择周数的view

public class WeekSelectedView extends GridLayout {

    private TextView[] mTvWeeks;
    private int selectedWeekPos = 21;
    private OnWeekSelectedListener mOnWeekSelectedListener;

    public static final int SELECTED_VIEW_HEIGHT
            = (DimensUtil.getScreenWidth() - DimensUtil.dp2px(32)) / 7 - DimensUtil.dp2px(24);
    public static final int SELECTED_VIEW_MARGIN
            = DimensUtil.dp2px(12);

    public WeekSelectedView(Context context) {
        this(context, null);
    }
    public WeekSelectedView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setColumnCount(7);
        this.setRowCount(3);
        this.setPadding(DimensUtil.dp2px(16),0,DimensUtil.dp2px(16),0);
        this.setBackgroundColor(Color.WHITE);
        this.setVisibility(INVISIBLE);
        mTvWeeks = new TextView[21];
        for (int i = 0; i < 21; i++) {
            mTvWeeks[i] = new TextView(context);
            mTvWeeks[i].setText((i + 1) + "");
            mTvWeeks[i].setTextColor(getResources().getColor(android.R.color.primary_text_light));
            mTvWeeks[i].setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(SELECTED_VIEW_HEIGHT,SELECTED_VIEW_HEIGHT);
            this.addView(mTvWeeks[i],params);
            LayoutParams tvWeekLayoutParams = (GridLayout.LayoutParams) mTvWeeks[i].getLayoutParams();
            tvWeekLayoutParams.leftMargin = SELECTED_VIEW_MARGIN;
            tvWeekLayoutParams.rightMargin = SELECTED_VIEW_MARGIN;
            tvWeekLayoutParams.topMargin = SELECTED_VIEW_MARGIN;
            tvWeekLayoutParams.bottomMargin = SELECTED_VIEW_MARGIN;

            final int selectedWeek = i;
            mTvWeeks[i].setOnClickListener(view -> {
                if (mOnWeekSelectedListener != null) {
                    mOnWeekSelectedListener.onWeekSelected(selectedWeek + 1);
                }
                setSelectedWeek(selectedWeek + 1);
                slideUp();
            });
        }

    }
    public void setSelectedWeek(int week) {
        if (selectedWeekPos != 21){
            mTvWeeks[selectedWeekPos].setBackground(null);
            mTvWeeks[selectedWeekPos].setTextColor(Color.BLACK);
        }
        mTvWeeks[week - 1].setBackgroundResource(R.drawable.bg_selected_week);
        mTvWeeks[week - 1].setTextColor(Color.WHITE);
        selectedWeekPos = week - 1;
    }
    public int getSelectedWeek(){
        return selectedWeekPos;
    }
    public void slideUp(){
        slide(-DimensUtil.dp2px(SELECTED_VIEW_HEIGHT * 3));
    }
    public void slideDown(){
        slide(0);
    }
    public void slide(int toY){
        // FIXME: 19-9-1 属性动画
        TranslateAnimation animation;
        if (toY < 0){
            animation = new TranslateAnimation(0,0,0,toY);
        }else {
            animation = new TranslateAnimation(0,0,-DimensUtil.dp2px((SELECTED_VIEW_HEIGHT + DimensUtil.dp2px(24))* 3),0);
        }
        animation.setDuration(250);
        animation.setFillAfter(true);
        this.startAnimation(animation);
    }
    public void setOnWeekSelectedListener(OnWeekSelectedListener onWeekSelectedListener){
        mOnWeekSelectedListener = onWeekSelectedListener;
    }
    public interface OnWeekSelectedListener{
        void onWeekSelected(int week);
    }
}
