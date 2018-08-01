package net.muxi.huashiapp.ui.score.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.utils.UserUtil;
import com.muxistudio.appcommon.widgets.LoadingDialog;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.SelectYearDialogFragment;

/**
 * author :kolibreath
 * 查算学分绩 fragment
 */
public class ScoreFragment extends Fragment implements  View.OnClickListener{

    private LoadingDialog mLoadingDialog;

    private TextView mTvSelectYear;
    private TextView mTvSelectTerm;
    private TextView mTvSelectCourseType;

    private TextView mTvYear;
    private TextView mTvTerm;
    private TextView mTvCourseType;

    private ImageView mIvYear;
    private ImageView mIvTerm;
    private ImageView mIvCourseType;

    private FragmentActivity mContext;

   private int type ;
   private int value;


    private void initView(View view) {
        mTvSelectYear = view.findViewById(R.id.tv_select_year);
        mIvYear = view.findViewById(R.id.iv_year);
        mTvYear = view.findViewById(R.id.tv_year);

        mTvSelectTerm = view.findViewById(R.id.tv_select_term);
        mTvTerm = view.findViewById(R.id.tv_term);
        mIvTerm = view.findViewById(R.id.iv_term);

        mIvCourseType = view.findViewById(R.id.iv_course_type);
        mTvSelectCourseType = view.findViewById(R.id.tv_select_course);
        mTvCourseType = view.findViewById(R.id.tv_course_type);

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
        type = getArguments().getInt("type");

        setYear(value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_score, container, false);
        mContext = (FragmentActivity) getActivity();
        initView(view);
        return  view;

    }


    public void setYear(int value) {
        mTvYear.setText(String.format("%s学年", UserUtil.generateHyphenYears(4)[value]));
    }


    private void showSelectYearDialog() {
        SelectYearDialogFragment fragment = SelectYearDialogFragment.newInstance(value);
        fragment.show(getActivity().getSupportFragmentManager(), "score_year_select");
        fragment.setOnPositionButtonClickListener(v -> {
            setYear(fragment.getValue());
        });
    }

    private void showSelectTermDialog(){

    }

    private void showSlectCourseTypeDialog(){

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_year || id == R.id.tv_select_year || id == R.id.tv_year) {
            showSelectYearDialog();
        }

        if(id == R.id.iv_term || id == R.id.tv_term || id == R.id.tv_select_term){

        }

        if(id == R.id.tv_select_course || id == R.id.tv_course_type || id == R.id.iv_course_type){

        }



    }


}