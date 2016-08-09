package net.muxi.huashiapp.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.PreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/6/20.
 */
public class ScoreDetailFragment extends BaseFragment {

    @BindView(R.id.image_none)
    ImageView mImageNone;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_last)
    TextView mTvLast;

    private ScoresAdapter mScoresAdapter;

    private String mYear;
    private String mTerm;

    private static final String SCHOOL_YEAR = "year";
    private static final String SCHOOL_TERM = "term";


    public static ScoreDetailFragment newInstance(String year, String term) {

        Bundle args = new Bundle();
        args.putString(SCHOOL_YEAR, year);
        args.putString(SCHOOL_TERM, term);

        ScoreDetailFragment fragment = new ScoreDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_detail, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(),DividerItemDecoration.VERTICAL_LIST));
        mYear = getArguments().getString(SCHOOL_YEAR);
        mTerm = getArguments().getString(SCHOOL_TERM);

        User user = new User();
        final PreferenceUtil sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        CampusFactory.getRetrofitService()
                .getScores(Base64Util.createBaseStr(user), mYear, mTerm)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<Scores>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Scores> scoresList) {
                        setupRecyclerview(scoresList);
                        sp.saveInt(PreferenceUtil.SCORES_NUM, scoresList.size());
                        loadDetailScore(scoresList);
                    }
                });
        return view;
    }

    private void loadDetailScore(List<Scores> scoresList) {
        int i = 0;
        for (final Scores scores : scoresList) {
            final int j = i;
            CampusFactory.getRetrofitService()
                    .getDetailScores(Base64Util.createBaseStr(App.sUser),mYear,mTerm,scores.getCourse(),scores.getJxb_id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<DetailScores>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(DetailScores detailScores) {
                            detailScores.setCourse(scores.getCourse());
                            mScoresAdapter.addDetailScore(detailScores,j);
                        }
                    });
            i ++;
        }
    }

    private void setupRecyclerview(List<Scores> scoresList) {
        mScoresAdapter = new ScoresAdapter(scoresList);
        mRecyclerView.setAdapter(mScoresAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
