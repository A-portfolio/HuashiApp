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
import net.muxi.huashiapp.common.Api;
import net.muxi.huashiapp.common.data.MainLoginResponse;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.RetrofitService;
import net.muxi.huashiapp.common.ui.MainActivity;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.schedule.ScheduleActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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

    private RetrofitService mRetrofitService;
    private User mUser = new User();
    private Retrofit mRetrofit;


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

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mRetrofitService = mRetrofit.create(RetrofitService.class);

        init();

    }

    private void init() {
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
    }

    @OnClick(R.id.btn_login)

    public void onClick() {
        if (!checkNetwork()) {
            ToastUtil.showLong(NETCONNECT_FAILED);
        }
        checkAccount();


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
        boolean b = false;
        if (mEditUserName != null) {
            mUser.setSid(mEditUserName.getText().toString());
        }
        if (mEditPassword != null) {
            mUser.setPassword(mEditPassword.getText().toString());
        }
        mRetrofitService.mainLogin(mUser)
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
                        if (mainLoginResponse.getStatus() == 200){
                            Intent intent = new Intent(LoginActivity.this,ScheduleActivity.class);
                            startActivity(intent);
                            ToastUtil.showShort(LOGIN_SUCCESS);
                        }else ToastUtil.showLong(VERIFY_FAILED);

                    }
                });
        return true;
    }

    private boolean checkNetwork() {
        return NetStatus.isConnected();

    }

}
