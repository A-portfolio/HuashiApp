package net.muxi.huashiapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.A;
import com.muxistudio.appcommon.data.Msg;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.event.RefreshSessionEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;

import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends ToolbarActivity {
    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;

    private LoginPresenter presenter = new LoginPresenter();
    private String type;
    private final static String TAG="LOGIN";
    private LoadingDialog mLoadingDialog;
    private boolean isShownPassword = false;
    private TextInputLayout mLayoutSid;
    private EditText mEtSid;
    private TextInputLayout mLayoutPwd;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private Subscription mSubscription ;

    /**
     * @param loginType 分为 lib 和 info
     */
    public static void start(Context context, String loginType) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("type", loginType);
        context.startActivity(starter);
    }

    public static void start(Context context, String loginType, String target) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("type", loginType);
        starter.putExtra("target", target);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        type = getIntent().getStringExtra("type");
        if (type.equals("info")) {
            setTitle("登录信息门户");
        }
        setLoginListener();
    }

    public void onClick() {
        if (!NetUtil.isConnected()) {
            showErrorSnackbarShort(R.string.tip_check_net);
            return;
        }
        final User user = new User();
        user.sid = mEtSid.getText().toString();
        user.password = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(user.sid) && TextUtils.isEmpty(user.password)) {
            showErrorSnackbarShort(getString(R.string.tip_input_id_password));
            return;
        } else if (TextUtils.isEmpty(user.sid)) {
            showErrorSnackbarShort(R.string.tip_input_id);
            return;
        } else if (TextUtils.isEmpty(user.password)) {
            showErrorSnackbarShort(R.string.tip_input_password);
            return;
        }

        mLoadingDialog = showLoading(CommonTextUtils.generateRandomLoginText());
        if (type.equals("info") || type.equals("lib")) {

                     mSubscription =presenter.login(user)
                     .flatMap(new Func1<Boolean, Observable<?>>() {
                         @Override
                         public Observable<?> call(Boolean result) {
                             // TODO: 18-10-15 test 
                             if(result){
                                 Log.i(TAG, " login thread "+Thread.currentThread().getName());
                                 hideLoading();
                                 presenter.saveLoginState(getIntent(),user,type);
                                 finish();
                                 return CampusFactory.getRetrofitService()
                                         .cache(new A(Integer.parseInt(user.sid),user.password))
                                         .onErrorResumeNext(Observable.empty());

                             }
                             return Observable.error(new Throwable());
                         }
                     }).subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(msg->{
                         Log.i(TAG, " cache thread "+Thread.currentThread().getName());
                         Logger.i(((Msg)msg).getMsg());
                     },e->{
                        ToastUtil.showShort("登录失败！请检查账号密码是否正确");
                        hideLoading();
                     }, this::hideLoading);

             mLoadingDialog.setOnSubscriptionCanceledListener(
                 () -> {
                   if(! mSubscription.isUnsubscribed())
                     CcnuCrawler2.clear();
                     mSubscription.unsubscribe();
                 });


            if (type.equals("info"))
                MobclickAgent.onEvent(this, "login");
            else {
                MobclickAgent.onEvent(this, "lib_login");
            }
        }
    }


    private void setLoginListener() {
        RxBus.getDefault().toObservable(RefreshSessionEvent.class)
                .subscribe(refreshSessionEvent -> {
                    presenter.login(refreshSessionEvent.getUser());
                }, Throwable::printStackTrace);
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
        if (item.getItemId() == R.id.action_close){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canBack() {
        return false;
    }

    private void initView() {
        mLayoutSid = findViewById(R.id.layout_sid);
        mEtSid = findViewById(R.id.et_sid);
        mLayoutPwd = findViewById(R.id.layout_pwd);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(v -> onClick());
    }
}
