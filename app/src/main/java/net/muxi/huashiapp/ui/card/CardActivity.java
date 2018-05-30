package net.muxi.huashiapp.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.view.ICardView;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.CardDailyUse;
import com.muxistudio.appcommon.data.CardDataEtp;
import com.muxistudio.appcommon.data.CardSumData;
import com.muxistudio.appcommon.presenter.CardDataPresenter;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.jsbridge.BridgeWebView;
import com.muxistudio.multistatusview.MultiStatusView;
import com.tencent.smtt.sdk.WebSettings;

import net.muxi.huashiapp.R;

import java.util.Date;
import java.util.List;

/**
 * Created by december on 16/7/18.
 */
public class CardActivity extends ToolbarActivity implements ICardView {

    private MultiStatusView mMultiStatusView;
    private TextView mMoney;
    private TextView mTvDate;
    private BridgeWebView mConsumeView;

    private CardDataPresenter mPresenter;
    private CardDailyUse mDailyUse;

    public static void start(Context context) {
        Intent starter = new Intent(context, CardActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initView();
        setTitle("校园卡");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mPresenter = new CardDataPresenter(this);
        mPresenter.setCardView();

        WebSettings settings = mConsumeView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath("data/data/net.muxi.huashiapp/cache");
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mMultiStatusView.setOnRetryListener(v -> {
            showLoading();
            mPresenter = new CardDataPresenter(this);
            mPresenter.setCardView();
        });

    }

    @Override
    public void initView(CardDailyUse dailyUse, CardDataEtp etp) {
        mMultiStatusView.showContent();
        mTvDate.setText("截止" + etp.getModel().getSmtDealdatetimeTxt());
        mMoney.setText(etp.getModel().getBalance());
        mDailyUse = dailyUse;


        CardSumData[] data = new CardSumData[7];
        for (int i = 0; i < data.length; i++) {
            data[i] = new CardSumData(DateUtil.getTheDateInYear(new Date(), i - 6), getDailySum(i));

        }
        Gson gson = new Gson();
        String json = gson.toJson(data);

        Logger.d(json);
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
        List<CardDailyUse.ListBean.DataBean> list=      mDailyUse.getList().get(0).getData();
        if(list.size()==0 || list == null) return Double.valueOf(0);
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).getSmtDealName().equals("消费"))
                if (date.equals(list.get(i).getSmtDealDateTimeTxt().substring(0, 10))) {
                    sum += Double.valueOf(list.get(i).getSmtTransMoney());
                }
        }
        Logger.d(sum + "");
        return sum;

    }

    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        mMoney = findViewById(R.id.money);
        mTvDate = findViewById(R.id.tv_date);
        mConsumeView = findViewById(R.id.consume_view);
    }



}

