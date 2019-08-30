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
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.util.ToastUtil;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.login.GetScorsePresenter;
import net.muxi.huashiapp.login.SingleCCNUClient;
import net.muxi.huashiapp.ui.score.RequestRetry;
import net.muxi.huashiapp.ui.score.adapter.ScoreCreditAdapter;
import net.muxi.huashiapp.ui.score.dialogs.CreditGradeDialog;
import net.muxi.huashiapp.utils.ScoreCreditUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
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
    private CheckBox allChecked;
    private ScoreCreditAdapter mScoresAdapter;

    private final static String TAG = "getScores";
    private List<String> mCourseParams = new ArrayList<>();
    private List<String> mYearParams = new ArrayList<>();
    private List<String> mTermParams = new ArrayList<>();
    private boolean mAllChecked = true;
    private Date date=new Date();
    private AtomicInteger time=new AtomicInteger(0);
    private Subscription subscription;

    /**
     * @param context    context
     * @param year       year eg: arraylistOf("2016","2017")
     * @param term       term code
     * @param courseType courseType name eg: arraylistOf("专业主干课程","通识选修课")
     */
    public static void start(Context context, String year, String term, String courseType) {
        Intent starter = new Intent(context, ScoreDisplayActivity.class);
        starter.putExtra("mYear", year);
        starter.putExtra("mTerm", term);
        starter.putExtra("mCourseType", courseType);
        context.startActivity(starter);
    }


    private void getParams() {
        mYear = getIntent().getStringExtra("mYear");
        mTerm = getIntent().getStringExtra("mTerm");
        mCourseType = getIntent().getStringExtra("mCourseType");

        Gson gson = new Gson();

        mCourseParams = gson.fromJson(mCourseType, List.class);
        mYearParams = gson.fromJson(mYear, List.class);
        mTermParams = gson.fromJson(mTerm, List.class);

    }

    public void getScores() {
        LoadingDialog loadingDialog = showLoading("正在请求成绩数据~~");
        Observable<ResponseBody>[] scoreArray=new Observable[mYearParams.size()*mTermParams.size()];
        for (int i = 0; i <mYearParams.size() ; i++) {
            for (int j = 0; j <mTermParams.size() ; j++) {
                scoreArray[i*mTermParams.size()+j] = SingleCCNUClient.getClient().getScores(mYearParams.get(i),mTermParams.get(j),false, String.valueOf(date.getTime()),100,1,"","asc",time.get() );
            }

        }

        subscription=Observable.merge(scoreArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        hideLoading();
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        Log.i(TAG, "onError: ");
                        if (e instanceof HttpException) {
                            Log.e(TAG, "onError: response code"+((HttpException)e).code() );

                        }
                        e.printStackTrace();
                    }


                    //很坑的是教务处基本所有请求不管对错都返回200，所以迫不得已要在onNext里进行错误处理
                    //即如果json解析错误，我们暂且将其理解为其实是请求错误了
                    @Override
                    public void onNext(ResponseBody responseBody) {

                        Log.i(TAG, "onNext: getscore");
                        List<Score> scoreList = null;
                        try {
                            scoreList = ScoreCreditUtils.getScoreFromJson(responseBody.string());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(R.string.score_error_1);
                            mMultiStatusView.showError();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (scoreList==null)return;

                        boolean isFull = filterList(scoreList, mFilteredList);
                        if (!isFull) {
                            renderedFilteredScoreList();
                        }
                    }
                });



    }


    /**
     * 根据 {@link com.muxistudio.appcommon.Constants} 中的 CLASS_TYPE 中用户选择所要计算学分绩的个别类型进行过滤
     * <p>
     * 因为 {@link Score} 中 kcxmzc字段没有 “其他”类型for循环遍历完成之后依然没有这个类型，同样需要添加到filterList中去
     *
     * @param scores
     * @return 是否为空 如果不为空返回false 如果为空返回true
     */
    private boolean filterList(List<Score> scores, List<Score> resultList) {
        List<Score> filteredList = new ArrayList<>();
        for (Score score : scores) {
            boolean flag = false;
            //没有课程分类的课程
            if (score.kcxzmc == null) {
                filteredList.add(score);
                continue;
            }
            for (int i = 0; i < mCourseParams.size(); i++) {
                if (score.kcxzmc.equals(mCourseParams.get(i))) {
                    filteredList.add(score);
                    break;
                }
                //说明这些课程是不在我们的确定的名称集合中
                // 但是有可能在我们确定的集合的补集 中
                for (String type : Constants.CLASS_TYPE) {
                    if (score.kcxzmc.equals(type)) {
                        flag = true;
                        break;
                    }
                }
                if (i == mCourseParams.size() - 1 && !flag) {
                    filteredList.add(score);
                }
            }
        }

        if (filteredList.isEmpty()) {
            mMultiStatusView.showEmpty();
            return true;
        } else {
            resultList.addAll(filteredList);
            return false;
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
        //mMultiStatusView.setOnRetryListener(v -> loadGrade());


        setTitle(ScoreCreditUtils.parseYears2Title(mYearParams));

        mBtnEnter = findViewById(R.id.btn_confirm);
        allChecked=findViewById(R.id.all_check);
        allChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mScoresAdapter.setAllChecked(isChecked);
                mScoresAdapter.notifyDataSetChanged();
            }
        });
        mBtnEnter.setOnClickListener(v -> {
            // FIXME: 19-1-22
            if (mScoresAdapter == null) {
                mBtnEnter.setEnabled(false);
                mBtnEnter.setText("列表为空无法计算");
                return;
            }
            Map<Integer, Boolean> map = mScoresAdapter.getCheckMap();
            float credit = 0, sum = 0;
            Set<Integer> set = map.keySet();
            for (int key : set) {
                if (map.get(key)) {
                    if (!Character.isDigit(mFilteredList.get(key).grade.charAt(0)))
                        continue;
                    double curSum = Float.parseFloat(mFilteredList.get(key).grade);
                    double curCredit = Float.parseFloat(mFilteredList.get(key).credit);
                    sum += curSum * curCredit;
                    credit += curCredit;
                }
            }
            if (credit == 0)
                showCreditGradeDialog(0);
            else {
                float result = sum / credit;
                showCreditGradeDialog(result);
            }
        });


    }

    private void showCreditGradeDialog(float result) {
        CreditGradeDialog gradeDialog = CreditGradeDialog.newInstance(result);
        gradeDialog.showNow(getSupportFragmentManager(), "result");
    }


    /**
     * activity出入的两种动画
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void slideFromBottom(AppCompatActivity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Transition transition = TransitionInflater.from(context).inflateTransition(com.muxistudio.common.R.transition.trans_slide_from_bottom);
            context.getWindow().setEnterTransition(transition);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void slideFromTop(AppCompatActivity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Transition transition = TransitionInflater.from(context).inflateTransition(com.muxistudio.common.R.transition.trans_slide_from_bottom);
            context.getWindow().setEnterTransition(transition);
        }
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
        //performLogin();


        getScores();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        slideFromTop(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (subscription!=null&&subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

    }



}

