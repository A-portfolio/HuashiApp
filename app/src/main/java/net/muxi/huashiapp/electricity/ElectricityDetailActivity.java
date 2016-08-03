package net.muxi.huashiapp.electricity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.EleRequestData;
import net.muxi.huashiapp.common.data.Electricity;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/7.
 */
public class ElectricityDetailActivity extends ToolbarActivity implements ElectricityDetailFragment.OnChangeBtnClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private Electricity mEleAirData;
    private Electricity mEleLightData;

    private String mQuery;
    private List<Fragment> detailFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        ButterKnife.bind(this);
        mQuery = getIntent().getStringExtra("query");
        EleRequestData eleAirRequest = new EleRequestData();
        eleAirRequest.setDor(mQuery);
        eleAirRequest.setType("air");
        EleRequestData eleLightRequest = new EleRequestData();
        eleLightRequest.setDor(mQuery);
        eleLightRequest.setType("light");
        CampusFactory.getRetrofitService().getElectricity(eleLightRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Electricity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Electricity electricity) {
                        ((ElectricityDetailFragment) detailFragments.get(0)).setEleDetail(electricity);

                    }
                });
        CampusFactory.getRetrofitService().getElectricity(eleAirRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Electricity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Electricity electricity) {
                        ((ElectricityDetailFragment) detailFragments.get(1)).setEleDetail(electricity);
                    }
                });

        init();
    }

    public void init() {
        List<String> titles = new ArrayList<>();
        titles.add("照明");
        titles.add("空调");
        for (int i = 0; i < 2; i ++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        detailFragments = new ArrayList<>();
        detailFragments.add(new ElectricityDetailFragment());
        detailFragments.add(new ElectricityDetailFragment());
        ((ElectricityDetailFragment) detailFragments.get(0)).setOnChangeBtnClickListener(this);
        ((ElectricityDetailFragment) detailFragments.get(1)).setOnChangeBtnClickListener(this);
        MyDetailAdapter myDetailAdapter = new MyDetailAdapter(getSupportFragmentManager(), detailFragments, titles);
        mViewPager.setAdapter(myDetailAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_normal_tab),getResources().getColor(R.color.colorWhite));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onChangeBtnClick() {
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
        Intent intent = new Intent(ElectricityDetailActivity.this, ElectricityActivity.class);
        startActivity(intent);
        this.finish();
    }
}
