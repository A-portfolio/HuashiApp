package net.muxi.huashiapp.schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/5/27.
 * 选择课程的dialog
 */
public class WeeksDialog extends Dialog implements View.OnClickListener {

    private TextView mTvTitle;
    private LinearLayout mLl;
    private GridLayout mWeeksLayout;
    private TextView mBtnNegative;
    private TextView mBtnPositive;
    private LinearLayout mBtnLayout;
    private CheckBox mCheckboxSingleWeek;
    private CheckBox mCheckboxDoubleWeek;
    private CheckBox mCheckboxAllWeek;

    private CheckBox[] mCBWeeks;

    private Context mContext;
    private String mWeek;
    private List<Integer> mLists;

    private static final int TV_WEEK_WIDTH = DimensUtil.dp2px(30);
    private static final int TV_WEEK_HEIGHT = DimensUtil.dp2px(20);

    private static final int TYPE_ALL = 0;
    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_DOUBLE = 2;

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
        mCheckboxSingleWeek = (CheckBox) view.findViewById(R.id.checkbox_single_week);
        mCheckboxAllWeek = (CheckBox) view.findViewById(R.id.checkbox_all_week);
        mCheckboxDoubleWeek = (CheckBox) view.findViewById(R.id.checkbox_double_week);
        mBtnNegative = (TextView) view.findViewById(R.id.btn_negative);
        mBtnPositive = (TextView) view.findViewById(R.id.btn_positive);

        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);

        this.setContentView(view);

        setUpGridLayout();
        initSelectWeeks();

    }


    private void initSelectWeeks() {
        //设置快捷选择按钮的监听事件
        mCheckboxSingleWeek.setOnClickListener(this);
        mCheckboxDoubleWeek.setOnClickListener(this);
        mCheckboxAllWeek.setOnClickListener(this);

        //根据原选的课程设置mCBWeeks的选中状态
        for (int i = 0; i < mLists.size(); i++) {
            mCBWeeks[mLists.get(i) - 1].setChecked(true);
            mCBWeeks[mLists.get(i) - 1].setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mCheckboxAllWeek) {
            if (mCheckboxAllWeek.isChecked()) {
                mCheckboxSingleWeek.setChecked(false);
                mCheckboxDoubleWeek.setChecked(false);
                switchCB(mCheckboxSingleWeek);
                switchCB(mCheckboxDoubleWeek);

                mCheckboxAllWeek.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
                handleCBWeeks(TYPE_ALL, true);
            } else {
                mCheckboxAllWeek.setBackgroundColor(Color.WHITE);
                handleCBWeeks(TYPE_ALL, false);
            }
        }
        if (v == mCheckboxSingleWeek) {
            if (mCheckboxSingleWeek.isChecked()) {
                mCheckboxAllWeek.setChecked(false);
                mCheckboxDoubleWeek.setChecked(false);
                switchCB(mCheckboxDoubleWeek);
                switchCB(mCheckboxAllWeek);

                mCheckboxSingleWeek.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
                handleCBWeeks(TYPE_SINGLE, true);
            } else {
                mCheckboxSingleWeek.setBackgroundColor(Color.WHITE);
                handleCBWeeks(TYPE_SINGLE, false);
            }
        }
        if (v == mCheckboxDoubleWeek) {
            if (mCheckboxDoubleWeek.isChecked()) {
                mCheckboxSingleWeek.setChecked(false);
                mCheckboxAllWeek.setChecked(false);
                switchCB(mCheckboxSingleWeek);
                switchCB(mCheckboxAllWeek);

                mCheckboxDoubleWeek.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
                handleCBWeeks(TYPE_DOUBLE, true);
            } else {
                mCheckboxDoubleWeek.setBackgroundColor(Color.WHITE);
                handleCBWeeks(TYPE_DOUBLE, false);
            }
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

    //对24个周进行批量操作,type 为0是全部,1是单周,2是双周
    private void handleCBWeeks(int type, boolean value) {
        if (type == TYPE_ALL) {
            for (int i = 0; i < 24; i++) {
                mCBWeeks[i].setChecked(value);
                switchCB(mCBWeeks[i]);
            }
        } else if (type == TYPE_SINGLE) {
            handleCBWeeks(TYPE_ALL, false);
            for (int i = 0; i < 24; i += 2) {
                mCBWeeks[i].setChecked(value);
                switchCB(mCBWeeks[i]);
            }
        } else if (type == TYPE_DOUBLE) {
            handleCBWeeks(TYPE_ALL, false);
            for (int i = 1; i < 24; i += 2) {
                mCBWeeks[i].setChecked(value);
                switchCB(mCBWeeks[i]);
            }
        }
    }


    //更换 checkbox 背景颜色,始终保持背景颜色和选中的状态一致
    private void switchCB(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            checkBox.setBackgroundColor(Color.WHITE);
        }
    }

    private void setUpGridLayout() {
        mCBWeeks = new CheckBox[24];
        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(
                TV_WEEK_WIDTH,
                TV_WEEK_HEIGHT
        );
        for (int i = 0; i < 24; i++) {
            mCBWeeks[i] = new CheckBox(mContext);
            mCBWeeks[i].setLayoutParams(tvParams);
            mCBWeeks[i].setButtonDrawable(android.R.color.transparent);
            mCBWeeks[i].setGravity(Gravity.CENTER);
            mCBWeeks[i].setText(i + 1 + "");
            mWeeksLayout.addView(mCBWeeks[i]);
            mCBWeeks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < 24; j++) {
                        if (v == mCBWeeks[j]) {
                            switchCB(mCBWeeks[j]);
                        }
                    }
                }

            });
        }
    }


    //解析上课的周的字符串
    public List<Integer> parseWeeks(String s) {
        List<Integer> weeks = new ArrayList<>();
        String str = "";
        if (s == "请选择上课的周数") {
            return weeks;
        }
        if (s.contains("单") || s.contains("双")){
            int position = s.indexOf('-');
            int start = Integer.valueOf(s.substring(0,position));
            int end = Integer.valueOf(s.substring(position + 1,s.length() - 4));
            for (int i = start;i <= end;i += 2){
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
