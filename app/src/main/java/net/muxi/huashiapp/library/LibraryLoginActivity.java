package net.muxi.huashiapp.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.login.LoginEditText;

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
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
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
        User user = new User();
        user.setSid(mEditUserName.getText().toString());
        user.setPassword(mEditPassword.getText().toString());
        if (NetStatus.isConnected()){
            CampusFactory.getRetrofitService().libLogin(Base64Util.createBaseStr(user))
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
                            if (verifyResponseResponse.code() == 200){
                                Intent intent = new Intent(LibraryLoginActivity.this,MineActivity.class);
                                startActivity(intent);
                            }else if (verifyResponseResponse.code() == 403){
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_account));
                            }else {
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_server));
                            }
                        }
                    });
        }
    }
}
