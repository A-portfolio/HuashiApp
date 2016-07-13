package net.muxi.huashiapp.schedule;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/5/27.
 * 选择课程的dialog
 */
public class WeeksDialog extends Dialog implements View.OnClickListener {

    private GridLayout mWeeksLayout;
    private Button mBtnNegative;
    private Button mBtnPositive;
    private LinearLayout mBtnLayout;
    private AppCompatRadioButton mRadioCustom;
    private AppCompatRadioButton mRadioSingle;
    private AppCompatRadioButton mRadioDouble;
    private AppCompatRadioButton mRadioAllWeek;

    //各周的布局
    private LinearLayout[] mLayouts;
    private TextView[] mTvWeeks;
    private AppCompatCheckBox[] mCBWeeks;

    private Context mContext;
    private String mWeek;
    private List<Integer> mLists;

    private static final int TYPE_ALL = 0;
    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_DOUBLE = 2;
    private static final int TYPE_CUSTOM = 3;

    private OnDialogClickListener mOnDialogClickListener;

    //设置 dialog 点击的回调接口
    public interface OnDialogClickListener {
        public void onDialogClick(List<Integer> list);
    }


    public WeeksDialog(Context context, String week, OnDialogClickListener onDialogClickListener) {
        super(context, R.style.DialogStyle);
        mContext = context;
        mWeek = week;
        mOnDialogClickListener = onDialogClickListener;
        mLists = parseWeeks(week);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_weeks, null);
        mWeeksLayout = (GridLayout) view.findViewById(R.id.weeks_layout);
        mRadioSingle = (AppCompatRadioButton) view.findViewById(R.id.radio_single_week);
        mRadioAllWeek = (AppCompatRadioButton) view.findViewById(R.id.radio_all_week);
        mRadioDouble = (AppCompatRadioButton) view.findViewById(R.id.radio_double_week);
        mRadioCustom = (AppCompatRadioButton) view.findViewById(R.id.radio_custom);
        mBtnNegative = (Button) view.findViewById(R.id.btn_negative);
        mBtnPositive = (Button) view.findViewById(R.id.btn_positive);

        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);

        this.setContentView(view);

        setUpGridLayout();
        initSelectWeeks();

    }


    private void initSelectWeeks() {
        //设置快捷选择按钮的监听事件
        mRadioSingle.setOnClickListener(this);
        mRadioDouble.setOnClickListener(this);
        mRadioAllWeek.setOnClickListener(this);
        mRadioCustom.setOnClickListener(this);

        //根据原选的课程设置mCBWeeks的选中状态
        for (int i = 0; i < mLists.size(); i++) {
            mCBWeeks[mLists.get(i) - 1].setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mRadioAllWeek) {
            handleCBWeeks(TYPE_ALL);
        }
        if (v == mRadioSingle) {
            handleCBWeeks(TYPE_SINGLE);
        }
        if (v == mRadioDouble) {
            handleCBWeeks(TYPE_DOUBLE);
        }
        if (v == mRadioCustom) {
            handleCBWeeks(TYPE_CUSTOM);
        }
        if (v == mBtnNegative) {
            dismiss();
        }
        if (v == mBtnPositive) {
            mOnDialogClickListener.onDialogClick(getTheSelectWeeks(mCBWeeks));
            dismiss();
        }
    }


    private List<Integer> getTheSelectWeeks(CheckBox[] checkBoxes) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                list.add(i + 1);
            }
        }
        return list;
    }

    //对18个周进行批量操作,type 为0是全部,1是单周,2是双周
    private void handleCBWeeks(int type) {
        if (type == TYPE_ALL) {
            for (int i = 0; i < 18; i++) {
                mCBWeeks[i].setChecked(true);
            }
        } else if (type == TYPE_SINGLE) {
            for (int i = 0; i < 18; i++) {
                mCBWeeks[i].setChecked(i % 2 == 0 ? true : false);
            }
        } else if (type == TYPE_DOUBLE) {
            for (int i = 0; i < 18; i++) {
                mCBWeeks[i].setChecked(i % 2 == 1 ? true : false);
            }
        } else if (type == TYPE_CUSTOM) {
            for (int i = 0; i < 18; i++) {
                mCBWeeks[i].setChecked(false);
            }
        }
    }


//    //更换 checkbox 背景颜色,始终保持背景颜色和选中的状态一致
//    private void switchCB(CheckBox checkBox) {
//        if (checkBox.isChecked()) {
//            checkBox.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
//        } else {
//            checkBox.setBackgroundColor(Color.WHITE);
//        }
//    }

    private void setUpGridLayout() {
        mLayouts = new LinearLayout[18];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
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
        mCBWeeks = new AppCompatCheckBox[18];

        for (int i = 0; i < 18; i++) {
            mLayouts[i] = new LinearLayout(mContext);
            mLayouts[i].setPadding(DimensUtil.dp2px(16), 0, 0, 0);
            mLayouts[i].setLayoutParams(layoutParams);
            mWeeksLayout.addView(mLayouts[i]);

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
            mLayouts[i].addView(mTvWeeks[i]);

            mCBWeeks[i] = new AppCompatCheckBox(mContext);
            mCBWeeks[i].setLayoutParams(boxParams);
            mCBWeeks[i].setSupportButtonTintList(new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checkable}
                    },new int[]{
                    getContext().getResources().getColor(R.color.colorPrimary)
            }));
            mLayouts[i].addView(mCBWeeks[i]);
            mCBWeeks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //凡是用户自己选择就是自定义
                    mRadioCustom.setChecked(true);
                }

            });
        }
    }


    //解析上课的周的字符串
    public List<Integer> parseWeeks(String s) {
        List<Integer> weeks = new ArrayList<>();
        String str = "";
        if (s.equals(mContext.getString(R.string.tip_select_weeks))) {
            return weeks;
        }
        if (s.contains("单") || s.contains("双")) {
            int position = s.indexOf('-');
            int start = Integer.valueOf(s.substring(0, position));
            int end = Integer.valueOf(s.substring(position + 1, s.length() - 4));
            for (int i = start; i <= end; i += 2) {
                weeks.add(i);
            }
            return weeks;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ',' && s.charAt(i) != '周') {
                str += s.charAt(i);
            }
            if (s.charAt(i) == ',' || s.charAt(i) == '周') {
                if (str.contains("-")) {
                    weeks.addAll(parseConnectLine(str));
                } else weeks.add(Integer.valueOf(str));
                str = "";
            }
        }

        return weeks;
    }


    //解析带有 "-" 的字符串
    public List<Integer> parseConnectLine(String s) {
        List<Integer> list = new ArrayList<>();
        int position = s.indexOf("-");
        String s1 = s.substring(0, position);
        String s2 = s.substring(position + 1, s.length());
        for (int i = Integer.valueOf(s1); i <= Integer.valueOf(s2); i++) {
            list.add(i);
        }
        return list;
    }


    //transfrom list to string把 存有选择了周数的 list 转化为 string


}
