package net.muxi.huashiapp.ui.score.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.UserUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.ScoreDisplayActivity;
import net.muxi.huashiapp.ui.score.SelectCourseTypeDialog;
import net.muxi.huashiapp.ui.score.SelectTermDialog;
import net.muxi.huashiapp.ui.score.SelectYearDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author :kolibreath
 * 查算学分绩 fragment
 */
public class ScoreFragment extends BaseAppFragment implements  View.OnClickListener{


    private TextView mTvYear;

   //学期在请求的时候需要转换为特殊的一个参数 具体参考api文档
   private List<String> termParams = new ArrayList<>();
   private List<String> yearParams = new ArrayList<>();

    private void initView(View view) {
        TextView mTvSelectYear = view.findViewById(R.id.tv_select_year);
        ImageView mIvYear = view.findViewById(R.id.iv_year);
        mTvYear = view.findViewById(R.id.tv_year);

        TextView mTvSelectTerm = view.findViewById(R.id.tv_select_term);
        TextView mTvTerm = view.findViewById(R.id.tv_term);
        ImageView mIvTerm = view.findViewById(R.id.iv_term);

        ImageView mIvCourseType = view.findViewById(R.id.iv_course_type);
        TextView mTvSelectCourseType = view.findViewById(R.id.tv_select_course);
        TextView mTvCourseType = view.findViewById(R.id.tv_course_type);

        Button mBtnQuery = view.findViewById(R.id.btn_enter);
        mBtnQuery.setOnClickListener(this);

        mTvSelectYear.setOnClickListener(this);
        mIvYear.setOnClickListener(this);
        mTvYear.setOnClickListener(this);
        mTvSelectTerm.setOnClickListener(this);
        mTvTerm.setOnClickListener(this);
        mIvTerm.setOnClickListener(this);
        mIvCourseType.setOnClickListener(this);
        mTvSelectCourseType.setOnClickListener(this);
        mTvCourseType.setOnClickListener(this);

    }

    public static ScoreFragment newInstance(int type){

        Bundle args = new Bundle();
        args.putInt("type",type);
        ScoreFragment scoreFragment = new ScoreFragment();
        scoreFragment.setArguments(args);

        return scoreFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_score, container, false);
        initView(view);
        return  view;

    }


    public void setYear(int value) {
        mTvYear.setText(String.format("%s学年", UserUtil.generateHyphenYears(4)[value]));
    }


    //获取到学年的跨度
    //2015 -2016 学年度 不包括 2016年的成绩
    private void showSelectYearDialog() {
        //todo startyear and end year required!
        SelectYearDialog dialog = SelectYearDialog.newInstance("2016","2018");
        dialog.show(getActivity().getSupportFragmentManager(), "score_credit_year_select");
        dialog.setOnPositiveButtonClickListener((start, end) -> {
            int startYear = Integer.parseInt(start);
            int endYear = Integer.parseInt(end);

            for(int i = startYear;i< endYear;i++)
               yearParams.add(String.valueOf(i));

            //todo to find what fuck it is!
//            setYear(mGap);
            loadGrade();
        });
    }

    /**
     * 下面的数字表示第 n 学期
     * 请求参数对应表： 0 -> "0" 1->"3" 2->"12" 3->"16"
     */
    private void showSelectTermDialog(){
        SelectTermDialog dialog = SelectTermDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(),"score_credit_term_select");
        dialog.setOnPositiveButtonClickListener(terms -> {
            for(int i=0;i<terms.length;i++){
                if(terms[i]){
                    switch (i){
                        case 0:
                            termParams.add("0");
                            break;
                        case 1:
                            termParams.add("3");
                            break;
                        case 2:
                            termParams.add("12");
                            break;
                        case 3:
                            termParams.add("16");
                            break;
                    }
                }
            }
        });
        }


    /**
     * 返回的课程中包括所有的课程 如果不满足条件的课程只能手动过滤掉
     */
    private void showSelectCourseTypeDialog(){
        SelectCourseTypeDialog dialog = SelectCourseTypeDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(),"score_credit_course_type");
        dialog.setOnPositiveButtonClickListener(courses -> {
            //todo to implement
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_year || id == R.id.tv_select_year || id == R.id.tv_year) {
            showSelectYearDialog();
        }

        if(id == R.id.iv_term || id == R.id.tv_term || id == R.id.tv_select_term){
            showSelectTermDialog();
        }

        if(id == R.id.tv_select_course || id == R.id.tv_course_type || id == R.id.iv_course_type){
            showSelectCourseTypeDialog();
        }

        if(id == R.id.btn_enter){
            Gson gson = new Gson();
            String termJson = gson.toJson(termParams);
            String yearJson = gson.toJson(yearParams);

            ScoreDisplayActivity.start(getActivity(),yearJson,termJson);
        }
    }



    private void loadGrade() {
        List<Observable<List<Score>>> scores = new ArrayList<>();
        for (int i = 0; i < yearParams.size(); i++) {
            for (int j = 0; j < termParams.size(); j++) {
                scores.add(CampusFactory.getRetrofitService()
                        .getScores(yearParams.get(i), termParams.get(j)));
            }
        }

        //todo 增加应用级别的重试
        Observable.from(scores)
                .toList()
                .flatMapIterable((Func1<List<Observable<List<Score>>>, Iterable<?>>) observables -> observables)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(scoresList -> {
                    //get List<Scores> from the score
                    //todo to render
                        },Throwable::printStackTrace,()->{}
                );
    }


}