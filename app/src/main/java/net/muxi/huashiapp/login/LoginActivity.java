package net.muxi.huashiapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.ui.MainActivity;
import net.muxi.huashiapp.common.util.NetStatus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String NETCONNECT_FAILED = "请连接网络再试";
    public static final String VERIFY_FAILED = "你的学号或者密码有误";
    public static final String SERVICE_PROBLEM = "服务器繁忙,请稍后再试";


    @Bind(R.id.edit_userName)
    LoginEditText mEditUserName;
    @Bind(R.id.edit_password)
    LoginEditText mEditPassword;
    @Bind(R.id.tv_tips)
    TextView mTvTips;
    @Bind(R.id.login_center_layout)
    RelativeLayout mLoginCenterLayout;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.login_bottom_layout)
    RelativeLayout mLoginBottomLayout;
    private TextWatcher mTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateButton();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
    }

    @OnClick(R.id.btn_login)

    public void onClick() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);

//        if (!checkNetwork()) {
//            ToastUtil.showLong(NETCONNECT_FAILED);
//        }
//        if (checkAccount()) {
//            showLoginDialog();
//        } else {
//            ToastUtil.showShort(VERIFY_FAILED);
//        }


    }

    private void updateButton() {
        if (mEditUserName.length() != 0
                && mEditPassword.length() != 0) {
            mBtnLogin.setEnabled(true);
        } else mBtnLogin.setEnabled(false);
    }

    private void showLoginDialog() {
        ToastUtil.showShort("登陆成功");
    }

    private boolean checkAccount() {
        return true;
    }

    private boolean checkNetwork() {
        return NetStatus.isConnected();
    }

}
