package net.muxi.huashiapp.ui.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.*;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.Book;

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
        mTabLayout.addTab(mTabLayout.newTab().setText("关注"));
        mTabLayout.addTab(mTabLayout.newTab().setText("借阅"));

        mTabLayout.setupWithViewPager(mViewPager);


    }
}
