package net.muxi.huashiapp.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/7/7.
 */
public class ElectricityDetailActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ec_tabLayout)
    TabLayout mEcTabLayout;
    @BindView(R.id.ec_viewPager)
    ViewPager mEcViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle("元四101");
        init();
    }

    public void init(){
        MyDetailAdapter myDetailAdapter = new MyDetailAdapter(getSupportFragmentManager(),this);
        mEcViewPager.setAdapter(myDetailAdapter);
        mEcTabLayout.setupWithViewPager(mEcViewPager);


    }
}
