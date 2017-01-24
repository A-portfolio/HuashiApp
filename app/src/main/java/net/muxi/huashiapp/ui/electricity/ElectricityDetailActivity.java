package net.muxi.huashiapp.ui.electricity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.EleRequestData;
import net.muxi.huashiapp.common.data.Electricity;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.util.ZhugeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
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
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PreferenceUtil sp;

    private String mQuery;
    private List<Fragment> detailFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        ButterKnife.bind(this);
        sp = new PreferenceUtil();
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mSwipeRefreshLayout.setEnabled(false);
        mQuery = getIntent().getStringExtra("query");
        setTitle(mQuery);
        EleRequestData eleAirRequest = new EleRequestData();
        eleAirRequest.setDor(mQuery);
        eleAirRequest.setType("air");
        EleRequestData eleLightRequest = new EleRequestData();
        eleLightRequest.setDor(mQuery);
        eleLightRequest.setType("light");
        CampusFactory.getRetrofitService().getElectricity(eleLightRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Response<Electricity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mSwipeRefreshLayout != null){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        ToastUtil.showShort(getString(R.string.tip_err_server));
                    }

                    @Override
                    public void onNext(Response<Electricity> response) {
                        if (mSwipeRefreshLayout != null){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (response.code() == 404){
                            sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
                            ToastUtil.showShort(getString(R.string.ele_room_not_found));
                            Intent intent = new Intent(ElectricityDetailActivity.this,ElectricityActivity.class);
                            startActivity(intent);
                            ElectricityDetailActivity.this.finish();
                        }
                        if (response.code() == 503){
                            ToastUtil.showShort(getString(R.string.tip_school_server_error));
                        }
                        if (response.code() == 200) {
                            ((ElectricityDetailFragment) detailFragments.get(0)).setEleDetail(response.body());
                        }
                    }
                });
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().getElectricity(eleAirRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<Response<Electricity>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mSwipeRefreshLayout != null){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response<Electricity> response) {
                            if (mSwipeRefreshLayout != null){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            if (response.code() == 200) {
                                ((ElectricityDetailFragment) detailFragments.get(1)).setEleDetail(response.body());
                            }
                        }
                    });
        } else {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            ToastUtil.showShort(getString(R.string.tip_check_net));
        }

        init();
    }

    public void init() {
        List<String> titles = new ArrayList<>();
        titles.add("照明");
        titles.add("空调");
        for (int i = 0; i < 2; i++) {
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
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_normal_tab), getResources().getColor(R.color.colorWhite));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onChangeBtnClick() {
        ZhugeUtils.sendEvent("电费查询更换寝室","更换寝室");
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
        Intent intent = new Intent(ElectricityDetailActivity.this, ElectricityActivity.class);
        startActivity(intent);
        this.finish();
    }
}
