package net.muxi.huashiapp.schedule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by ybao on 16/5/27.
 * 选择课程的dialog
 */
public class WeeksDialog extends Dialog {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ll)
    LinearLayout mLl;
    @Bind(R.id.weeks_layout)
    GridLayout mWeeksLayout;
    @Bind(R.id.btn_negative)
    TextView mBtnNegative;
    @Bind(R.id.btn_positive)
    TextView mBtnPositive;
    @Bind(R.id.btn_layout)
    LinearLayout mBtnLayout;
    @Bind(R.id.checkbox_single_week)
    CheckBox mCheckboxSingleWeek;
    @Bind(R.id.checkbox_double_week)
    CheckBox mCheckboxDoubleWeek;
    @Bind(R.id.checkbox_all_week)
    CheckBox mCheckboxAllWeek;

    private TextView[] mTvWeeks;

    private Context mContext;
    private String mWeek;

    private static final int TV_WEEK_WIDTH = DimensUtil.dp2px(30);
    private static final int TV_WEEK_HEIGHT = DimensUtil.dp2px(20);

    private OnDialogClickListener mOnDialogClickListener;

    //设置 dialog 点击的回调接口
    public interface OnDialogClickListener {
        public void onDialogClick(String strWeek);
    }


    public WeeksDialog(Context context, String week, OnDialogClickListener onDialogClickListener) {
        super(context, R.style.DialogStyle);
        mContext = context;
        mWeek = week;
        mOnDialogClickListener = onDialogClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_weeks, null);
        mWeeksLayout = (GridLayout) view.findViewById(R.id.weeks_layout);
        this.setTitle("请选择要上的周");
        this.setContentView(view);
        setUpGridLayout();
    }

    private void setUpGridLayout() {
        mTvWeeks = new TextView[24];
        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(
                TV_WEEK_WIDTH,
                TV_WEEK_HEIGHT
        );
        for (int i = 0; i < 24; i++) {
            mTvWeeks[i] = new TextView(mContext);
            mTvWeeks[i].setLayoutParams(tvParams);
            mTvWeeks[i].setGravity(Gravity.CENTER);
            mTvWeeks[i].setText(i + 1 + "");
            mWeeksLayout.addView(mTvWeeks[i]);
        }
    }


    //解析上课的周的字符串
    public List<Integer> parseWeeks(String s) {
        List<Integer> weeks = new ArrayList<>();
        String str = "";
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


    //解析带有 - 的字符串
    public List<Integer> parseConnectLine(String s) {
        List<Integer> list = new ArrayList<>();
        int position = s.indexOf("-");
        String s1 = s.substring(0, position - 1);
        String s2 = s.substring(position + 1, s.length() - 1);
        for (int i = Integer.valueOf(s1); i <= Integer.valueOf(s2); i++) {
            list.add(i);
        }
        return list;
    }


    //transfrom list to string
    public String transList(List<Integer> list) {
        if (list.size() == 0) {
            return new String("请选择上课周数");
        }
        String s;
        if (list.size() == 1) {
            s = "第" + list.get(0) + "周";
            return s;
        }
        if (isSingleWeeks(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周(单)";
            return s;
        }
        if (isDoubleWeeks(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周(双)";
            return s;
        }
        if (isContinuous(list) && list.size() > 1) {
            s = list.get(0) + "-" + list.get(list.size() - 1) + "周";
            return s;
        }
        int n = list.get(0);
        int j = -1;
        for (int i = 0; i < list.size(); i++) {
//            if (n1 == 0) {
//                n1 = list.get(i);
//            } else if (n1 != 0 && list.get(i) - n1 == 1 && n2 == 0) {
//                n2 = list.get(i);
//            } else if (n2 != 0) {
//                if (list.get(i) - n2 == 1) {
//                    n2 = list.get(i);
//                } else
//            }
            if (list.get(i + 1) - n == 1) {
                j = list.get(i);
                n++;
            }
        }
        return new String("fen");
    }


    private boolean isContinuous(List<Integer> list) {
        if (list.get(list.size() - 1) - list.get(0) == list.size() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDoubleWeeks(List<Integer> list) {
        boolean b = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 != 0) {
                b = false;
                break;
            }
        }
        return b;
    }

    private boolean isSingleWeeks(List<Integer> list) {
        boolean b = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 != 1) {
                b = false;
                break;
            }
        }
        return b;
    }
}
