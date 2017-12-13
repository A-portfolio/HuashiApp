package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.Score;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler2;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;

    private ScoresAdapter mScoresAdapter;
    private List<Score> mScoresList = new ArrayList<>();
    private List<DetailScores> mDetailScores = new ArrayList<>();

    private String year;
    private String term;

    public static void start(Context context, String year, String term) {
        Intent starter = new Intent(context, ScoreActivity.class);
        starter.putExtra("year", year);
        starter.putExtra("term", term);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        year = getIntent().getStringExtra("year");
        term = getIntent().getStringExtra("term");
        if (term == null){
            term = "0";
        }
        if (!term.equals("0")) {
            setTitle(String.format("%s-%d学年第%d学期", year, Integer.parseInt(year) + 1,
                    getTermOrder(Constants.TERMS, term)));
        } else {
            setTitle(String.format("%s-%d学年", year, Integer.parseInt(year) + 1));
        }

        mMultiStatusView.setOnRetryListener(v -> loadGrade(term));
        loadGrade(term);
    }

    private void loadGrade(String term) {
        showLoading();
        Logger.d("term" + term);
        if (term.equals("0")) {
            CampusFactory.getRetrofitService().getScores(year,"")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(scores -> renderScoreList(scores),
                            throwable -> {
                                throwable.printStackTrace();
                                mMultiStatusView.showNetError();
                                hideLoading();
                                int code = ((HttpException)throwable).code();
                        if(((HttpException)throwable).code()==403){
                            String sid = PreferenceUtil.getString(PreferenceUtil.STUDENT_ID);
                            String pwd = PreferenceUtil.getString(PreferenceUtil.STUDENT_PWD);
                            try {
                                CcnuCrawler2.performLogin(sid,pwd);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            showErrorSnackbarShort(R.string.tip_refresh_retry);
                        }
                            },
                            () -> hideLoading());
            return;
        }

        CampusFactory.getRetrofitService().getScores(year, term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> renderScoreList(scores),
                        throwable -> {
                            throwable.printStackTrace();
                            mMultiStatusView.showNetError();
                            CcnuCrawler.clearCookieStore();
                            CcnuCrawler.initCrawler();
                            hideLoading();
                        },
                        () -> hideLoading());
    }

    private void renderScoreList(List<Score> scores) {
        if (scores == null || scores.size() == 0) {
            mMultiStatusView.showEmpty();
            return;
        }
        mMultiStatusView.showContent();
        mScoresList.addAll(scores);
        try {
            mScoresAdapter = new ScoresAdapter(mScoresList);
        } catch (Exception e) {
            e.printStackTrace();
            mScoresAdapter = new ScoresAdapter(new ArrayList<>());
        }
        RecyclerView recyclerView = (RecyclerView) mMultiStatusView
                .getContentView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreActivity
                .this));
        recyclerView.setAdapter(mScoresAdapter);
    }

    public int getTermOrder(String[] termStrings, String term) {
        for (int i = 0; i < termStrings.length; i++) {
            if (termStrings[i].equals(term)) {
                return i + 1;
            }
        }
        return 1;
    }

}

