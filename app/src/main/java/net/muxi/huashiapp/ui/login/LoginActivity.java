package net.muxi.huashiapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.data.UserInfo;
import com.muxistudio.appcommon.event.RefreshSessionEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.common.util.NetUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/18.
 */
public class LoginActivity extends ToolbarActivity {
    //此处方便登录调试,到时候要删除
    public static final boolean DEBUG_VALUE = true;

    private LoginPresenter presenter = new LoginPresenter();
    private String type;
    private boolean isShownPassword = false;
    private TextInputLayout mLayoutSid;
    private EditText mEtSid;
    private TextInputLayout mLayoutPwd;
    private EditText mEtPwd;
    private Button mBtnLogin;

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
        //showCaptcha(type);
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
        }
        showLoading();
        if (type.equals("info") || type.equals("lib")) {
            presenter.login(user)
                    .subscribeOn(Schedulers.io())
                    .flatMap(result -> {
                        if (result) {
                            hideLoading();
                            //保存登录状态
                            presenter.saveLoginState(getIntent(),user,type);
                            finish();
                            return CampusFactory.getRetrofitService()
                                    .postUserInfo(new
                                            UserInfo(Integer.parseInt(user.sid), user.password));
                        } else {
                            hideLoading();
                            CcnuCrawler2.clearCookieStore();
                            showErrorSnackbarShort(R.string.tip_err_account);
                            return Observable.error(new Exception());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();

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
