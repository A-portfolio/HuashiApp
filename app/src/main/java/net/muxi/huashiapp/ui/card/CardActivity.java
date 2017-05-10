package net.muxi.huashiapp.ui.card;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.jsbridge.BridgeWebView;
import com.muxistudio.multistatusview.MultiStatusView;
import com.tencent.smtt.sdk.WebSettings;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.CardSumData;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/18.
 */
public class CardActivity extends ToolbarActivity {

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


    private final int itemcount = 7;

    private List<CardData> mCardDatas;
    private float sum;

    private PreferenceUtil sp;


    private static final int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        setTitle("校园卡");

        if (!isStorgePermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

//        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PHONE_STATE);}


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
                        mDate.setText("截止" + cardDatas.get(0).getDealDateTime());
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

                    }
                });

    }

    /**
     * 获取指定日的消费总额
     *
     * @param day 前七天为0，今天为6
     * @return
     */
    private float getDailySum(int day) {
        String date = DateUtil.getTheDateInYear(new Date(), -6 + day);
        Logger.d(date);
        float sum = 0;
        for (int i = 0, size = mCardDatas.size(); i < size; i++) {
            if (mCardDatas.get(i).getDealTypeName().equals("消费"))
                if (date.equals(mCardDatas.get(i).getDealDateTime().substring(0, 10))) {
                    sum += Float.valueOf(mCardDatas.get(i).getTransMoney());
                }
        }
        Logger.d(sum + "");
        return sum;

    }

    public boolean isStorgePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}



