package net.muxi.huashiapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.LibLoginEvent;
import net.muxi.huashiapp.event.LoginSuccessEvent;
import net.muxi.huashiapp.event.RefreshSessionEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.MyBooksUtils;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends ToolbarActivity {
    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;

    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_sid)
    EditText mEtSid;
    @BindView(R.id.layout_sid)
    TextInputLayout mLayoutSid;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.layout_pwd)
    TextInputLayout mLayoutPwd;

    private User mUser;
    private LoginPresenter presenter = new LoginPresenter();
    private String type;
    private String pwdHint;

    /**
     * @param loginType 分为 lib 和 info
     */
    public static void start(Context context, String loginType) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("type", loginType);
        context.startActivity(starter);
    }

    public static void start(Context context, String loginType,String target) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("type", loginType);
        starter.putExtra("target",target);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);type = getIntent().getStringExtra("type");
        initViews();
        //showCaptcha(type);
        setLoginListener();
    }


    private void initViews() {

        if (type.equals("info")) {
            setTitle("登录信息门户");
            pwdHint = "输入您的密码";
        }

    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (!NetStatus.isConnected()) {
            showErrorSnackbarShort(R.string.tip_check_net);
            return;
        }
        final User user = new User();
        user.sid = mEtSid.getText().toString();
        user.password = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(user.sid) && TextUtils.isEmpty(user.password)){
            showErrorSnackbarShort(getString(R.string.tip_input_id_password));
            return;
        }else if (TextUtils.isEmpty(user.sid)){
            showErrorSnackbarShort(R.string.tip_input_id);
            return;
        }else if (TextUtils.isEmpty(user.password)){
            showErrorSnackbarShort(R.string.tip_input_password);
        }
        showLoading();
        if (type.equals("info")||type.equals("lib")) {
            presenter.login(user)
                    .subscribe(s -> {
                        int loginCode = Integer.parseInt((String)s);
                        //0 表示只登陆成功了信息门户
                        if (loginCode>=0) {
                            finish();
                            hideLoading();
                            //保存登录状态
                            PreferenceUtil.saveString(PreferenceUtil.LOGIN_STATUS,loginCode+"");
                            App.sLoginStatus = loginCode+"";
                            App.saveUser(user);
                            MobclickAgent.onProfileSignIn(user.getSid());
                            String target = getIntent().hasExtra("target") ?
                                    getIntent().getStringExtra("target") : null;
                            if(type.equals("info")) {
                                RxBus.getDefault().send(new LoginSuccessEvent(target));
                            }
                            else{
                                RxBus.getDefault().send(new LibLoginEvent());
                                }
                        } else {
                            hideLoading();
                            showErrorSnackbarShort(R.string.tip_err_account);
                        }
                    }, throwable -> {
                        Logger.d("登录失败");
                        Throwable e = (Throwable) throwable;
                        e.printStackTrace();
                        hideLoading();
                        showErrorSnackbarShort(R.string.tip_check_net);
                    }, () -> hideLoading());
            if (type.equals("info"))
                MobclickAgent.onEvent(this,"login");
            else {
                MobclickAgent.onEvent(this,"lib_login");}
        }
    }


    private void setLoginListener(){
        RxBus.getDefault().toObservable(RefreshSessionEvent.class)
        .subscribe(refreshSessionEvent -> {
            presenter.login(refreshSessionEvent.getUser());
        },Throwable::printStackTrace);
    }


    private void loadMyBooks() {
        CampusFactory.getRetrofitService().getAttentionBooks(App.sUser.sid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(listResponse -> {
                    if (listResponse.code() == 200) {
                        MyBooksUtils.saveAttentionBooks(listResponse.body());
                    }
                });
        CampusFactory.getRetrofitService().getPersonalBook(App.PHPSESSID)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(personalBooks -> {
                    if (personalBooks != null) {
                        MyBooksUtils.saveBorrowedBooks(personalBooks);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canBack() {
        return false;
    }
}
