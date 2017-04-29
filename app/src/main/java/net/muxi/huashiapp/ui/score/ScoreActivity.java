package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreActivity extends ToolbarActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ScoresAdapter mScoresAdapter;
    private List<Scores> mScoresList = new ArrayList<>();
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
        if (!term.equals("0")) {
            setTitle(String.format("%s-%d第%d学期", year, Integer.parseInt(year) + 1,
                    Arrays.binarySearch(Constants.TERMS, term)));
        }else {
            setTitle(String.format("%s-%d", year, Integer.parseInt(year) + 1));
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScoresAdapter = new ScoresAdapter(mScoresList, mDetailScores);
        mRecyclerView.setAdapter(mScoresAdapter);

        showLoading();
        loadGrade(term);
    }

    private void loadGrade(String term) {
        Logger.d("term" + term);
        if (term.equals("0")) {
            String[] terms = {"3", "12", "16"};
            for (String t : terms) {
                loadGrade(t);
            }
            return;
        }

        Observable.create(new Observable.OnSubscribe<InfoCookie>() {

            @Override
            public void call(Subscriber<? super InfoCookie> subscriber) {
                subscriber.onStart();
                InfoCookie cookie = CcnuCrawler.getInfoCookie();
                subscriber.onNext(cookie);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cookie -> {
                    loadGradeWithCookie(cookie);
                },throwable -> throwable.printStackTrace());
//        CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr(App.sUser), year,
//                term)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(scores -> {
//                    Logger.d("get score");
//                    hideLoading();
//                    mScoresList.addAll(scores);
//                    mScoresAdapter.notifyDataSetChanged();
//                }, throwable -> throwable.printStackTrace());

//        Observable.zip(CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr
// (App.sUser),year,"3"),
//                CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr(App
// .sUser),year,"12"),((scores, scores2) -> {
//                    mScoresList.addAll(scores);
//                    mScoresList.addAll(scores2);
//                    return mScoresList;
//                }))
    }

    private void loadGradeWithCookie(InfoCookie cookie) {
//        CampusFactory.getRetrofitService().getScores()
    }

//    public Observable<Scores> get
}

