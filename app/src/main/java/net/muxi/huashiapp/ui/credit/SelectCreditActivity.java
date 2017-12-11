package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 17/2/9.
 */

public class SelectCreditActivity extends ToolbarActivity {

    @BindView(R.id.et_year)
    TextView mEtYear;
    @BindView(R.id.tv_credit)
    TextView mTvCredit;
    @BindView(R.id.tv_credit_grade)
    TextView mTvCreditGrade;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private String start;
    private String end;

    public static final int CREDIT = 1;
    public static final int CREDIT_GRADE = 2;

    private int calType = 2;

    public static void start(Context context) {
        Intent starter = new Intent(context, SelectCreditActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        ButterKnife.bind(this);
        setTitle("算学分");
        String[] years = UserUtil.generateYears(6);
        mEtYear.setText(String.format("%s-%s学年", years[0],years[1]));
        start = years[0];
        end = years[1];

    }

    @OnClick({R.id.tv_select_year, R.id.et_year, R.id.tv_credit, R.id.tv_credit_grade,
            R.id.btn_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_year:
            case R.id.et_year:
                CreditYearSelectDialog creditYearSelectDialog = CreditYearSelectDialog.newInstance(start,end);
                creditYearSelectDialog.show(getSupportFragmentManager(),"creditYearSelect");
                creditYearSelectDialog.setOnPositiveButtonClickListener((startYear, endYear) -> {
                    mEtYear.setText(String.format("%s-%s学年",startYear,endYear));
                    start = startYear;
                    end = endYear;
                });
                break;
            case R.id.tv_credit:
                mTvCredit.setBackgroundResource(R.drawable.shape_green);
                mTvCredit.setTextColor(Color.WHITE);
                mTvCreditGrade.setBackgroundResource(R.drawable.shape_disabled);
                mTvCreditGrade.setTextColor(getResources().getColor(R.color.disable_color));
                calType = CREDIT;
                break;
            case R.id.tv_credit_grade:
                mTvCreditGrade.setBackgroundResource(R.drawable.shape_green);
                mTvCreditGrade.setTextColor(Color.WHITE);
                mTvCredit.setBackgroundResource(R.drawable.shape_disabled);
                mTvCredit.setTextColor(getResources().getColor(R.color.disable_color));
                calType = CREDIT_GRADE;
                break;
            case R.id.btn_enter:
                if (calType == CREDIT){
                    CreditResultActivity.start(SelectCreditActivity.this,Integer.parseInt(start),Integer.parseInt(end));
                }else {
                    CreditGradeActivity.start(SelectCreditActivity.this,Integer.parseInt(start),Integer.parseInt(end));
                }
                break;
        }
    }
}
