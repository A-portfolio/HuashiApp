package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Space;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;

import net.muxi.huashiapp.R;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditResultActivity extends ToolbarActivity {


    private int start;
    private int end;

    private float zb, zx, tb, tx, th;
    private TextView mTvCreditAll;
    private TextView mTvZb;
    private TextView mZb;
    private TextView mTvTb;
    private TextView mTb;
    private TextView mTvTh;
    private TextView mTh;
    private Space mCenter;
    private TextView mTvZx;
    private TextView mZx;
    private TextView mTvTx;
    private TextView mTx;

    public static void start(Context context, int start, int end) {
        Intent starter = new Intent(context, CreditResultActivity.class);
        starter.putExtra("start", start);
        starter.putExtra("ending", end);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_result);
        initView();

        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("ending", 0);
        setTitle(String.format("%d-%d学年", start, end));
        loadCredit();
    }

    public void loadCredit() {
        loadCredit(getScoreRequest(start, end));
    }

    public void loadCredit(Observable<List<Score>>[] listObservable) {
        showLoading();
        Observable<List<Score>> scoreObservable = Observable.merge(listObservable, 5)
                .flatMap(Observable::from)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        scoreObservable.subscribe(scores -> {
            addCredit(scores);
            float all = zb + zx + tb + tx + th;
            mTvZb.setText(String.valueOf(zb));
            mTvZx.setText(String.valueOf(zx));
            mTvTb.setText(String.valueOf(tb));
            mTvTx.setText(String.valueOf(tx));
            mTvTh.setText(String.valueOf(th));
            mTvCreditAll.setText(String.valueOf(all));
            hideLoading();
        }, throwable -> {
            hideLoading();
            throwable.printStackTrace();
            new LoginPresenter().login(UserAccountManager.getInstance().getInfoUser())
                    .flatMap(aubBoolean -> scoreObservable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(scores -> {
                        addCredit(scores);
                        float all = zb + zx + tb + tx + th;
                        mTvZb.setText(String.valueOf(zb));
                        mTvZx.setText(String.valueOf(zx));
                        mTvTb.setText(String.valueOf(tb));
                        mTvTx.setText(String.valueOf(tx));
                        mTvTh.setText(String.valueOf(th));
                        mTvCreditAll.setText(String.valueOf(all));
                        hideLoading();
                    });
        }, () -> {
        });
    }

    public Observable<List<Score>>[] getScoreRequest(int start, int end) {
        Observable<List<Score>>[] observables = new Observable[(end - start)];
        for (int i = 0; i < (end - start); i++) {
            observables[i] = CampusFactory.getRetrofitService()
                    .getScores(String.valueOf(start + i), "");
        }
        return observables;
    }

    public void addCredit(List<Score> scores) {
        for (Score score : scores) {
            for (int i = 0; i < Constants.CREDIT_CATEGORY.length; i++) {
                if (Constants.CREDIT_CATEGORY[i].equals(score.kcxzmc)) {
                    switch (i) {
                        case 0:
                            zb += Float.parseFloat(score.credit);
                            break;
                        case 1:
                            zx += Float.parseFloat(score.credit);
                            break;
                        case 2:
                            tb += Float.parseFloat(score.credit);
                            break;
                        case 3:
                            tx += Float.parseFloat(score.credit);
                            break;
                        case 4:
                            th += Float.parseFloat(score.credit);
                            break;
                    }
                    break;
                }
            }
        }
    }

    private void initView() {
        mTvCreditAll = findViewById(R.id.tv_credit_all);
        mTvZb = findViewById(R.id.tv_zb);
        mZb = findViewById(R.id.zb);
        mTvTb = findViewById(R.id.tv_tb);
        mTb = findViewById(R.id.tb);
        mTvTh = findViewById(R.id.tv_th);
        mTh = findViewById(R.id.th);
        mCenter = findViewById(R.id.center);
        mTvZx = findViewById(R.id.tv_zx);
        mZx = findViewById(R.id.zx);
        mTvTx = findViewById(R.id.tv_tx);
        mTx = findViewById(R.id.tx);
    }
}
