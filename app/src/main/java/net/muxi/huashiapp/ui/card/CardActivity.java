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
import com.tencent.smtt.sdk.WebSettings;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.muxi.huashiapp.util.ToastUtil.showShort;

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


    public static void start(Context context) {
        Intent starter = new Intent(context, CardActivity.class);
        context.startActivity(starter);
    }


    private final int itemcount = 7;

    private List<CardData> mCardDatas;
    private float sum;



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

        WebSettings settings = mConsumeView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);


        User user = new User();
        PreferenceUtil sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        if (!NetStatus.isConnected()) {
            showShort(getString(R.string.tip_check_net));
        } else {
            CampusFactory.getRetrofitService()
                    .getCardBalance(user.getSid(), "90", "0", "20")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<List<CardData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showErrorSnackbarShort(getString(R.string.tip_school_server_error));
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<CardData> cardDatas) {
                            Logger.d("id card");
                            mDate.setText("截止" + cardDatas.get(0).getDealDateTime());
                            mMoney.setText(cardDatas.get(0).getOutMoney());
                            mCardDatas = cardDatas;

                            DataToJson[] data = new DataToJson[7];
                            for (int i = 0; i < data.length; i++) {
                                data[i] = new DataToJson(DateUtil.getTheDateInYear(new Date(), -6 + i), getDailySum(i));
                            }

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            Logger.d(json);
                            Logger.d("get json");

//                            mConsumeView.setInitData(data);
//
//                            mConsumeView.loadUrl("http://123.56.41.13:4088");

                        }
                    });
        }
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


    //七天消费额转换为Json数据
    class DataToJson {

        public String time;
        public float sum;

        public DataToJson(String time, float sum) {
            this.time = time;
            this.sum = sum;
        }
    }


}



