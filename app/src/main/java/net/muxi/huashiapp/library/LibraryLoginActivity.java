package net.muxi.huashiapp.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Space;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.login.LoginEditText;
import net.muxi.huashiapp.login.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/15.
 */
public class LibraryLoginActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_userName)
    LoginEditText mEditUserName;
    @BindView(R.id.edit_password)
    LoginEditText mEditPassword;
    @BindView(R.id.login_center_layout)
    RelativeLayout mLoginCenterLayout;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.space_divider)
    Space mSpaceDivider;

    private TextWatcher mTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateBtn();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mEditPassword.setHint(getResources().getString(R.string.tip_lib_pwd));
        mToolbar.setTitle("图书馆");
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateBtn() {
        if (mEditUserName.length() != 0 && mEditPassword.length() != 0) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }




    @OnClick(R.id.btn_login)
    public void onClick() {
        final User libUser = new User();
        libUser.setSid(mEditUserName.getText().toString());
        libUser.setPassword(mEditPassword.getText().toString());
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().libLogin(Base64Util.createBaseStr(libUser))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<VerifyResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response<VerifyResponse> verifyResponseResponse) {
                            if (verifyResponseResponse.code() == 200) {
                                PreferenceUtil sp = new PreferenceUtil();
                                sp.saveString(PreferenceUtil.LIBRARY_ID,libUser.getSid());
                                sp.saveString(PreferenceUtil.LIBRARY_PWD,libUser.getPassword());
                                Intent intent = new Intent(LibraryLoginActivity.this, MineActivity.class);
                                startActivity(intent);
                                LibraryLoginActivity.this.finish();
                            } else if (verifyResponseResponse.code() == 403) {
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_account));
                            } else {
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_server));
                            }
                        }
                    });
        }
    }
}
