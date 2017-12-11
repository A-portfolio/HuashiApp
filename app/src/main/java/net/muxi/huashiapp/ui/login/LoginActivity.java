package net.muxi.huashiapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.LibLoginEvent;
import net.muxi.huashiapp.event.LoginSuccessEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler2;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.MyBooksUtils;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.ZhugeUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        initViews();
    }


    private void initViews() {

        if (type.equals("info")) {
            setTitle("登录信息门户");
            pwdHint = "输入您的密码";
        } else {
            setTitle("登录图书馆");
            pwdHint = "初始密码为123456";
            mLayoutPwd.setHint(pwdHint);
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
        if (type.equals("info")) {
            Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
                subscriber.onStart();
                //这个算不算在主线程执行耗时请求
                boolean crawlerResult = false;
                try {
                    crawlerResult = CcnuCrawler2.performLogin(user.sid, user.password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (crawlerResult) {
                    subscriber.onNext(crawlerResult);
                    subscriber.onCompleted();
                    return;
                }
                boolean infoResult = false;
                try {
                    infoResult = CampusFactory.getRetrofitService().mainLogin(
                            Base64Util.createBaseStr(user)).execute().code() == 200;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(infoResult);
                subscriber.onCompleted();
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b -> {
                        if (b) {
                            finish();
                            App.saveUser(user);
                            String target = getIntent().hasExtra("target") ? getIntent().getStringExtra("target") : null;
                            RxBus.getDefault().send(new LoginSuccessEvent(target));
                            Log.d("here", "onClick: "+b);
                        } else {
                            showErrorSnackbarShort(R.string.tip_err_account);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        hideLoading();
                        showErrorSnackbarShort(R.string.tip_check_net);
                    },() -> hideLoading());
            ZhugeUtils.sendEvent("登录");
        } else {
            CampusFactory.getRetrofitService().libLogin(Base64Util.createBaseStr(user))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(verifyResponseResponse -> {
                                if (verifyResponseResponse.code() == 200) {
                                    App.saveLibUser(user);
                                    loadMyBooks();
                                    RxBus.getDefault().send(new LibLoginEvent());
                                    finish();
                                } else if (verifyResponseResponse.code() == 403) {
                                    showErrorSnackbarShort(getString(R.string.tip_err_account));
                                } else {
                                    showErrorSnackbarShort(getString(R.string
                                            .tip_school_server_error));
                                }
                            }, throwable -> {
                                throwable.printStackTrace();
                                hideLoading();
                                showErrorSnackbarShort(getString(R.string.tip_check_net));
                            },
                            () -> {
                                hideLoading();
                            });
            ZhugeUtils.sendEvent("图书馆登录");
        }
    }

    private void loadMyBooks() {
        CampusFactory.getRetrofitService().getAttentionBooks(
                Base64Util.createBaseStr(App.sLibrarayUser))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(listResponse -> {
                    if (listResponse.code() == 200) {
                        MyBooksUtils.saveAttentionBooks(listResponse.body());
                    }
                });
        CampusFactory.getRetrofitService().getPersonalBook(
                Base64Util.createBaseStr(App.sLibrarayUser))
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
