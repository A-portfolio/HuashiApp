package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/11.
 */

public class ScoreSelectActivity extends ToolbarActivity {

    @BindView(R.id.et_year)
    EditText mEtYear;
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
            ScoreActivity.start(ScoreSelectActivity.this, mEtYear.getText().toString(), term + "");
        });

    }
}
