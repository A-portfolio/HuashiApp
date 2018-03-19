package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.utils.UserUtil;
import com.muxistudio.common.util.NetStatus;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/9.
 */

public class SelectCreditActivity extends ToolbarActivity {

    private String start;
    private String end;

    public static final int CREDIT = 1;
    public static final int CREDIT_GRADE = 2;

    private int calType = 2;
    private ImageView mIvYear;
    private TextView mTvSelectYear;
    private TextView mEtYear;
    private View mDivider;
    private TextView mTvType;
    private TextView mTvCredit;
    private TextView mTvCreditGrade;
    private Button mBtnEnter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SelectCreditActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        initView();
        setTitle("算学分");
        String[] years = UserUtil.generateYears(6);
        mEtYear.setText(String.format("%s-%s学年", years[0], years[1]));
        start = years[0];
        end = years[1];

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_select_year || id == R.id.tv_year) {
            CreditYearSelectDialog creditYearSelectDialog = CreditYearSelectDialog.newInstance(start, end);
            creditYearSelectDialog.show(getSupportFragmentManager(), "creditYearSelect");
            creditYearSelectDialog.setOnPositiveButtonClickListener((startYear, endYear) -> {
                mEtYear.setText(String.format("%s-%s学年", startYear, endYear));
                start = startYear;
                end = endYear;

            });
        } else if (id == R.id.tv_credit) {
            mTvCredit.setBackgroundResource(R.drawable.shape_green);
            mTvCredit.setTextColor(Color.WHITE);
            mTvCreditGrade.setBackgroundResource(R.drawable.shape_disabled);
            mTvCreditGrade.setTextColor(getResources().getColor(R.color.disable_color));
            calType = CREDIT;
        } else if (id == R.id.tv_credit_grade) {
            mTvCreditGrade.setBackgroundResource(R.drawable.shape_green);
            mTvCreditGrade.setTextColor(Color.WHITE);
            mTvCredit.setBackgroundResource(R.drawable.shape_disabled);
            mTvCredit.setTextColor(getResources().getColor(R.color.disable_color));
            calType = CREDIT_GRADE;
        } else if (id == R.id.btn_enter) {
            if (NetStatus.isConnected()) {
                if (calType == CREDIT) {
                    CreditResultActivity.start(SelectCreditActivity.this, Integer.parseInt(start)
                            , Integer.parseInt(end));
                } else {
                    CreditGradeActivity.start(SelectCreditActivity.this, Integer.parseInt(start)
                            , Integer.parseInt(end));
                }
            } else {
                ToastUtil.showShort(R.string.tip_net_error);
            }
        }
    }

    private void initView() {
        mIvYear = findViewById(R.id.iv_year);
        mTvSelectYear = findViewById(R.id.tv_select_year);
        mEtYear = findViewById(R.id.tv_year);
        mDivider = findViewById(R.id.divider);
        mTvType = findViewById(R.id.tv_type);
        mTvCredit = findViewById(R.id.tv_credit);
        mTvCreditGrade = findViewById(R.id.tv_credit_grade);
        mBtnEnter = findViewById(R.id.btn_enter);
        mTvSelectYear.setOnClickListener(v -> onClick(v));
        mEtYear.setOnClickListener(v -> onClick(v));
        mTvCredit.setOnClickListener(v -> onClick(v));
        mTvCreditGrade.setOnClickListener(v -> onClick(v));
        mBtnEnter.setOnClickListener(v -> onClick(v));
    }
}
