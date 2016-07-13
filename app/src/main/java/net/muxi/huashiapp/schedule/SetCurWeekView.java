package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/7/11.
 */
public class SetCurWeekView extends GridLayout{

    private Context mContext;
    private int mCurWeek;
    private TextView[] mTvWeeks;
    private LinearLayout[] weeksLayout;
    private AppCompatRadioButton[] mRadioButtons;

    //当前选中的RadioButton
    private int pos;
    private int lastPos;

    public SetCurWeekView(Context context,int curWeek) {
        super(context);
        mContext = context;
        mCurWeek = curWeek;
        pos = curWeek - 1;
        this.setColumnCount(3);
        this.setRowCount(6);
        initView();
    }

    private void initView() {
        weeksLayout = new LinearLayout[18];
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                DimensUtil.dp2px(106),
                DimensUtil.dp2px(56)
        );
        mTvWeeks = new TextView[18];
        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        ViewGroup.LayoutParams boxParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mRadioButtons = new AppCompatRadioButton[18];

        for (int i = 0; i < 18; i++) {
            weeksLayout[i] = new LinearLayout(mContext);
            weeksLayout[i].setPadding(DimensUtil.dp2px(16), 0, 0, 0);
            weeksLayout[i].setLayoutParams(layoutParams);
            this.addView(weeksLayout[i]);

            mTvWeeks[i] = new TextView(mContext);
            mTvWeeks[i].setGravity(Gravity.CENTER);
            mTvWeeks[i].setLayoutParams(tvParams);
            String s;
            if ((i + 1) / 10 < 1) {
                s = "第0" + (i + 1) + "周";
            } else {
                s = "第" + (i + 1) + "周";
            }
            mTvWeeks[i].setText(s);
            weeksLayout[i].addView(mTvWeeks[i]);

            mRadioButtons[i] = new AppCompatRadioButton(mContext);
            if (i == pos){
                mRadioButtons[i].setChecked(true);
            }
            mRadioButtons[i] = new AppCompatRadioButton(mContext);
            mRadioButtons[i].setLayoutParams(boxParams);
            weeksLayout[i].addView(mRadioButtons[i]);
            final int j = i;
            mRadioButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPos = pos;
                    pos = j;
                    ((AppCompatRadioButton) v).setChecked(true);
                    mRadioButtons[lastPos].setChecked(false);
                }

            });
        }
        mRadioButtons[pos].setChecked(true);

    }

    //获取选择的周次
    public int getSelectPostion(){
        return pos;
    }

}
