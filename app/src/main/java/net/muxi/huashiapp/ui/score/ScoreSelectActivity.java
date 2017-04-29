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
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.UserUtil;

import java.io.IOException;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

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
    TextView mEtYear;

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
//            searchScore(mEtYear.getText().toString(), term + "");
//            ScoreActivity.start(ScoreSelectActivity.this, mEtYear.getText().toString(), term +
// "");
        });

    }

//    private void searchScore(String s, String s1) {
//        Call call1 = CampusFactory.getRetrofitService().loginInfo("2014214629","fmc2014214629");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response response = call1.execute();
//                    String s = response.message();
//                    Logger.d(s);
//                    if (s.split("\"")[1].equals("index_jg.jsp")){
//                        HashSet<String> cookieSet = new HashSet<>();
////                        for (String str : response.headers("Set-Cookie")){
////
////                        }
//                    }
//                    CampusFactory.getRetrofitService().updateCookie().execute();
//                    CampusFactory.getRetrofitService().updateFinalCookie().execute();
//
//                    Logger.d("request end");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

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
        mEtYear.setText(String.format("%s学年", UserUtil.generateHyphenYears(4)[value]));
    }

}
