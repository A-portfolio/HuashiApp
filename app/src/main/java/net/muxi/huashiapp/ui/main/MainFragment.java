package net.muxi.huashiapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/1/25.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MainAdapter mMainAdapter;

    private String[] titles = {"成绩", "校园通知", "电费", "校园卡", "算学分", "空闲教室", "部门信息", "校历", "常用网址", "学而"};
    private Integer[] icons =
            {R.drawable.ic_score, R.drawable.ic_news, R.drawable.ic_ele, R.drawable.ic_card,
                    R.drawable.ic_credit, R.drawable.ic_empty_room, R.drawable.ic_apartment,
                    R.drawable.ic_calendar, R.drawable.ic_net, R.drawable.ic_xueer};

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("华师匣子");
        mMainAdapter = new MainAdapter((List<String>)Arrays.asList(titles),Arrays.asList(icons));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMainAdapter);
        return view;
    }
}
