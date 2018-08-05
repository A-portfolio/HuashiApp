package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.utils.UserUtil;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/11.
 */

public class ScoreSelectActivity extends ToolbarActivity {

    private int value = 0;
    private String[] years = UserUtil.generateHyphenYears(4);
    private ImageView mIvYear;
    private TextView mTvSelectYear;
    private TextView mTvYear;
    private View mDivider;
    private TextView mTvType;
    private RadioGroup mLayoutTerm;
    private RadioButton mRbAll;
    private RadioButton mRb1;
    private RadioButton mRb2;
    private RadioButton mRb3;
    private Button mBtnEnter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ScoreSelectActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_select);
        initView();
        setTitle("成绩查询");
        setYear(value);
        mRbAll.setChecked(true);
        mBtnEnter.setOnClickListener(v -> {
            if (NetUtil.isConnected()) {
                int term = 0;
                int bid = mLayoutTerm.getCheckedRadioButtonId();
                if (bid == R.id.rb_1){
                    term = 3;
                }else if (bid == R.id.rb_2){
                    term = 12;
                }else if (bid == R.id.rb_3){
                    term = 16;
                }
                ScoreDisplayActivity.start(ScoreSelectActivity.this, mTvYear.getText().toString().substring(0, 4), term + "");
            } else {
                ToastUtil.showShort(R.string.tip_net_error);
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_select_year || id == R.id.tv_year){
            showSelectDialog();
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

    private void initView() {
        mIvYear = findViewById(R.id.iv_year);
        mTvSelectYear = findViewById(R.id.tv_select_year);
        mDivider = findViewById(R.id.divider);
        mTvType = findViewById(R.id.tv_type);
        mTvYear = findViewById(R.id.tv_year);
        mLayoutTerm = findViewById(R.id.layout_term);
        mRbAll = findViewById(R.id.rb_all);
        mRb1 = findViewById(R.id.rb_1);
        mRb2 = findViewById(R.id.rb_2);
        mRb3 = findViewById(R.id.rb_3);
        mBtnEnter = findViewById(R.id.btn_enter);
        mTvSelectYear.setOnClickListener(v -> onClick(v));
        mTvYear.setOnClickListener(v -> onClick(v));
    }
}
