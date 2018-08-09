package net.muxi.huashiapp.ui.score.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.utils.UserUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.ScoreDisplayActivity;
import net.muxi.huashiapp.ui.score.SelectCourseTypeDialog;
import net.muxi.huashiapp.ui.score.SelectTermDialog;
import net.muxi.huashiapp.ui.score.SelectYearDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author :kolibreath
 * 真正的网络请求的逻辑 委托到了 ScoreDisplayActivity
 * 查算学分绩 fragment
 */
public class ScoreFragment extends BaseAppFragment implements  View.OnClickListener{


    private boolean mDefault = true;
    private String startYear, endYear;
    private TextView mTvYear;
    private TextView mTvTerm;
    private boolean mTerms[] = new boolean[3];

   //学期在请求的时候需要转换为特殊的一个参数 具体参考api文档
   private List<String> mTermParams = new ArrayList<>();
   private List<String> mYearParams = new ArrayList<>();
   private List<String> mCourseTypeParams = new ArrayList<>();

    private void initView(View view) {
        TextView mTvSelectYear = view.findViewById(R.id.tv_select_year);
        ImageView mIvYear = view.findViewById(R.id.iv_year);
        mTvYear = view.findViewById(R.id.tv_year);

        TextView mTvSelectTerm = view.findViewById(R.id.tv_select_term);
        mTvTerm = view.findViewById(R.id.tv_term);
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

    /**
     *如果没有选择直接查询的话将会设置为默认值
     */
    private void setDefaultValue(){
        startYear = UserUtil.getStudentFirstYear();
        endYear   = String.valueOf(Integer.parseInt(startYear) + 1);

        int startYearValue = Integer.parseInt(startYear);
        int endYearValue = Integer.parseInt(endYear);

        mYearParams.clear();
        for(int i=startYearValue;i<=endYearValue;i++){
            mYearParams.add(String.valueOf(i));
        }

        mTermParams.clear();
        mTermParams.add("");

        mCourseTypeParams.addAll(Arrays.asList(Constants.CREDIT_CATEGORY));

        for(int i=0;i<mTerms.length;i++)
            mTerms[i] = true;
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
        setDefaultValue();
        return  view;

    }

    //获取到学年的跨度
    //2015 -2016 学年度 不包括 2016年的成绩
    @SuppressLint("DefaultLocale")
    private void showSelectYearDialog() {

        SelectYearDialog dialog = SelectYearDialog.newInstance(startYear,endYear);

        dialog.show(getActivity().getSupportFragmentManager(), "score_credit_year_select");
        dialog.setOnPositiveButtonClickListener((start, end) -> {
            mDefault = false;
            startYear = start;
            endYear = end;

            int startYearValue = Integer.parseInt(startYear);
            int endYearValue   = Integer.parseInt(endYear);

            mYearParams.clear();

            for(int i = startYearValue;i< endYearValue;i++) {
                mYearParams.add(String.valueOf(i));
            }
            mTvYear.setText(String.format("%d-%d学年",startYearValue,endYearValue));
        });
    }

    /**
     * 下面的数字表示第 n 学期
     * 请求参数对应表： 全部 -> "" 1->"3" 2->"12" 3->"16"
     */
    private void showSelectTermDialog(){
        SelectTermDialog dialog = SelectTermDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(),"score_credit_term_select");
        dialog.setOnPositiveButtonClickListener(terms -> {
            mDefault = false;
            String term = "";

            mTermParams.clear();
            mTerms = terms;

            for(int i=0;i<terms.length;i++){
                if(terms[i]){
                    switch (i){
                        case 0:
                            mTermParams.add("3");
                            term += "第一学期/";
                            break;
                        case 1:
                            mTermParams.add("12");
                            term +="第二学期/";
                            break;
                        case 2:
                            mTermParams.add("16");
                            term +="第三学期";
                            break;
                    }
                }

            }
            if(terms[0] && terms[1] &&terms[2]){
                term = "全部";
                mTermParams.clear();
                mTermParams.add("");
            }else{
                term = term.substring(0,term.length()-1);
            }

            mTvTerm.setText(term);
        });
        }


    /**
     * 返回的课程中包括所有的课程 如果不满足条件的课程只能手动过滤掉
     */
    private void showSelectCourseTypeDialog(){
        SelectCourseTypeDialog dialog = SelectCourseTypeDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(),"score_credit_course_type");
        dialog.setOnPositiveButtonClickListener(courses -> {
            //boolean course 的顺序是专业主干课 专业选修 通识必修 通识选修 list 只会存放选择的课程
            mCourseTypeParams.clear();
            for(int i=0;i<courses.length;i++){
                if(courses[i]) {
                    switch (i) {
                        case 0:
                            mCourseTypeParams.add(Constants.CREDIT_CATEGORY[5]);
                            break;
                        case 1:
                            mCourseTypeParams.add(Constants.CREDIT_CATEGORY[1]);
                            break;
                        case 2:
                            mCourseTypeParams.add(Constants.CREDIT_CATEGORY[2]);
                            break;
                        case 3:
                            mCourseTypeParams.add(Constants.CREDIT_CATEGORY[3]);
                            break;
                    }
                }
            }
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
            String termJson = gson.toJson(mTermParams);
            String yearJson = gson.toJson(mYearParams);
            String courseTypeJosn = gson.toJson(mCourseTypeParams);

            if(mDefault) {
                String year = mYearParams.get(0) + "-" + mYearParams.get(mYearParams.size() -1);
                String term = "";
                if(mTerms[0]&&mTerms[1]&&mTerms[2]){
                    term = "第一学期/第二学期/第三学期";
                }
                for(int i=0;i<mTerms.length;i++){
                    if(mTerms[i]&&i==0)
                        term += "第一学期/";
                    if(mTerms[i]&&i==1)
                        term += "第二学期/";
                    if(mTerms[i]&&i==2)
                        term += "第三学期";
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("使用默认选项进行成绩查询")
                        .setMessage("选择的学年为:"+year+"\n"+
                                    "选择的学期为:"+term+"\n")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("确定", (dialog, which) -> {
                            dialog.dismiss();
                            ScoreDisplayActivity.start(getActivity(),yearJson,termJson,courseTypeJosn);
                        })
                        .create().show();

            }else
                ScoreDisplayActivity.start(getActivity(),yearJson,termJson,courseTypeJosn);
        }
    }

}