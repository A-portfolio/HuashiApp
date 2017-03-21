package net.muxi.huashiapp.ui.electricity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.EleRequestData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/7.
 */
public class ElectricityDetailActivity extends ToolbarActivity {


    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.pay_hint)
    TextView mPayHint;

    public static void start(Context context, String query) {
        Intent starter = new Intent(context, ElectricityDetailActivity.class);
        starter.putExtra("query", query);
        context.startActivity(starter);
    }


    private static final String PAY_HINT = "电费不足？查看如何微信缴费";

    private PreferenceUtil sp;

    private CardView mCardView;

    private String mQuery;
    private List<Fragment> detailFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        ButterKnife.bind(this);



//        mToolbar.setTitle("查询结果");
        setTitle("查询结果");

        setFontType(PAY_HINT);

        init();
        sp = new PreferenceUtil();


        mQuery = getIntent().getStringExtra("query");
        setTitleColor(Color.BLACK);
        EleRequestData eleAirRequest = new EleRequestData();
        eleAirRequest.setDor(mQuery);
        eleAirRequest.setType("air");
        EleRequestData eleLightRequest = new EleRequestData();
        eleLightRequest.setDor(mQuery);
        eleLightRequest.setType("light");
        CampusFactory.getRetrofitService().getElectricity(eleLightRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(electricityResponse -> {
                    if (electricityResponse.code() == 404) {
                        sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
                        showSnackbarShort(getString(R.string.ele_room_not_found));
                        Intent intent = new Intent(ElectricityDetailActivity.this, ElectricityActivity.class);
                        startActivity(intent);
                        ElectricityDetailActivity.this.finish();
                    }
                    if (electricityResponse.code() == 503) {
                        showSnackbarShort(getString(R.string.tip_school_server_error));
                    }
                    if (electricityResponse.code() == 200) {
//                        ((ElectricityDetailFragment) detailFragments.get(0)).setCardColor(0);
                        ((ElectricityDetailFragment) detailFragments.get(0)).setEleDetail(electricityResponse.body());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                    showErrorSnackbarShort(getString(R.string.tip_check_net));
                }, () -> {
                    hideLoading();
                });
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().getElectricity(eleAirRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(electricityResponse -> {
                        if (electricityResponse.code() == 200) {
//                            ((ElectricityDetailFragment) detailFragments.get(1)).setCardColor(1);
                            ((ElectricityDetailFragment) detailFragments.get(1)).setEleDetail(electricityResponse.body());
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        showErrorSnackbarShort(getString(R.string.tip_check_net));
                    }, () -> {
                        hideLoading();
                    });
        } else {
            showErrorSnackbarShort(getString(R.string.tip_check_net));
        }
    }

    public void init() {
        List<String> titles = new ArrayList<>();
        titles.add("照明");
        titles.add("空调");
        for (int i = 0; i < 2; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        detailFragments = new ArrayList<>();
        detailFragments.add(ElectricityDetailFragment.newInstance(0));
        detailFragments.add(ElectricityDetailFragment.newInstance(1));




        MyDetailAdapter myDetailAdapter = new MyDetailAdapter(getSupportFragmentManager(), detailFragments, titles);
        mViewPager.setAdapter(myDetailAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_normal_tab), getResources().getColor(R.color.colorAccent));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_electricity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_change_room) {
            ZhugeUtils.sendEvent("电费查询更换寝室", "更换寝室");
            PreferenceUtil sp = new PreferenceUtil();
            sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
            Intent intent = new Intent(ElectricityDetailActivity.this, ElectricityActivity.class);
            startActivity(intent);
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

    //设置特定位置字体变换颜色
    private void setFontType(String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 5, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPayHint.setText(spannableString);
    }

    @OnClick(R.id.pay_hint)
    public void onClick() {
        ElectricityPayHintView view = new ElectricityPayHintView(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
        view.startAnimation(animation);
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
