package net.muxi.huashiapp.card;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        init();
        User user = new User();
        PreferenceUtil sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        if (!NetStatus.isConnected()){
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
}
