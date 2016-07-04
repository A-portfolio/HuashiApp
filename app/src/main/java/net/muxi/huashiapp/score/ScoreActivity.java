package net.muxi.huashiapp.score;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.util.Logger;
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
    @BindView(R.id.spinner_year)
    Spinner mSpinnerYear;
    @BindView(R.id.spinner_term)
    Spinner mSpinnerTerm;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.fragment_layout)
    FrameLayout mFragmentLayout;

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
//        ScoreSearchAdapter adapter = new ScoreSearchAdapter(this,itemYears);
//        mSpinnerYear.setAdapter(adapter);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemYears);
        mSpinnerYear.setAdapter(adapter);
        mSpinnerYear.setPrompt("" + startYear);
        mSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerYear.setPrompt(itemYears[position]);
                year = itemYears[position].substring(0, 4);
                Logger.d("year" + position);
                Logger.d(year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemTerms = App.getContext().getResources().getStringArray(R.array.score_term);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemTerms);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.score_term,R.layout.item_score_search);
        mSpinnerTerm.setAdapter(arrayAdapter);
        mSpinnerTerm.setPrompt(itemTerms[0]);
        mSpinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerTerm.setPrompt(itemTerms[position]);
                Logger.d("term" + position);
                term = TERM[position];
                Logger.d(term);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (getFragmentManager().getBackStackEntryCount() > 0){
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
}

