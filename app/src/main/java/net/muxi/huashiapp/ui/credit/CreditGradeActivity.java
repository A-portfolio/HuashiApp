package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Score;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler2;
import net.muxi.huashiapp.ui.login.LoginPresenter;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditGradeActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private List<Score> mScoresList = new ArrayList<>();
    private CreditGradeAdapter mCreditGradeAdapter;

    private int start;
    private int end;

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
        ButterKnife.bind(this);
        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("ending", 0);
        setTitle(String.format("%d-%d学年", start, end));
        loadCredit(getScoreRequest(start,end));
        mBtnEnter.setOnClickListener(v ->{
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
        if(mCreditGradeAdapter.getCheckedList()!=null) {
            for (int pos : mCreditGradeAdapter.getCheckedList()) {
                float credit = Float.parseFloat(mScoresList.get(pos).credit);
                credits += credit;
                sum += Float.parseFloat(mScoresList.get(pos).grade) * credit;
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
        Observable.merge(listObservable,5)
                .flatMap(Observable::from)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    mScoresList = scores;
                    initRecyclerView();
                },throwable -> {
                    throwable.printStackTrace();
                    CcnuCrawler2.clearCookieStore();
                    LoginPresenter presenter = new LoginPresenter();
                    presenter.login(App.sUser).subscribe(s->
                    {App.sLoginStatus = (String)s;
                        PreferenceUtil.saveString(PreferenceUtil.LOGIN_STATUS,(String)s);},
                            thowable ->{},()->{});
                        hideLoading();
                        showErrorSnackbarShort(R.string.tip_school_server_error);
                },() -> hideLoading());
    }

    public Observable<List<Score>>[] getScoreRequest(int start,int end){
        Observable<List<Score>>[] observables = new Observable[(end - start)];
        for (int i = 0;i < (end - start);i++){
            observables[i] = CampusFactory.getRetrofitService()
                    .getScores(String.valueOf(start + i ), "");
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
}
