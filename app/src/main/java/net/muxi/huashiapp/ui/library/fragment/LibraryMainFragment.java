package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.ui.library.LibrarySearchActivity;
import net.muxi.huashiapp.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/14.
 */

public class LibraryMainFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.btn_login_lib)
    Button mBtnLoginLib;

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
        ButterKnife.bind(this,view);
        mToolbar.setTitle(R.string.library);
        mEtSearch.setOnClickListener(v -> LibrarySearchActivity.start(getContext()));
        mBtnLoginLib.setOnClickListener(v -> LoginActivity.start(getContext(),"lib"));
        return view;
    }
}
