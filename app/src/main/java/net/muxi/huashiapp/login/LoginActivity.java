package net.muxi.huashiapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.main.MainActivity;

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
public class LoginActivity extends AppCompatActivity {

    public static final String NETCONNECT_FAILED = "请连接网络再试";
    public static final String VERIFY_FAILED = "你的学号或者密码有误";
    public static final String SERVICE_PROBLEM = "服务器繁忙,请稍后再试";
    public static final String LOGIN_SUCCESS = "登录成功";

    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;
    @BindView(R.id.edit_userName)
    LoginEditText mEditUserName;
    @BindView(R.id.edit_password)
    LoginEditText mEditPassword;
    @BindView(R.id.login_center_layout)

    RelativeLayout mLoginCenterLayout;
    @BindView(R.id.btn_login)
    Button mBtnLogin;



    private User mUser;

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

//        ZhugeSDK.getInstance().openDebug();
//        ZhugeSDK.getInstance().openLog();
        initViews();
//        mBtnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });


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
        //        JSONObject eventObject = new JSONObject();
//        try {
//            eventObject.put("主界面登录","true");
//            ZhugeSDK.getInstance().track(App.getContext(),"主界面登录",eventObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        if (!NetStatus.isConnected()) {
            ToastUtil.showLong(NETCONNECT_FAILED);
        }
//        startMainActivity();
        checkAccount();

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
        mUser.setSid("2014214629");
        mUser.setPassword("fmc2014214629");
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
                        hintToCheck();
                    }

                    @Override
                    public void onNext(Response<VerifyResponse> response) {
//                        if (response.code() == 200) {

                        if (response.code() == 200){
                            Logger.d("200");
                        }
                        if(response.code() == 403){
                            Logger.d("403");
                        }
                        PreferenceUtil loader = new PreferenceUtil();
                        loader.saveString(PreferenceUtil.STUDENT_ID, mUser.getSid());
                        loader.saveString(PreferenceUtil.STUDENT_PWD, mUser.getPassword());

                        ToastUtil.showShort(LOGIN_SUCCESS);
                        startMainActivity();
//                        }
                    }
                });

    }

    //验证通过后登入主界面
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //提示重新核对账号密码
    private void hintToCheck() {
        ToastUtil.showLong(VERIFY_FAILED);
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
