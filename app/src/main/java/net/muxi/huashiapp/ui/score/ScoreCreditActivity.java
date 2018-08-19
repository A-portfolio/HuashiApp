package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.appbase.ToolbarActivity;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.adapter.ScoreCreditPagerAdapter;
import net.muxi.huashiapp.ui.score.fragments.CurCreditFragment;
import net.muxi.huashiapp.ui.score.fragments.ScoreFragment;

import java.util.ArrayList;
import java.util.List;

public class ScoreCreditActivity extends ToolbarActivity {

    //有两种类型 一种是算查学分绩 另一种是 看自己的学分 刚进入的界面是查算学分绩
    public static final int SCORE_CREDIT = -1, CUR_CREDIT = -2;

    private ScoreFragment mScoreFragment;
    //mCurCreditFragment means the current credit
    private CurCreditFragment mCurCreditFragment;

    private List<Fragment> fragments = new ArrayList<>();


    public static void start(Context context) {
        context.startActivity(new Intent(context,ScoreCreditActivity.class));
    }


    private void initView(){
        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        ViewPager mViewPager = findViewById(R.id.view_pager);

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

        ScoreCreditPagerAdapter mPagerAdapter = new ScoreCreditPagerAdapter(getSupportFragmentManager(), fragments,
                titles);

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int curPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position > curPosition){
                    //slide to the right fragment
                    curPosition = position;
                    if(mCurCreditFragment.getCredit().isEmpty()) {
                        mCurCreditFragment.loadCredit();
                    }
                }else if (position < curPosition){
                    curPosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        todo add mobclick agents


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_credit);
        setTitle("查算学分绩");
        initView();
    }
}
