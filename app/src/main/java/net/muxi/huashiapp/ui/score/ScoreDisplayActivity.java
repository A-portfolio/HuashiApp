package net.muxi.huashiapp.ui.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.Gson;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.credit.CreditGradeDialog;
import net.muxi.huashiapp.ui.score.adapter.ScoreCreditAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


//这里需要展示成绩学分情况 并且 算出学分绩
public class ScoreDisplayActivity extends ToolbarActivity {

    private List<Score> mFilteredList = new ArrayList<>();
    private String mYear;
    private String mTerm;
    private String mCourseType;

    private MultiStatusView mMultiStatusView;
    private Button mBtnEnter;

    private ScoreCreditAdapter mScoresAdapter;

    private List<String> mCourseParams = new ArrayList<>();
    private List<String> mYearParams = new ArrayList<>();
    private List<String> mTermParams = new ArrayList<>();

    public static void start(Context context, String year, String term,String courseType) {
        Intent starter = new Intent(context, ScoreDisplayActivity.class);
        starter.putExtra("mYear", year);
        starter.putExtra("mTerm", term);
        starter.putExtra("mCourseType",courseType);
        context.startActivity(starter);
    }



    private void getParams(){
        mYear = getIntent().getStringExtra("mYear");
        mTerm = getIntent().getStringExtra("mTerm");
        mCourseType = getIntent().getStringExtra("mCourseType");

        Gson gson = new Gson();

        mCourseParams = gson.fromJson(mCourseType,List.class);
        mYearParams = gson.fromJson(mYear,List.class);
        mTermParams = gson.fromJson(mTerm,List.class);

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
                .flatMap(aBoolean ->
                        CampusFactory.getRetrofitService()
                        .getScores(mYear, finalTermTemp))
                .subscribe( scoreList -> {
                            filterList(scoreList);
                            renderedFilteredScoreList();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            mMultiStatusView.showNetError();
                            CcnuCrawler2.clearCookieStore();
                        }, this::hideLoading);
    }

    private void loadGrade() {
        //todo to refractor
        List<Observable<List<Score>>> scores = new ArrayList<>();
        for (int i = 0; i < mYearParams.size(); i++) {
            for (int j = 0; j < mTermParams.size(); j++) {
                scores.add(CampusFactory
                        .getRetrofitService()
                        .getScores(mYearParams.get(i), mTermParams.get(j)));
            }
        }

        Observable.merge(scores)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    if(throwable instanceof  HttpException){
                        int code  = ((HttpException) throwable).code();
                        switch (code){
                            case 404:
                                //todo implement with hint
                                break;
                            case  403:
                                CcnuCrawler2.clearCookieStore();
                                return new LoginPresenter()
                                        .login(UserAccountManager.getInstance().getInfoUser())
                                        .flatMap(aBoolean ->Observable.merge(scores))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io());
                            case 500:
                                return Observable.merge(scores)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io());
                        }
                    }
                    //todo to test
                    return null;
                })
               .subscribe(
                       scoreList -> {
                           filterList(scoreList);
                           renderedFilteredScoreList();
                           },
                       throwable ->{
                           throwable.printStackTrace();
                           mMultiStatusView.showNetError();
                           hideLoading();
               },this::hideLoading );
    }

    /**
     * 根据用户选择的课程分类展示用户的成绩 比如 用户选择只查看
     * @param scores
     */
    private void filterList(List<Score> scores){
        List<Score> filteredList = new ArrayList<>();
        for(Score score: scores) {
            //有些课程的kcxzmc字段是空 注意规避一下
            for (String type : mCourseParams) {
                if(score.kcxzmc == null){
                    System.out.println("fuckfafa");
                }
                if (score.kcxzmc.equals(type)){
                    filteredList.add(score);
                }
            }
        }

        if ( filteredList.isEmpty()) {
            mMultiStatusView.showEmpty();
            return;
        }

        mFilteredList.addAll(filteredList);
    }

    private void renderedFilteredScoreList() {
        //filter list
        mMultiStatusView.showContent();


        try {
            mScoresAdapter = new ScoreCreditAdapter(mFilteredList);
        } catch (Exception e) {
            e.printStackTrace();
            mScoresAdapter = new ScoreCreditAdapter(new ArrayList<>());
        }
        //getContentView() 内部使用了 LayoutInflater 去加载自定义view MultiStatusView 中自定义的布局
        //需要加载的view 写在multistatusiew的定义中
        RecyclerView recyclerView = (RecyclerView) mMultiStatusView.getContentView();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreDisplayActivity
                .this));
        recyclerView.setAdapter(mScoresAdapter);
    }


    @SuppressLint("DefaultLocale")
    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        //因为在请求的过程中使用了自动重新的登录 而且也有应用级别的cookie刷新 主动重试的情况一般不会出现
        mMultiStatusView.setOnRetryListener(v -> retryLoadGrade(mTerm));
        setTitle(generateYearTitle() + generateTermTitle());

        mBtnEnter = findViewById(R.id.btn_enter);
        mBtnEnter.setOnClickListener(v -> {
            Map<Integer,Boolean> map = mScoresAdapter.getCheckMap();
            float credit = 0, sum = 0;
            Set<Integer> set = map.keySet();
            for(int key : set){
                if(map.get(key)){
                    sum += Float.parseFloat(mFilteredList.get(key).grade);
                    credit += Float.parseFloat(mFilteredList.get(key).credit);
                }
            }
            if(credit == 0)
                showCreditGradeDialog(0);
            float result = sum / credit;
            showCreditGradeDialog(result);
        });
    }

    private void showCreditGradeDialog(float result) {
        CreditGradeDialog gradeDialog = CreditGradeDialog.newInstance(result);
        gradeDialog.show(getSupportFragmentManager(), "result");
    }

    @SuppressLint("DefaultLocale")
    private String generateYearTitle(){
        int startYear = Integer.parseInt(mYearParams.get(0));
        int endYear   = Integer.parseInt(mYearParams.get(mYearParams.size()-1));

        return String.format("第%d-%d学期",startYear,endYear);
    }

    private String generateTermTitle(){
        //通过termParams 生成对应的 学期
        String termTitle = "";
        if(mTermParams.size() == 1 && !mTermParams.get(0).equals("0")){
            String termValue = getTermValue(mTermParams.get(0));
            termTitle = String.format("第%s学期", termValue);
        }else if(mTermParams.size() == 1 && mTermParams.get(0).equals("0")){
            termTitle = "所有学期";
        }else{
            String termValueStart = getTermValue(mTermParams.get(0));
            String termValueEnd   = getTermValue(mTermParams.get(mTermParams.size() -1));
            termTitle = String.format("第%s-%s学期",termValueStart,termValueEnd);
        }
        return termTitle;
    }

    //从对应的 mTerm 的 string 中解析出对应的 mTerm 名称 第 n 学期
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


    /**
     * activity出入的两种动画
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void slideFromBottom(AppCompatActivity context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            Transition transition = TransitionInflater.from(context).inflateTransition(com.muxistudio.common.R.transition.trans_slide_from_bottom);
            context.getWindow().setEnterTransition(transition);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void slideFromTop(AppCompatActivity context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            Transition transition = TransitionInflater.from(context).inflateTransition(com.muxistudio.common.R.transition.trans_slide_from_bottom);
            context.getWindow().setEnterTransition(transition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_all) {
            if (mScoresAdapter != null) {
                mScoresAdapter.setAllChecked();
                mScoresAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        slideFromBottom(this);

        setContentView(R.layout.activity_score_display);

        //获取解析 mYear mTerm params
        getParams();
        initView();
        loadGrade();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        slideFromTop(this);
    }
}

