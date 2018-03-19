package net.muxi.huashiapp.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.CardData;
import com.muxistudio.appcommon.data.CardSumData;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.jsbridge.BridgeHandler;
import com.muxistudio.jsbridge.BridgeWebView;
import com.muxistudio.jsbridge.CallbackFunc;
import com.muxistudio.multistatusview.MultiStatusView;
import com.tencent.smtt.sdk.WebSettings;

import net.muxi.huashiapp.R;

import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/18.
 */
public class CardActivity extends ToolbarActivity {

    private MultiStatusView mMultiStatusView;
    private RelativeLayout mCoordinatorLayout;
    private TextView mMoneySign;
    private TextView mMoney;
    private TextView mTxt2;
    private TextView mTvDate;
    private View mDivider;
    private BridgeWebView mConsumeView;

    public static void start(Context context) {
        Intent starter = new Intent(context, CardActivity.class);
        context.startActivity(starter);
    }


    private final int itemcount = 7;
    private List<CardData> mCardDatas;
    private float sum;
    private PreferenceUtil sp;
    private static final int REQUEST_READ_PHONE_STATE = 1;

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
        loadDatas();

        WebSettings settings = mConsumeView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath("data/data/net.muxi.huashiapp/cache");
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mMultiStatusView.setOnRetryListener(v -> {
            showLoading();
            loadDatas();
        });


    }


    private void loadDatas() {
        User user = new User();
        sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        CampusFactory.getRetrofitService()
                .getCardBalance(user.getSid(), "90", "0", "60")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<CardData>>() {
                    @Override
                    public void onCompleted() {
                        hideLoading();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mMultiStatusView.showNetError();
                        hideLoading();

                    }

                    @Override
                    public void onNext(List<CardData> cardDatas) {
                        Logger.d("id card");
                        mMultiStatusView.showContent();
                        mTvDate.setText("截止" + cardDatas.get(0).getDealDateTime());
                        mMoney.setText(cardDatas.get(0).getOutMoney());
                        mCardDatas = cardDatas;

                        CardSumData[] data = new CardSumData[7];
                        for (int i = 0; i < data.length; i++) {
                            data[i] = new CardSumData(DateUtil.getTheDateInYear(new Date(), -6 + i), getDailySum(i));
                        }

                        Gson gson = new Gson();
                        String json = gson.toJson(data);
                        Logger.d(json);
                        Logger.d("get json");

                        mConsumeView.setInitData(data);
                        mConsumeView.loadUrl("http://123.56.41.13:4088");

                        //event ? 事件名?
                        mConsumeView.register("fafafafa", new BridgeHandler() {
                            @Override
                            public void handle(String s, CallbackFunc callbackFunc) {

                            }
                        });

                    }
                });

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
        for (int i = 0, size = mCardDatas.size(); i < size; i++) {
            if (mCardDatas.get(i).getDealTypeName().equals("消费"))
                if (date.equals(mCardDatas.get(i).getDealDateTime().substring(0, 10))) {
                    sum += Double.valueOf(mCardDatas.get(i).getTransMoney());
                }
        }
        Logger.d(sum + "");
        return sum;

    }

    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mMoneySign = findViewById(R.id.money_sign);
        mMoney = findViewById(R.id.money);
        mTxt2 = findViewById(R.id.txt2);
        mTvDate = findViewById(R.id.tv_date);
        mDivider = findViewById(R.id.divider);
        mConsumeView = findViewById(R.id.consume_view);
    }
}



