package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/5/10.
 * 选择周数的布局
 */
public class WeekHScrollView extends HorizontalScrollView {

    private TextView[] mTextViews;
    private LinearLayout mLinearLayout;
    private Context mContext;

    //上一个被点击的 View的序号,也就是有背景色的
    private int mLastClickWeek;


    private OnWeekChangeListener mOnWeekChangeListener;

    private String weekFormat = "第%d周";

    public WeekHScrollView(Context context) {
        this(context, null);
    }


    public WeekHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        mLinearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mLinearLayout.setLayoutParams(layoutParams);
        this.addView(mLinearLayout);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        mTextViews = new TextView[24];
        for (int i = 0; i < 24; i++) {
            mTextViews[i] = new TextView(mContext);
            mTextViews[i].setWidth(DimensUtil.dp2px(50));
            mTextViews[i].setGravity(Gravity.CENTER);
            mTextViews[i].setText(String.format(weekFormat, i + 1));
            mTextViews[i].setTextColor(Color.BLACK);
            mTextViews[i].setLayoutParams(tvParams);

            mTextViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0;j < 24 ;j ++) {
                        if (v == mTextViews[j]) {
                            mOnWeekChangeListener.OnWeekChange(j + 1);
                            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ((TextView) v).setTextColor(Color.WHITE);
                            //在重复按同一个TextView的情况下
                            if (j != mLastClickWeek - 1) {
                                mTextViews[mLastClickWeek - 1].setTextColor(Color.BLACK);
                                mTextViews[mLastClickWeek - 1].setBackgroundColor(Color.WHITE);
                                mLastClickWeek = j + 1;
                            }
                            break;
                        }
                    }

                }
            });

            mLinearLayout.addView(mTextViews[i]);
        }

    }


    //要求传入的 int 刚好为第几周
    public void setCurWeek(int week){
        mLastClickWeek = week;
        mTextViews[mLastClickWeek - 1].setBackgroundColor(getResources().getColor(R.color.colorWeekLayout));
        mTextViews[mLastClickWeek - 1].setTextColor(Color.WHITE);
    }

    public void setOnWeekChangeListener(OnWeekChangeListener onWeekChangeListener) {
        mOnWeekChangeListener = onWeekChangeListener;
    }

}
