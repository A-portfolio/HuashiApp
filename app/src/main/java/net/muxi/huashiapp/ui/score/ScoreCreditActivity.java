package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.adapter.ScoreCreditPagerAdapter;
import net.muxi.huashiapp.ui.score.fragments.CurCreditFragment;
import net.muxi.huashiapp.ui.score.fragments.ScoreFragment;

import java.util.ArrayList;
import java.util.List;

public class ScoreCreditActivity extends AppCompatActivity {

    //有两种类型 一种是算查学分绩 另一种是 看自己的学分 刚进入的界面是查算学分绩
    public static final int SCORE_CREDIT = -1, CUR_CREDIT = -2;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ScoreCreditPagerAdapter mPagerAdapter;

    private ScoreFragment mScoreFragment;
    //mCurCreditFragment means the current credit
    private CurCreditFragment mCurCreditFragment;

    private List<Fragment> fragments = new ArrayList<>();


    public static void start(Context context) {
        context.startActivity(new Intent(context,ScoreActivity.class));
    }


    private void initView(){
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        List<String> titles = new ArrayList<>();
        titles.add("查算学分绩");
        titles.add("已修学分");


        mTabLayout.addTab(mTabLayout.newTab().setText("查算学分绩"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已经修学分"));

        mTabLayout.setupWithViewPager(mViewPager);

        if(mScoreFragment == null){
            mScoreFragment = ScoreFragment.newInstance(SCORE_CREDIT);
        }

        if(mCurCreditFragment == null){
            mCurCreditFragment = CurCreditFragment.newInstance(CUR_CREDIT);
        }

        fragments.add(mScoreFragment);
        fragments.add(mCurCreditFragment);

        mPagerAdapter = new ScoreCreditPagerAdapter(getSupportFragmentManager(),fragments,
                titles);

//        todo add mobclick agents


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_credit);

        initView();
    }
}
