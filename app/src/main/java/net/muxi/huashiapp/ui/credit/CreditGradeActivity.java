package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.Logger;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditGradeActivity extends ToolbarActivity {

    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.btn_enter)
//    Button mBtnEnter;
//    @BindView(R.id.multi_status_view)
//    MultiStatusView mMultiStatusView;
    private List<Score> mScoresList = new ArrayList<>();
    private CreditGradeAdapter mCreditGradeAdapter;

    private int start;
    private int end;
    private RecyclerView mRecyclerView;
    private LinearLayout mLayoutBtn;
    private Button mBtnEnter;
    private MultiStatusView mMultiStatusView;

    public static void start(Context context, int start, int end) {
        Intent starter = new Intent(context, CreditGradeActivity.class);
        starter.putExtra("start", start);
        starter.putExtra("ending", end);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_grade);
        initView();
        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("ending", 0);
        setTitle(String.format("%d-%d学年", start, end));
        loadCredit(getScoreRequest(start, end));

        mBtnEnter.setOnClickListener(v -> {
            showCreditGradeDialog();
        });
    }

    private void showCreditGradeDialog() {
        float result = calculateResult();
        CreditGradeDialog gradeDialog = CreditGradeDialog.newInstance(result);
        gradeDialog.show(getSupportFragmentManager(), "result");
    }

    private float calculateResult() {
        float sum = 0;
        float credits = 0;
        if (mCreditGradeAdapter.getCheckedList() != null) {
            for (int pos : mCreditGradeAdapter.getCheckedList()) {
                float credit = Float.parseFloat(mScoresList.get(pos).credit);

                if (Character.isDigit(mScoresList.get(pos).grade.charAt(0))) {
                    credits += credit;
                    sum += Float.parseFloat(mScoresList.get(pos).grade) * credit;
                }
            }
            if (credits == 0) {
                return 0;
            }
        }
        return sum / credits;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.credit_grade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_all) {
            if (mCreditGradeAdapter != null) {
                mCreditGradeAdapter.setAllChecked();
                mCreditGradeAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadCredit(Observable<List<Score>>[] listObservable) {
        showLoading();
        Observable<List<Score>> creditObservable = Observable.merge(listObservable,5)
                .flatMap(Observable::from)
                .toList();

        creditObservable
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    CcnuCrawler2.clearCookieStore();
                    return new LoginPresenter().login(UserAccountManager.getInstance().getInfoUser())
                            .flatMap(aBoolean -> creditObservable);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    mScoresList = scores;
                    initRecyclerView();
                    this.hideLoading();
                }, Throwable::printStackTrace, () -> {});
    }


    public Observable<List<Score>>[] getScoreRequest(int start, int end) {
        Observable<List<Score>>[] observables = new Observable[(end - start)];
        for (int i = 0; i < (end - start); i++) {
            observables[i] = CampusFactory.getRetrofitService()
                    .getScores(String.valueOf(start + i), "");
        }
        return observables;
    }

    public void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCreditGradeAdapter = new CreditGradeAdapter(mScoresList);

        Logger.d(mCreditGradeAdapter.getItemCount() + "");
        mRecyclerView.setAdapter(mCreditGradeAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutBtn = findViewById(R.id.layout_btn);
        mBtnEnter = findViewById(R.id.btn_enter);
        mMultiStatusView = findViewById(R.id.multi_status_view);
    }
}
