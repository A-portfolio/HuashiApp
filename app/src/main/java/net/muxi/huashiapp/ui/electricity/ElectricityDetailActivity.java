package net.muxi.huashiapp.ui.electricity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.EleRequestData;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.multistatusview.MultiStatusView;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/7.
 */
public class ElectricityDetailActivity extends ToolbarActivity {

    private RelativeLayout mEleDetailLayout;
    private TabLayout mTabLayout;
    private MultiStatusView mMultiStatusView;
    private ViewPager mViewPager;
    private TextView mPayHint;

    private LoadingDialog mLoadingDialog ;

    public static void start(Context context, String query) {
        Intent starter = new Intent(context, ElectricityDetailActivity.class);
        starter.putExtra("query", query);
        context.startActivity(starter);
    }

    private static final String PAY_HINT = "电费不足？查看如何微信缴费";
    private String mQuery;
    private List<Fragment> detailFragments;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_detail);
        initView();
        setTitle("查询结果");
        init();
        PreferenceUtil sp = new PreferenceUtil();
        mQuery = getIntent().getStringExtra("query");
        mMultiStatusView.setOnRetryListener(v -> {
            mLoadingDialog = showLoading(CommonTextUtils.generateRandomApartmentText());
            loadData();
        });
        setFontType(PAY_HINT);
        loadData();

    }

    private void loadData() {
        EleRequestData eleAirRequest = new EleRequestData();
        eleAirRequest.setDor(mQuery);
        eleAirRequest.setType("air");
        EleRequestData eleLightRequest = new EleRequestData();
        eleLightRequest.setDor(mQuery);
        eleLightRequest.setType("light");
        Subscription subscriptionEle = CampusFactory.getRetrofitService().getElectricity(eleLightRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(electricityResponse -> {
                    if (electricityResponse.code() == 200) {
//                        ((ElectricityDetailFragment) detailFragments.get(0)).setCardColor(0);
                        mMultiStatusView.showContent();
                        ((ElectricityDetailFragment) detailFragments.get(0)).setEleDetail(electricityResponse.body());
                    }
                    if (electricityResponse.code() == 503) {
                        showSnackbarShort(getString(R.string.tip_school_server_error));
                    }
                    if (electricityResponse.code() == 500) {
                        mMultiStatusView.showNetError();
                        hideLoading();
                    }
                    //onError()
                    //throwable 是里面的一个参数
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showNetError();
                    hideLoading();

                    //onComplete()
                }, this::hideLoading);

        Subscription subscriptionAir = CampusFactory.getRetrofitService().getElectricity(eleAirRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(electricityResponse -> {
                    if (electricityResponse.code() == 200) {
//                            ((ElectricityDetailFragment) detailFragments.get(1)).setCardColor(1);
                        mMultiStatusView.showContent();
                        ((ElectricityDetailFragment) detailFragments.get(1)).setEleDetail(electricityResponse.body());
                    }
                    if (electricityResponse.code() == 500) {
                        mMultiStatusView.showNetError();
                        hideLoading();
                    }
                }, (Throwable throwable) -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showNetError();
                    hideLoading();
                }, this::hideLoading);

        mLoadingDialog.setOnSubscriptionCanceledListener(
            () -> {
              if(!subscriptionAir.isUnsubscribed() || !subscriptionEle.isUnsubscribed()){
                subscriptionAir.unsubscribe();
                subscriptionEle.unsubscribe();
              }
            });
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

            MobclickAgent.onEvent(this, "ele_fee_change_dom_query");
            PreferenceUtil sp = new PreferenceUtil();
            PreferenceUtil.clearString(PreferenceUtil.ELE_QUERY_STRING);
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

    public void onClick() {
        ElectricityPayHintView view = new ElectricityPayHintView(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
        view.startAnimation(animation);
        mEleDetailLayout.addView(view);

    }


    @Override
    public void onBackPressed() {
        ElectricityPayHintView view = new ElectricityPayHintView(this);
        if (getWindow().getDecorView().equals(view)) {
            view.getAnimation().cancel();
            mEleDetailLayout.removeView(view);
        } else {
            super.onBackPressed();
        }

    }

    private void initView() {
        mEleDetailLayout = findViewById(R.id.ele_detail_layout);
        AppBarLayout mAppBar = findViewById(R.id.app_bar);
        mTabLayout = findViewById(R.id.tabLayout);
        mMultiStatusView = findViewById(R.id.multi_status_view);
        mViewPager = findViewById(R.id.viewPager);
        mPayHint = findViewById(R.id.pay_hint);
        mPayHint.setOnClickListener(v -> onClick());
    }
}
