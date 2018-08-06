package net.muxi.huashiapp.ui.score.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.UserUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.adapter.CreditAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//当前已修学分
public class CurCreditFragment extends BaseAppFragment {

    private int startYear,endYear;
    private List<Score> mCredit = new ArrayList<>();
    private ExpandableListView mElvCredit;

    private double mAllCredit = 0;
    private double mZybxCredit = 0, mZyxxCredit = 0, mTshxCredit = 0, mTsxxCredit = 0;
    private double mOtherCredit = 0;
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

    /**
     * 返回的课程二维list按照这个顺序：专业必修 专业选修 通识核心 通识选修 其他课程
     * @param credits
     * @return List<List<Score>>
     */
    public List<List<Score>> getSortCredit(List<Score> credits){
        List<Score> zybx = new ArrayList<>();
        List<Score> zyxx = new ArrayList<>();
        List<Score> tshx = new ArrayList<>();
        List<Score> tsxx = new ArrayList<>();
        List<Score> other = new ArrayList<>();

        //如果还有不符合这个上面的课程就归为其他类别
        //除了基本的四种分类之外其余的都属于其他的类别
        for(Score credit : credits){
            mAllCredit += Double.parseDouble(credit.credit);
            for(int i= 0;i< Constants.CREDIT_CATEGORY.length;i++){
                if(Constants.CREDIT_CATEGORY[i].equals(credit.kcxzmc)) {
                    switch (i) {
                        case 1:
                            mZyxxCredit += Double.parseDouble(credit.credit);
                            zyxx.add(credit);
                            break;
                        case 3:
                            mTsxxCredit += Double.parseDouble(credit.credit);
                            tsxx.add(credit);
                            break;
                        case 4:
                            mTshxCredit += Double.parseDouble(credit.credit);
                            tshx.add(credit);
                            break;
                        case 5:
                            mZybxCredit += Double.parseDouble(credit.credit);
                            zybx.add(credit);
                            break;
                        default:
                            mOtherCredit += Double.parseDouble(credit.credit);
                            other.add(credit);
                            break;
                    }
                }
            }
        }

        List<List<Score>> sortedList = new ArrayList<>();
        sortedList.add(zybx);
        sortedList.add(zyxx);
        sortedList.add(tshx);
        sortedList.add(tsxx);

        return sortedList;
    }

    public void loadCredit(Observable<List<Score>>[] listObservable) {
        showLoading();
        Observable<List<Score>> scoreObservable = Observable.merge(listObservable, 5)
                .flatMap(new Func1<List<Score>, Observable<Score>>() {
                    @Override
                    public Observable<Score> call(List<Score> scoreList) {
                        return Observable.from(scoreList);
                    }
                })
                .toList();

        scoreObservable
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    CcnuCrawler2.clearCookieStore();
                    return new LoginPresenter().login(UserAccountManager.getInstance().getInfoUser())
                            .flatMap(aubBoolean -> scoreObservable);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scores -> {
                    hideLoading();

                    mCredit = scores;
                    List<List<Score>> sortedList = getSortCredit(scores);

                    //专业必修 专业选修 通识核心 通识选修 其他课程
                    List<String> courseTypes = new ArrayList<>();
                    courseTypes.add("专业必修");
                    courseTypes.add("专业选修");
                    courseTypes.add("通识核心");
                    courseTypes.add("通识选修");
                    courseTypes.add("其他课程");

                    List<Double> courseCredits = new ArrayList<>();
                    courseCredits.add(mZybxCredit);
                    courseCredits.add(mZyxxCredit);
                    courseCredits.add(mTshxCredit);
                    courseCredits.add(mTsxxCredit);
                    courseCredits.add(mOtherCredit);

                    CreditAdapter adapter = new CreditAdapter(getActivity(),sortedList,courseTypes,courseCredits);
                    mElvCredit.setAdapter(adapter);

                }, Throwable::printStackTrace, () -> {
                });
    }

    /**
     * the wrapper of the credit loading
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_credit,container,false);

        mElvCredit = view.findViewById(R.id.eplv_credit);
        return view;
    }



    interface ViewPagerSlideListener{
        void onSlideRight();
    }
}
