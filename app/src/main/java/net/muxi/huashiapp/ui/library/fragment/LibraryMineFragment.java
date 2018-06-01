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

import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.library.LibrarySearchActivity;
import net.muxi.huashiapp.ui.library.adapter.MyBookListPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ybao on 17/2/16.
 */

public class LibraryMineFragment extends BaseAppFragment {

    private MyBookListFragment mBooksBorrowedFragment;
    private MyBookListFragment mBooksAttentionFragment;

    private MyBookListPagerAdapter mPagerAdapter;
    private EditText mEtSearch;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

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
        mEtSearch = view.findViewById(R.id.et_search);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager);
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

        if (mBooksBorrowedFragment == null) {
            mBooksBorrowedFragment = MyBookListFragment.newInstance(
                    MyBookListFragment.TYPE_BORROW);
        }
        if (mBooksAttentionFragment == null) {
            mBooksAttentionFragment = MyBookListFragment.newInstance(
                    MyBookListFragment.TYPE_ATTENTION);
        }
        fragmentList.add(mBooksBorrowedFragment);
        fragmentList.add(mBooksAttentionFragment);
        mPagerAdapter = new MyBookListPagerAdapter(getChildFragmentManager(), fragmentList,
                titleList);
        mViewPager.setAdapter(mPagerAdapter);

        MobclickAgent.onEvent(this.getActivity(),"lib_mine");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
