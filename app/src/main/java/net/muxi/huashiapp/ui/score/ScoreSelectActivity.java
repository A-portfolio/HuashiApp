package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 17/2/11.
 */

public class ScoreSelectActivity extends ToolbarActivity {

    @BindView(R.id.layout_term)
    RadioGroup mLayoutTerm;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.rb_all)
    RadioButton mRbAll;
    @BindView(R.id.rb_1)
    RadioButton mRb1;
    @BindView(R.id.rb_2)
    RadioButton mRb2;
    @BindView(R.id.rb_3)
    RadioButton mRb3;
    @BindView(R.id.tv_select_year)
    TextView mTvSelectYear;
    @BindView(R.id.et_year)
    TextView mTvYear;

    private int value = 0;
    private String[] years = UserUtil.generateHyphenYears(4);

    public static void start(Context context) {
        Intent starter = new Intent(context, ScoreSelectActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_select);
        ButterKnife.bind(this);
        setTitle("成绩查询");
        setYear(value);
        mRbAll.setChecked(true);
        mBtnEnter.setOnClickListener(v -> {
            int term = 0;
            switch (mLayoutTerm.getCheckedRadioButtonId()) {
                case R.id.rb_1:
                    term = 3;
                    break;
                case R.id.rb_2:
                    term = 12;
                    break;
                case R.id.rb_3:
                    term = 16;
                    break;
            }
            ScoreActivity.start(ScoreSelectActivity.this, mTvYear.getText().toString().substring(0,4), term + "");
        });

    }

    @OnClick({R.id.tv_select_year, R.id.et_year})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_year:
            case R.id.et_year:
                showSelectDialog();
                break;
        }
    }

    private void showSelectDialog() {
        SelectYearDialogFragment fragment = SelectYearDialogFragment.newInstance(value);
        fragment.show(getSupportFragmentManager(), "score_year_select");
        fragment.setOnPositionButtonClickListener(v -> {
            setYear(fragment.getValue());
        });
    }

    public void setYear(int value) {
        mTvYear.setText(String.format("%s学年", UserUtil.generateHyphenYears(4)[value]));
    }

}
