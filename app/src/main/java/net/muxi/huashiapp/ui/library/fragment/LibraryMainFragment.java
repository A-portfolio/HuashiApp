package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.BaseAppFragment;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.library.LibrarySearchActivity;
import net.muxi.huashiapp.ui.login.LoginActivity;


/**
 * Created by ybao on 17/2/14.
 */

public class LibraryMainFragment extends BaseAppFragment {


    private Toolbar mToolbar;
    private EditText mEtSearch;
    private ImageView mIvPeople;
    private TextView mTvTitle;
    private Button mBtnLoginLib;

    public static LibraryMainFragment newInstance() {
        Bundle args = new Bundle();
        LibraryMainFragment fragment = new LibraryMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_main, container, false);
        initView(view);
        mToolbar.setTitle(R.string.library);
        mEtSearch.setOnClickListener(v -> LibrarySearchActivity.start(getContext()));
        mBtnLoginLib.setOnClickListener(v -> LoginActivity.start(getContext(), "lib"));
        return view;
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        mEtSearch = view.findViewById(R.id.et_search);
        mIvPeople = view.findViewById(R.id.iv_people);
        mTvTitle = view.findViewById(R.id.tv_title);
        mBtnLoginLib = view.findViewById(R.id.btn_login_lib);
    }
}