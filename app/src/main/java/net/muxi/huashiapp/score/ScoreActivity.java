package net.muxi.huashiapp.score;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.fragment_layout)
    FrameLayout mFragmentLayout;
    @BindView(R.id.title_year)
    TextView mTitleYear;
    @BindView(R.id.tv_year1)
    TextView mTvYear1;
    @BindView(R.id.tv_year2)
    TextView mTvYear2;
    @BindView(R.id.tv_year3)
    TextView mTvYear3;
    @BindView(R.id.tv_year4)
    TextView mTvYear4;
    @BindView(R.id.rb_year1)
    AppCompatRadioButton mRbYear1;
    @BindView(R.id.rb_year2)
    AppCompatRadioButton mRbYear2;
    @BindView(R.id.rb_year3)
    AppCompatRadioButton mRbYear3;
    @BindView(R.id.rb_year4)
    AppCompatRadioButton mRbYear4;
    @BindView(R.id.rb_group_year)
    RadioGroup mRbGroupYear;
    @BindView(R.id.title_term)
    TextView mTitleTerm;
    @BindView(R.id.rb_term1)
    AppCompatRadioButton mRbTerm1;
    @BindView(R.id.rb_term2)
    AppCompatRadioButton mRbTerm2;
    @BindView(R.id.rb_term3)
    AppCompatRadioButton mRbTerm3;

    private User mUser;
    private PreferenceUtil sp;
    private String year;
    private String term;
    private int startYear;
    private String[] itemYears;
    private String[] itemTerms;


    private static final String[] TERM = {"3", "12", "16"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

        initVariables();
        initView();
    }

    private void initVariables() {
        mUser = new User();
        sp = new PreferenceUtil();
        mUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        mUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        getCurYear();
        getYears();
        year = itemYears[0].substring(0, 4);
        term = TERM[0];
    }

    private void getYears() {
        itemYears = new String[4];
        for (int i = 0; i < 4; i++) {
            itemYears[i] = startYear + "-" + (++startYear) + "学年";
        }
        //初始化
        startYear -= 4;
    }

    //获取当前的学年
    private void getCurYear() {
        String start = mUser.getSid().substring(0, 4);
        startYear = Integer.valueOf(start);
    }

    private void initView() {
        setTitle("成绩查询");
        mTvYear1.setText(itemYears[0]);
        mTvYear2.setText(itemYears[1]);
        mTvYear3.setText(itemYears[2]);
        mTvYear4.setText(itemYears[3]);
    }


    @Override
    public void initToolbar() {
        super.initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            mFragmentLayout.setClickable(false);
        }
        super.onBackPressed();
    }

    @Override
    protected boolean canBack() {
        return super.canBack();
    }

    @OnClick(R.id.btn_enter)
    public void onClick() {
        year = getCheckedYear();
        term = getCheckTerm();
        if (NetStatus.isConnected()) {
            ScoreDetailFragment scoreDetailFragment = ScoreDetailFragment.newInstance(year, term);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_layout, scoreDetailFragment);
            ft.addToBackStack(null);
            ft.commit();
            mFragmentLayout.setClickable(true);
        } else {
            ToastUtil.showShort(AppConstants.TIP_CHECK_NET);
        }
    }

    private String getCheckTerm() {
        int pos = 0;
        if (mRbTerm1.isChecked()){
            pos = 0;
        }else if (mRbTerm2.isChecked()){
            pos = 1;
        }else if (mRbTerm3.isChecked()){
            pos = 2;
        }
        return TERM[pos];
    }

    private String getCheckedYear() {
        int pos = 0;
        if (mRbYear1.isChecked()){
            pos = 0;
        }else if (mRbYear2.isChecked()){
            pos = 1;
        }else if (mRbYear3.isChecked()){
            pos = 2;
        }else if (mRbYear4.isChecked()){
            pos = 3;
        }
        return String.valueOf(startYear + pos);
    }


}

