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

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.User;

import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.common.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.login.CcnuCrawler3;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends ToolbarActivity {
    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;

    private String type;
    private final static String TAG = "LOGIN";
    private LoadingDialog mLoadingDialog;
    private boolean isShownPassword = false;
    private TextInputLayout mLayoutSid;
    private EditText mEtSid;
    private TextInputLayout mLayoutPwd;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private Subscription mSubscription;

    private CcnuCrawler3 loginPresenter;

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
            setTitle("统一身份认证");
        }
        loginPresenter = new CcnuCrawler3();
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

            loginPresenter.getClient().clearAllCookie();
            loginPresenter.performLogin(new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    Log.i(TAG, "onCompleted: ");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: "+e.getMessage());
                    hideLoading();
                    if (UserAccountManager.getInstance().isInfoUserLogin()) {
                        ToastUtil.showLong("认证成功！");
                        finish();
                    }
                    else
                        ToastUtil.showShort("登录失败！请检查账号密码是否正确");
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    loginPresenter.saveLoginState(getIntent(), user, type);
                    loginPresenter.getClient().saveCookieToLocal();
                    hideLoading();
                    ToastUtil.showShort("登陆成功!");
                    finish();
                }
            }, user);


            if (type.equals("info"))
                MobclickAgent.onEvent(this, "login");
            else {
                MobclickAgent.onEvent(this, "lib_login");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unsubscription();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
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
        if (PreferenceUtil.getBoolean("prompt",true)){
            LoginPromptDialog dialog=new LoginPromptDialog();
            dialog.show(getSupportFragmentManager(),"prompt");
        }
    }


}
