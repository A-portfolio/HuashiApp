package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;
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

    private List<Scores> mScoresList = new ArrayList<>();
    private CreditGradeAdapter mCreditGradeAdapter;

    private int start;
    private int end;

    private int counter;

    public static void start(Context context, int start, int end) {
        Intent starter = new Intent(context, CreditGradeActivity.class);
        starter.putExtra("start", start);
        starter.putExtra("end", end);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_grade);
        ButterKnife.bind(this);
        setTitle(String.format("%d-%d学年", start, end));

        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("end", 0);

        for (int i = 0; i < (end - start) * 2; i++) {
            int term = 3;
            if (i % 2 == 1) {
                term = 12;
            }
            loadCredit(String.valueOf(start + i / 2), String.valueOf(term), 0);
        }

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
        for (int pos : mCreditGradeAdapter.getCheckedList()) {
            float credit = Float.parseFloat(mScoresList.get(pos).getCredit());
            credits += credit;
            sum += Float.parseFloat(mScoresList.get(pos).getGrade()) * credit;
        }
        if (credits == 0){
            return 0;
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
            mCreditGradeAdapter.setAllChecked();
            mCreditGradeAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadCredit(String year, String term, final int reloadNum) {
        if (reloadNum > 4) {
            ToastUtil.showShort("学校服务器异常");
            return;
        }
        CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr(App.sUser),
                String.valueOf(year), String.valueOf(term))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    counter++;
                    mScoresList.addAll(scores);

                    Logger.d(counter + "");
                    if (counter == (end - start) * 2) {
                        initRecyclerView();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    loadCredit(year, term, reloadNum + 1);
                });
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
