package net.muxi.huashiapp.card;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;

import org.joda.time.DateTime;

import java.util.ArrayList;
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


    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.count_view)
    CountView mCountView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        init();
        setupCountView();

        User user = new User();
        PreferenceUtil sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        if (!NetStatus.isConnected()) {
            ToastUtil.showShort(getString(R.string.tip_check_net));
        }
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
                        ToastUtil.showShort(getString(R.string.tip_school_server_error));
                    }

                    @Override
                    public void onNext(List<CardData> cardDatas) {
                        Logger.d("id card");
                        mDate.setText(cardDatas.get(0).getDealDateTime());
                        mMoney.setText(cardDatas.get(0).getOutMoney());
                        mTvUnit.setVisibility(View.VISIBLE);

                    }
                });

    }

    public void init() {
        mToolbar.setTitle("学生卡");
    }

    public void setupCountView() {
        List<DailyData> mDailyDatas = new ArrayList<>();
        DateTime dateTime = new DateTime(new Date());
        DateTime time = dateTime.minusDays(6);
        String day = time.toString("yyyy-MM-dd");
        DateTime dateTime1 = new DateTime(day);
        DateTime time7 = dateTime.minusDays(0);
        String day7 = time7.toString("yyyy-MM-dd");
        DateTime dateTime7 = new DateTime(day7);

        for (int i = 0; i < 6; i++) {
            DailyData dataEntity = new DailyData();
            long millis = (long) (dateTime1.getMillis() + Math.random() * (dateTime7.getMillis() - dateTime1.getMillis()));
            dataEntity.setTime(millis);
            mDailyDatas.add(dataEntity);
        }
    }
}
