package net.muxi.huashiapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;
import net.muxi.huashiapp.util.ZhugeUtils;
import net.muxi.huashiapp.widget.LoginEditText;
import net.muxi.huashiapp.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends BaseActivity {

    public static final String NETCONNECT_FAILED = "请连接网络再试";
    public static final String VERIFY_FAILED = "你的学号或者密码有误";
    public static final String SERVICE_PROBLEM = "服务器繁忙,请稍后再试";
    public static final String LOGIN_SUCCESS = "登录成功";

    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;

    RelativeLayout mLoginCenterLayout;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.edit_user_name)
    LoginEditText mEditUserName;
    @BindView(R.id.edit_password)
    LoginEditText mEditPassword;

    private User mUser;

    private TextWatcher mTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateButton();
        }
    };

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();

    }


    private void initViews() {
        mEditPassword.setHint(getResources().getString(R.string.tip_pwd_text));
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
        mBtnLogin.setEnabled(DEBUG_VALUE);

        mEditPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                checkAccount();
                mBtnLogin.callOnClick();
                return true;
            }
        });
    }


    @OnClick(R.id.btn_login)
    public void onClick() {

        if (!NetStatus.isConnected()) {
            ToastUtil.showLong(NETCONNECT_FAILED);
        } else {
            showProgressBarDialog(true,getString(R.string.tip_logining));
            checkAccount();
        }

    }


    private void updateButton() {
        if (mEditUserName.length() != 0
                && mEditPassword.length() != 0) {
            mBtnLogin.setEnabled(true);
        } else mBtnLogin.setEnabled(DEBUG_VALUE);
    }

    private void checkAccount() {
        mUser = new User();
        if (mEditUserName != null) {
            mUser.setSid(mEditUserName.getText().toString());
        }
        if (mEditPassword != null) {
            mUser.setPassword(mEditPassword.getText().toString());
        }
        CampusFactory.getRetrofitService()
                .mainLogin(Base64Util.createBaseStr(mUser))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<VerifyResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("403 ");
                        e.printStackTrace();
                        ToastUtil.showShort(SERVICE_PROBLEM);
                        showProgressBarDialog(false);
                    }

                    @Override
                    public void onNext(Response<VerifyResponse> response) {
//                        if (response.code() == 200) {

                        showProgressBarDialog(false);
                        if (response.code() == 200) {
                            Logger.d("200");
                            PreferenceUtil.saveString(PreferenceUtil.STUDENT_ID,mUser.getSid());
                            PreferenceUtil.saveString(PreferenceUtil.STUDENT_PWD,mUser.getPassword());
                            App.sUser.setSid(mUser.getSid());
                            App.sUser.setPassword(mUser.getPassword());

                            ToastUtil.showShort(LOGIN_SUCCESS);
                            startMainActivity();
                            ZhugeUtils.sendEvent("登录","登录成功");
                        }
                        if (response.code() == 403) {
                            ToastUtil.showShort(VERIFY_FAILED);
                            Logger.d("403");
                        }

//                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    //验证通过后登入主界面
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ZhugeSDK.getInstance().init(App.getContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ZhugeSDK.getInstance().flush(App.getContext());
    }

}
