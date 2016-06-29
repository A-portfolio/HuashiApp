package net.muxi.huashiapp.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.login.LoginEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/5/15.
 */
public class LibraryLoginActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.edit_userName)
    LoginEditText mEditUserName;
    @BindView(R.id.edit_password)
    LoginEditText mEditPassword;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.login_center_layout)
    RelativeLayout mLoginCenterLayout;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.login_bottom_layout)
    RelativeLayout mLoginBottomLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTips.setText(getResources().getString(R.string.tip_library_login));
        initToolbar("图书馆");
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
