package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/14.
 */
public class CurweekSetDialog extends Dialog {


    @BindView(R.id.gl_weeks)
    GridLayout mGlWeeks;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    private LinearLayout[] weeksLayout;
    private TextView[] mTvWeeks;
    private AppCompatRadioButton[] mRadioButtons;
    private int lastPos;
    private int pos;

    private OnDialogPostiveClickListener mOnDialogPostiveClickListener;

    private Context mContext;


    public CurweekSetDialog(Context context,int curWeek) {
        super(context,R.style.DialogStyle);
        mContext = context;
        pos = curWeek - 1;
    }

    public interface OnDialogPostiveClickListener{
        void onDialogPostiveClick(int curWeek);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_curweek_set);
        ButterKnife.bind(this);
        initView();
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDialogPostiveClickListener != null){
                    mOnDialogPostiveClickListener.onDialogPostiveClick(pos + 1);
                }
                dismiss();
            }
        });
    }

    public void setOnDialogPostiveClickListener(OnDialogPostiveClickListener dialogPostiveClickListener){
        mOnDialogPostiveClickListener = dialogPostiveClickListener;
    }

    private void initView() {
        weeksLayout = new LinearLayout[AppConstants.WEEKS_LENGTH];
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                DimensUtil.dp2px(106),
                DimensUtil.dp2px(56)
        );
        mTvWeeks = new TextView[AppConstants.WEEKS_LENGTH];
        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        ViewGroup.LayoutParams boxParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mRadioButtons = new AppCompatRadioButton[AppConstants.WEEKS_LENGTH];

        for (int i = 0; i < AppConstants.WEEKS_LENGTH; i++) {
            weeksLayout[i] = new LinearLayout(mContext);
            weeksLayout[i].setPadding(DimensUtil.dp2px(16), 0, 0, 0);
            weeksLayout[i].setLayoutParams(layoutParams);
            mGlWeeks.addView(weeksLayout[i]);

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
}
