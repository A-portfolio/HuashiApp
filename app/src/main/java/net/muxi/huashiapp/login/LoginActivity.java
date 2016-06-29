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
import net.muxi.huashiapp.common.data.MainLoginResponse;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.AlarmUtil;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.main.MainActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.login_center_layout)
    RelativeLayout mLoginCenterLayout;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.login_bottom_layout)
    RelativeLayout mLoginBottomLayout;


    private User mUser = new User();


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

        init();

        AlarmUtil.register(this);

    }

    private void init() {
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
        mBtnLogin.setEnabled(DEBUG_VALUE);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


//        if (!checkNetwork()) {
//            ToastUtil.showLong(NETCONNECT_FAILED);
//        }
//        checkAccount();
    }


    private void updateButton() {
        if (mEditUserName.length() != 0
                && mEditPassword.length() != 0) {
            mBtnLogin.setEnabled(true);
        } else mBtnLogin.setEnabled(DEBUG_VALUE);
    }

    private boolean checkAccount() {
        boolean b = false;
        if (mEditUserName != null) {
            mUser.setSid(mEditUserName.getText().toString());
        }
        if (mEditPassword != null) {
            mUser.setPassword(mEditPassword.getText().toString());
        }

        CampusFactory.getRetrofitService().mainLogin(mUser)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainLoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MainLoginResponse mainLoginResponse) {
                        if (mainLoginResponse.getStatus() == 200) {

                            PreferenceUtil loader = new PreferenceUtil();
                            loader.saveString("lastUserId", mUser.getSid());
                            loader.saveString("lastUserMainPwd", mUser.getPassword());

                            Intent intent = new Intent(LoginActivity.this, ScheduleActivity.class);
                            startActivity(intent);

                            ToastUtil.showShort(LOGIN_SUCCESS);

                        } else if (mainLoginResponse.getStatus() == 403) {
                            ToastUtil.showLong(VERIFY_FAILED);

                        } else ToastUtil.showLong(SERVICE_PROBLEM);
                    }
                });
        return true;
    }

    private boolean checkNetwork() {
        return NetStatus.isConnected();

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
