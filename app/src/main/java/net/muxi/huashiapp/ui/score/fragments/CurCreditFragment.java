package net.muxi.huashiapp.ui.score.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.UserUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.adapter.CreditAdapter;
import net.muxi.huashiapp.utils.ScoreCreditUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//当前已修学分
public class CurCreditFragment extends BaseAppFragment {

    private int startYear,endYear;
    private List<Score> mCredit = new ArrayList<>();

    private TextView mTvTotalCredit;
    private ExpandableListView mElvCredit;

    private ViewPagerSlideListener mViewPagerSlideListener;

    private int getCurYear(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date();

        int thisYear =  Integer.parseInt(format.format(date));
        //如果当前是2016 年 则自动选择 2016 - 2017 学年度的 学分
        if(startYear == (thisYear))
            thisYear = startYear + 1;

        return thisYear;
    }
    public static CurCreditFragment newInstance(int type){
        return new CurCreditFragment();
    }

    public List<Score> getCredit(){
        return mCredit;
    }

    public Observable<List<Score>>[] getScoreRequest(int start, int end) {
        Observable<List<Score>>[] observables = new Observable[(end - start)];
        for (int i = 0; i < (end - start); i++) {
            observables[i] = CampusFactory.getRetrofitService()
                    .getScores(String.valueOf(start + i), "");
        }
        return observables;
    }

    public void loadCredit(Observable<List<Score>>[] listObservable) {
        showLoading();
        Observable<List<Score>> scoreObservable =
                Observable.merge(listObservable, 5)
                .flatMap((Func1<List<Score>, Observable<Score>>) scoreList -> Observable.from(scoreList))
                .toList();

        scoreObservable
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    CcnuCrawler2.clear();
                    return new LoginPresenter().login(UserAccountManager.getInstance().getInfoUser())
                            .flatMap(aubBoolean -> scoreObservable);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    hideLoading();

                    mCredit = scores;

                    HashMap<String,Double> creditValueMap = ScoreCreditUtils.getSortedGroupCreditMap(scores);
                    HashMap<String,List<Score>> scoreMap  = ScoreCreditUtils.getSortedCourseTypeMap(scores);

                    List<List<Score>> sortedList = ScoreCreditUtils.getTypeOrderedList(scoreMap);
                    List<String> classType = ScoreCreditUtils.getCreditTypes();
                    List<Double> courseCredits = ScoreCreditUtils.getCreditValues(creditValueMap);

                    mElvCredit.setGroupIndicator(null);
                    CreditAdapter adapter = new CreditAdapter(getActivity(),sortedList,classType,courseCredits);
                    mElvCredit.setAdapter(adapter);

                    double totalValue = ScoreCreditUtils.getCreditTotal(creditValueMap);
                    mTvTotalCredit.setText(String.valueOf(totalValue));
                }, Throwable::printStackTrace, () -> {
                });
    }

    /**
     * the wrapper of the credit loading {@link #loadCredit(Observable[])}
     */
    public void loadCredit(){
        loadCredit(getScoreRequest(startYear,endYear));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startYear = Integer.parseInt(UserUtil.getStudentFirstYear());
        endYear = getCurYear();

        //loadCredit
        //todo to solve the 403 problem
    }

    public void setViewPagerListener(ViewPagerSlideListener listener){
        if(listener == null)
            this.mViewPagerSlideListener = listener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_credit,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        mElvCredit = view.findViewById(R.id.eplv_credit);
        mTvTotalCredit = view.findViewById(R.id.tv_credit_total_value);

    }

    interface ViewPagerSlideListener{
        void onSlideRight();
    }
}
