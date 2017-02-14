package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditResultActivity extends ToolbarActivity {

    @BindView(R.id.tv_credit_all)
    TextView mTvCreditAll;
    @BindView(R.id.tv_zb)
    TextView mTvZb;
    @BindView(R.id.tv_tb)
    TextView mTvTb;
    @BindView(R.id.tv_th)
    TextView mTvTh;
    @BindView(R.id.tv_zx)
    TextView mTvZx;
    @BindView(R.id.tv_tx)
    TextView mTvTx;

    private int start;
    private int end;

    private int length;

    private float zb, zx, tb, tx, th;
    private int counter = 0;

    public static void start(Context context, int start, int end) {
        Intent starter = new Intent(context, CreditResultActivity.class);
        starter.putExtra("start", start);
        starter.putExtra("end", end);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_result);
        ButterKnife.bind(this);

        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("end", 0);
        length = (end - start) * 2;
        setTitle(String.format("%d-%d学年", start, end));
        loadCredit();
    }

    public void loadCredit() {
        for (int i = 0; i < length; i++) {
            int term = 3;
            if (i % 2 == 1) {
                term = 12;
            }
            loadCredit(String.valueOf(start + i / 2), String.valueOf(term), 0);
        }

    }

    public void loadCredit(String year, String term, final int reloadNum) {
        if (reloadNum > 4){
            ToastUtil.showShort("学校服务器异常");
            return;
        }
        CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr(App.sUser),
                String.valueOf(year), String.valueOf(term))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    counter++;
                    addCredit(scores);
                    if (counter == length) {
                        float all = zb + zx + tb + tx + th;
                        mTvZb.setText(String.valueOf(zb));
                        mTvZx.setText(String.valueOf(zx));
                        mTvTb.setText(String.valueOf(tb));
                        mTvTx.setText(String.valueOf(tx));
                        mTvTh.setText(String.valueOf(th));
                        mTvCreditAll.setText(String.valueOf(all));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    loadCredit(year, term, reloadNum + 1);
                });
    }

    public void addCredit(List<Scores> scores) {
        for (Scores score : scores) {
            for (int i = 0; i < Constants.CREDIT_CATEGORY.length; i++) {
                if (Constants.CREDIT_CATEGORY[i].equals(score.getKcxzmc())) {
                    switch (i) {
                        case 0:
                            zb += Float.parseFloat(score.getCredit());
                            break;
                        case 1:
                            zx += Float.parseFloat(score.getCredit());
                            break;
                        case 2:
                            tb += Float.parseFloat(score.getCredit());
                            break;
                        case 3:
                            tx += Float.parseFloat(score.getCredit());
                            break;
                        case 4:
                            th += Float.parseFloat(score.getCredit());
                            break;
                    }
                    break;
                }
            }
        }
    }

}
