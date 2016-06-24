package net.muxi.huashiapp.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;

/**
 * Created by ybao on 16/6/20.
 */
public class ScoreDetailFragment extends BaseFragment{

    private String mYear;
    private int mTerm;

    private static final String SCHOOL_YEAR = "year";
    private static final String SCHOOL_TERM = "term";


    public static ScoreDetailFragment newInstance(String year,int term) {

        Bundle args = new Bundle();
        args.putString(SCHOOL_YEAR,year);
        args.putInt(SCHOOL_TERM,term);

        ScoreDetailFragment fragment = new ScoreDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_detail,container,false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYear = getArguments().getString(SCHOOL_YEAR);
        mTerm = getArguments().getInt(SCHOOL_TERM);

    }
}
