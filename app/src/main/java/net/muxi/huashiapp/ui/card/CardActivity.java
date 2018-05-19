package net.muxi.huashiapp.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.jsbridge.BridgeWebView;
import com.muxistudio.multistatusview.MultiStatusView;
import com.tencent.smtt.sdk.WebSettings;

import net.muxi.huashiapp.CardDataPresenter;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CardDailyUse;
import net.muxi.huashiapp.common.data.CardDataEtp;
import net.muxi.huashiapp.common.data.CardSumData;
import net.muxi.huashiapp.common.data.ICardView;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/7/18.
 */
public class CardActivity extends ToolbarActivity implements ICardView {

    @BindView(R.id.tv_date)
    TextView mDate;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.consume_view)
    BridgeWebView mConsumeView;
    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;


    public static void start(Context context) {
        Intent starter = new Intent(context, CardActivity.class);
        context.startActivity(starter);
    }

    private CardDataPresenter mPresenter;
    private CardDailyUse mDailyUse;
    private float sum;
    private PreferenceUtil sp;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        setTitle("校园卡");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mPresenter = new CardDataPresenter(this);
        mPresenter.getData();

        WebSettings settings = mConsumeView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath("data/data/net.muxi.huashiapp/cache");
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mMultiStatusView.setOnRetryListener(v -> {
            showLoading();
            mPresenter = new CardDataPresenter(this);
            mPresenter.getData();
        });


    }


    @Override
    public void initView(CardDailyUse dailyUse, CardDataEtp etp) {
        mMultiStatusView.showContent();
        mDate.setText("截止" + etp.getModel().getSmtDealdatetimeTxt());
        mMoney.setText(etp.getModel().getBalance());
        mDailyUse = dailyUse;


        CardSumData[] data = new CardSumData[7];
        for (int i = 0; i < data.length; i++) {
            data[i] = new CardSumData(DateUtil.getTheDateInYear(new Date(), i - 6), getDailySum(i));

        }
        Gson gson = new Gson();
        String json = gson.toJson(data);

        mConsumeView.setInitData(data);
        mConsumeView.loadUrl("http://123.56.41.13:4088");
    }



    /**
     * 获取指定日的消费总额
     *
     * @param day 前七天为0，今天为6
     * @return
     */
    private Double getDailySum(int day) {
        String date = DateUtil.getTheDateInYear(new Date(), -6 + day);
        Logger.d(date);
        double sum = 0;
        List<CardDailyUse.ListBean.DataBean> list=mDailyUse.getList().get(0).getData();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).getSmtDealName().equals("消费"))
                if (date.equals(list.get(i).getSmtDealName().substring(0, 10))) {
                    sum += Double.valueOf(list.get(i).getSmtTransMoney());
                }
        }
        Logger.d(sum + "");
        return sum;

    }


}


