package net.muxi.huashiapp.ui.score.activtities;

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
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.credit.CreditGradeDialog;
import net.muxi.huashiapp.ui.score.RequestRetry;
import net.muxi.huashiapp.ui.score.adapter.ScoreCreditAdapter;
import net.muxi.huashiapp.utils.ScoreCreditUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


//这里需要展示成绩学分情况 并且 算出学分绩
public class ScoreDisplayActivity extends ToolbarActivity {

    private ArrayList<Score> mFilteredList = new ArrayList<>();
    private String mYear;
    private String mTerm;
    private String mCourseType;

    private MultiStatusView mMultiStatusView;
    private Button mBtnEnter;

    private ScoreCreditAdapter mScoresAdapter;

    private List<String> mCourseParams = new ArrayList<>();
    private List<String> mYearParams = new ArrayList<>();
    private List<String> mTermParams = new ArrayList<>();

    private boolean mAllChecked = true;

    /**
     *
     * @param context context
     * @param year year eg: arraylistOf("2016","2017")
     * @param term term code
     * @param courseType courseType name eg: arraylistOf("专业主干课程","通识选修课")
     */
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



    private void loadGrade() {
        showLoading("正在请求成绩数据~~");

        Observable<List<Score>>[] scoreArray = new Observable[mYearParams.size()*mTermParams.size()];
        for(int i=0;i<mYearParams.size();i++) {
            for (int j = 0; j < mTermParams.size(); j++) {
                int index = i;
                scoreArray[i*mTermParams.size() + j] = CampusFactory.getRetrofitService()
                        .getScores(mYearParams.get(i), mTermParams.get(j))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(scoreList -> {

                           setLoadingInfo(CommonTextUtils.generateRandomScoreText(mYearParams.get(index)));
                        })
                        .retryWhen(new RequestRetry.Builder()
                        .setMaxretries(3)
                        .setObservable(scoreArray).setRetryInfo(() -> setLoadingInfo("登录过期，正在重新登录中"))
                                .build());
            }
        }

        Observable.merge(scoreArray,5)
                .flatMap((Func1<List<Score>, Observable<Score>>) Observable::from)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        if(e instanceof RequestRetry.RetryException){
                            int code = ((RequestRetry.RetryException) e).code;
                            switch (code){
                                case RequestRetry.RetryException.CONFIRM_QUERY:
                                    mMultiStatusView.showError();
                                    break;
                                case RequestRetry.RetryException.NET_ERROR:
                                    mMultiStatusView.showNetError();
                                    break;
                            }
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNext(Object o) {
                        List<Score> scoreList = (List<Score>) o;
                        boolean isFull = filterList(scoreList,mFilteredList);
                        if(isFull)
                            renderedFilteredScoreList();
                    }
                });
    }


    /**
     * 根据 {@link com.muxistudio.appcommon.Constants} 中的 CLASS_TYPE 中用户选择所要计算学分绩的个别类型进行过滤
     * 因为 {@link Score} 中 kcxmzc字段没有 “其他”类型for循环遍历完成之后依然没有这个类型，同样需要添加到filterList中去
     * @param scores
     * @return 是否为空 如果不为空返回true 如果为空返回false
     */
    private boolean filterList(List<Score> scores,List<Score> resultList){

        List<Score> filteredList = new ArrayList<>();
        for(Score score: scores) {
            if(score.kcxzmc == null) {
                filteredList.add(score);
                continue;
            }
            for(int i=0;i<mCourseParams.size();i++){
                if(score.kcxzmc.equals(mCourseParams.get(i))){
                    filteredList.add(score);
                    break;
                }
                //说明这些课程是不在我们的确定的名称集合中
                if(i == mCourseParams.size() -1){
                    filteredList.add(score);
                }
            }
        }

        if ( filteredList.isEmpty()) {
            mMultiStatusView.showEmpty();
            return false;
        }else{
            resultList.clear();
            resultList.addAll(filteredList);
            return true;
        }
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
        //getContentVAdapteriew() 内部使用了 LayoutInflater 去加载自定义view MultiStatusView 中自定义的布局
        //需要加载的view 写在multistatusiew的定义中
        RecyclerView recyclerView = (RecyclerView) mMultiStatusView.getContentView();

        try {
            Field field = mMultiStatusView.getClass().getDeclaredField("mContentView");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScoreDisplayActivity
                .this));
        recyclerView.setAdapter(mScoresAdapter);
    }


    @SuppressLint("DefaultLocale")
    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        //因为在请求的过程中使用了自动重新的登录 而且也有应用级别的cookie刷新 主动重试的情况一般不会出现
        mMultiStatusView.setOnRetryListener(v -> loadGrade());
        setTitle(ScoreCreditUtils.parseYears2Title(mYearParams));

        mBtnEnter = findViewById(R.id.btn_confirm);
        mBtnEnter.setOnClickListener(v -> {

            Map<Integer,Boolean> map = mScoresAdapter.getCheckMap();
            float credit = 0, sum = 0;
            Set<Integer> set = map.keySet();
            for(int key : set){
                if(map.get(key)){
                    if(!Character.isDigit(mFilteredList.get(key).grade.charAt(0)))
                        continue;
                    double curSum = Float.parseFloat(mFilteredList.get(key).grade);
                    double curCredit = Float.parseFloat(mFilteredList.get(key).credit);
                    sum += curSum*curCredit;
                    credit += curCredit;
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
              if(mAllChecked )
                mScoresAdapter.setAllChecked(true);
                mAllChecked = false;
            }else{
              mScoresAdapter.setAllChecked(false);
              mAllChecked = true;
            }
            mScoresAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_display);
        slideFromBottom(this);

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

