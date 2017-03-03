package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.ui.library.LibrarySearchActivity;
import net.muxi.huashiapp.ui.library.adapter.MyBookListPagerAdapter;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.widget.IndicatedView.IndicatedView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/16.
 */

public class LibraryMineFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private MyBookListPagerAdapter mPagerAdapter;

    public static LibraryMineFragment newInstance() {
        Bundle args = new Bundle();
        LibraryMineFragment fragment = new LibraryMineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_mine, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mEtSearch.setOnClickListener(v -> {
            LibrarySearchActivity.start(getContext());
        });

        List<String> titleList = new ArrayList<>();
        titleList.add("借阅");
        titleList.add("关注");
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(1)));
        mTabLayout.setupWithViewPager(mViewPager);

        List<Fragment> fragmentList = new ArrayList<>();
        MyBookListFragment myBookListFragment = MyBookListFragment.newInstance(
                MyBookListFragment.TYPE_BORROW);
        MyBookListFragment myBookListFragment1 = MyBookListFragment.newInstance(
                MyBookListFragment.TYPE_ATTENTION);
        fragmentList.add(myBookListFragment);
        fragmentList.add(myBookListFragment1);
        mPagerAdapter = new MyBookListPagerAdapter(getChildFragmentManager(), fragmentList,
                titleList);
        mViewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("book mine");
    }
}
