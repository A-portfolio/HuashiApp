package net.muxi.huashiapp.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.DetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/7/7.
 */
public class ElectricityDetailActivity extends FragmentActivity   {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ec_tabLayout)
    TabLayout mEcTabLayout;
    @BindView(R.id.ec_viewPager)
    ViewPager mEcViewPager;

    DetailFragment mFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        ButterKnife.bind(this);
        init();
    }

    public void init(){
        mFragment = new DetailFragment();
        MyDetailAdapter myDetailAdapter = new MyDetailAdapter(getSupportFragmentManager(),this);
        mEcViewPager.setAdapter(myDetailAdapter);
        mEcTabLayout.setupWithViewPager(mEcViewPager);
        mEcTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mEcTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

}
