package net.muxi.huashiapp.ui.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreDisplayActivity extends ToolbarActivity {

    private ScoresAdapter mScoresAdapter;
    private List<Score> mScoresList = new ArrayList<>();
    private String year;
    private String term;
    private MultiStatusView mMultiStatusView;

    private List<String> yearParams = new ArrayList<>();
    private List<String> termParams = new ArrayList<>();

    public static void start(Context context, String year, String term) {
        Intent starter = new Intent(context, ScoreDisplayActivity.class);
        starter.putExtra("year", year);
        starter.putExtra("term", term);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //获取解析 year term params
        getParams();
        initView();
        loadGrade();
    }


    private void getParams(){
        year = getIntent().getStringExtra("year");
        term = getIntent().getStringExtra("term");

        Gson gson = new Gson();
        //todo test
        yearParams = gson.fromJson(year,List.class);
        termParams = gson.fromJson(term,List.class);

    }



    private void retryLoadGrade(String term) {
        String termTemp = "";
        if (term.equals("0"))
            termTemp = "";
        else
            termTemp = term;
        String finalTermTemp = termTemp;
        showLoading();
        new LoginPresenter()
                .login(UserAccountManager.getInstance().getInfoUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(aBoolean -> CampusFactory.getRetrofitService()
                        .getScores(year, finalTermTemp))
                .subscribe(this::renderScoreList,
                        throwable -> {
                            throwable.printStackTrace();
                            mMultiStatusView.showNetError();
                            CcnuCrawler2.clearCookieStore();
                        }, this::hideLoading);
    }

    private void loadGrade() {
        //todo to refractor
        List<Observable<List<Score>>> scores = new ArrayList<>();
        for (int i = 0; i < yearParams.size(); i++) {
            for (int j = 0; j < termParams.size(); j++) {
                scores.add(CampusFactory
                        .getRetrofitService()
                        .getScores(yearParams.get(i), termParams.get(j)));
            }
        }

        Observable.merge(scores)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    CcnuCrawler2.clearCookieStore();
                    return new LoginPresenter()
                            .login(UserAccountManager.getInstance().getInfoUser())
                            .flatMap(aBoolean ->Observable.merge(scores))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io());
                })
               .subscribe(
                       this::renderScoreList,
                       throwable ->{
                           throwable.printStackTrace();
                           mMultiStatusView.showNetError();
                           hideLoading();
               },this::hideLoading );
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
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreDisplayActivity
                .this));
        recyclerView.setAdapter(mScoresAdapter);
    }


    @SuppressLint("DefaultLocale")
    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        //因为在请求的过程中使用了自动重新的登录 而且也有应用级别的cookie刷新 主动重试的情况一般不会出现
        mMultiStatusView.setOnRetryListener(v -> retryLoadGrade(term));
        setTitle(generateYearTitle() + generateTermTitle());

    }

    @SuppressLint("DefaultLocale")
    private String generateYearTitle(){
        int startYear = Integer.parseInt(yearParams.get(0));
        int endYear   = Integer.parseInt(yearParams.get(yearParams.size()-1));

        return String.format("第%d-%d学期",startYear,endYear);
    }

    private String generateTermTitle(){
        //通过termParams 生成对应的 学期
        String termTitle = "";
        if(termParams.size() == 1 && !termParams.get(0).equals("0")){
            String termValue = getTermValue(termParams.get(0));
            termTitle = String.format("第%s学期", termValue);
        }else if(termParams.size() == 1 && termParams.get(0).equals("0")){
            termTitle = "所有学期";
        }else{
            String termValueStart = getTermValue(termParams.get(0));
            String termValueEnd   = getTermValue(termParams.get(termParams.size() -1));
            termTitle = String.format("第%s-%s学期",termValueStart,termValueEnd);
        }
        return termTitle;
    }

    //从对应的 term 的 string 中解析出对应的 term 名称 第 n 学期
    private String getTermValue(String term){
        String termValue = "";
            switch (term){
                case "0":
                    termValue = "0";
                    break;
                case "3":
                    termValue = "1";
                    break;
                case "12":
                    termValue=  "2";
                    break;
                case "16":
                    termValue = "16";
                    break;
        }
        return termValue;
    }
}

