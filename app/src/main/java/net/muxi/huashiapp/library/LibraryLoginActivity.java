package net.muxi.huashiapp.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Space;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.login.LoginEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/5/15.
 */
public class LibraryLoginActivity extends BaseActivity {

    @Bind(R.id.edit_userName)
    LoginEditText mEditUserName;
    @Bind(R.id.edit_password)
    LoginEditText mEditPassword;
    @Bind(R.id.login_center_layout)
    RelativeLayout mLoginCenterLayout;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @Bind(R.id.space_divider)
    Space mSpaceDivider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setTitle("图书馆");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_login)
    public void onClick() {

    }
}
