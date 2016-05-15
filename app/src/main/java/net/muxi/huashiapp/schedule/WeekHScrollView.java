package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ybao on 16/5/10.
 */
public class WeekHScrollView extends HorizontalScrollView {

    private TextView[] mTextViews;
    private LinearLayout mLinearLayout;
    private Context mContext;


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
            mTextViews[i].setText(String.format(weekFormat, i + 1));
            mTextViews[i].setLayoutParams(tvParams);
            mTextViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int j = 0;j < 24 ;j ++) {
                        if (v == mTextViews[j]) {
                            mOnWeekChangeListener.OnWeekChange(j + 1);
                            break;
                        }
                    }
                }
            });

            mLinearLayout.addView(mTextViews[i]);
        }

    }

    public void setOnWeekChangeListener(OnWeekChangeListener onWeekChangeListener) {
        mOnWeekChangeListener = onWeekChangeListener;
    }

}
