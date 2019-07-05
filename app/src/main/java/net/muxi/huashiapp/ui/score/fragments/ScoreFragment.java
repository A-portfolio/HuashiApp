package net.muxi.huashiapp.ui.score.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.activtities.ScoreDisplayActivity;
import net.muxi.huashiapp.ui.score.dialogs.ParamsDisplayDialog;
import net.muxi.huashiapp.ui.score.dialogs.SelectCourseTypeDialog;
import net.muxi.huashiapp.ui.score.dialogs.SelectTermDialog;
import net.muxi.huashiapp.ui.score.dialogs.SelectYearDialog;
import net.muxi.huashiapp.utils.ScoreCreditUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author :kolibreath
 * 真正的网络请求的逻辑 委托到了 ScoreDisplayActivity
 * 查算学分绩 fragment
 */
public class ScoreFragment extends BaseAppFragment implements  View.OnClickListener{


    private boolean mDefault = true;
    private String startYear, endYear;

    private TextView mTvCourseType;
    private TextView mTvYear;
    private TextView mTvTerm;

    private boolean mTerms[] = new boolean[3];

    //使用默认选择的时候的提示
    private String mCourseTypeName;
    private String mTermName;

    private List<String> mTermCodeParams = new ArrayList<>();
   private List<String> mTermNameParams = new ArrayList<>();
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
        mTvCourseType = view.findViewById(R.id.tv_course_type);

        Button mBtnQuery = view.findViewById(R.id.btn_confirm);
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

        mTvYear.setText(String.format("%s-%s学年",startYear,endYear));
        mTvTerm.setText(mTermName);
        mTvCourseType.setText(mCourseTypeName);
    }

    /**
     *如果没有选择直接查询的话将会设置为默认值
     * 学年 mYearParams:  包括用户入学的第一年一整个学年 eg: 2016210897 mYearParams 中就包含 2016 and 2017
     * 学期 mTermNameParams: 包括一学年所有学期 第一学期 第二学期 和 第三学期
     * 默认学期提示：
     * 学期code： 包括所有学期的code ""
     * 课程类型： 所有的课程类型
     */
    private void initDefaultValue(){
        startYear = UserUtil.getStudentFirstYear();
        endYear   = String.valueOf(Integer.parseInt(startYear) + 1);

        int startYearValue = Integer.parseInt(startYear);
        int endYearValue = Integer.parseInt(endYear);

        mYearParams.clear();
        for(int i=startYearValue;i<endYearValue;i++){
            mYearParams.add(String.valueOf(i));
        }

        mTermNameParams.clear();
        mTermNameParams.add("第一学期");
        mTermNameParams.add("第二学期");
        mTermNameParams.add("第三学期");

        mTermName = ScoreCreditUtils.parseNames2String(mTermNameParams,'/');

        mTermCodeParams.clear();
        mTermCodeParams.add("");

        mCourseTypeParams.addAll(Arrays.asList(Constants.CLASS_TYPE));
        mCourseTypeName = "全部";

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
        initDefaultValue();
        initView(view);
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

            endYearValue = ScoreCreditUtils.getProperEndYear(startYearValue,endYearValue);

            mYearParams = ScoreCreditUtils.parseNumber2Years(startYearValue,endYearValue);

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
            if (!terms[0]&&!terms[1] && !terms[2]){
                ToastUtil.showShort("请至少选择一个学期");
                return;
            }
            mDefault = false;

            mTerms = terms;

            mTermCodeParams = ScoreCreditUtils.parseTerm2Code(terms);
            mTermNameParams = ScoreCreditUtils.parseTerm2Names(terms);

            //清空默认情况下的mTermName
            mTermName = "";
            mTermName = ScoreCreditUtils.parseNames2String(mTermNameParams,'/');

            mTvTerm.setText(mTermName);
        });
    }


    /**
     *返回用户选择的课程名称
     */
    private void showSelectCourseTypeDialog(){
        SelectCourseTypeDialog dialog = SelectCourseTypeDialog.newInstance();
        dialog.show(getActivity().getSupportFragmentManager(), "score_credit_course_type");
        dialog.setOnPositiveButtonClickListener(courses ->{
            mDefault = false;
            if(courses != null )
                if(!courses.isEmpty())
                mCourseTypeParams = ScoreCreditUtils.getSelectedCourseType(courses);

            mCourseTypeName = "";
            mCourseTypeName = ScoreCreditUtils.parseNames2String(mCourseTypeParams,'/');
            mTvCourseType.setText(mCourseTypeName);
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

        if(id == R.id.btn_confirm){
            Gson gson = new Gson();
            String termJson = gson.toJson(mTermCodeParams);
            String yearJson = gson.toJson(mYearParams);
            String courseTypeJosn = gson.toJson(mCourseTypeParams);

            if(mDefault) {
                String year = ScoreCreditUtils.parseYears2Title(mYearParams);

                ParamsDisplayDialog dialog = ParamsDisplayDialog.newInstance(year,mTermName);
                dialog.setOnPositiveClickListener(() -> {
                    ScoreDisplayActivity.start(getActivity(),yearJson,termJson,courseTypeJosn);
                });
                dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"default_params");
            }else
                ScoreDisplayActivity.start(getActivity(),yearJson,termJson,courseTypeJosn);
        }
    }



}