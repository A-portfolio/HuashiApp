package net.muxi.huashiapp.ui.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.tv_year5)
    TextView mTvYear5;
    @BindView(R.id.rb_year5)
    AppCompatRadioButton mRbYear5;
    @BindView(R.id.rg_term)
    RadioGroup mRgTerm;

    private User mUser;
    private PreferenceUtil sp;
    private String year;
    private String term;
    private int startYear;
    private String[] itemYears;
    private String[] itemTerms;

    private static final int YEAR_COUNT = 5;
    private static final int TERM_COUNT = 3;

    private static final String[] TERM = {"3", "12", "16"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
//        checkUser();
        initVariables();
        initView();
    }

    /**
     * 查询成绩前检测用户是否改了密码
     */
    private void checkUser() {
        CampusFactory.getRetrofitService().mainLogin(Base64Util.createBaseStr(App.sUser))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Response<VerifyResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<VerifyResponse> verifyResponseResponse) {
//                        if (verifyResponseResponse.code() == 403){
//                            ToastUtil.showShort(App.sContext.getString(R.string.tip_login_again));
//                        }
                    }
                });
    }

    private void setTitle(String year, String term) {
        String title = "";
        switch (term) {
            case "3":
                title += year + "年秋";
                break;
            case "12":
                title += (Integer.valueOf(year) + 1) + "年春";
                break;
            case "16":
                title += (Integer.valueOf(year) + 1) + "年夏";
                break;
        }
        setTitle(title);
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
        itemYears = new String[YEAR_COUNT];
        for (int i = 0; i < YEAR_COUNT; i++) {
            itemYears[i] = startYear + "-" + (++startYear) + "学年";
        }
        //初始化
        startYear -= YEAR_COUNT;
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
        mTvYear5.setText(itemYears[4]);
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mFragmentLayout.setClickable(false);
            setTitle("成绩查询");
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
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_layout, scoreDetailFragment);
            ft.addToBackStack(null);
            ft.commit();
            mFragmentLayout.setClickable(true);
            setTitle(year, term);
            ZhugeUtils.sendEvent("查询成绩", "查询成绩");
        } else {
            ToastUtil.showShort(Constants.TIP_CHECK_NET);
        }
    }

    private String getCheckTerm() {
        int pos = 0;
        if (mRbTerm1.isChecked()) {
            pos = 0;
        } else if (mRbTerm2.isChecked()) {
            pos = 1;
        } else if (mRbTerm3.isChecked()) {
            pos = 2;
        }
        return TERM[pos];
    }

    private String getCheckedYear() {
        int pos = 0;
        if (mRbYear1.isChecked()) {
            pos = 0;
        } else if (mRbYear2.isChecked()) {
            pos = 1;
        } else if (mRbYear3.isChecked()) {
            pos = 2;
        } else if (mRbYear4.isChecked()) {
            pos = 3;
        }else if (mRbYear5.isChecked()){
            pos = 4;
        }
        return String.valueOf(startYear + pos);
    }



}

