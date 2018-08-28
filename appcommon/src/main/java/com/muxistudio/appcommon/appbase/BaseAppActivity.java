package com.muxistudio.appcommon.appbase;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.muxistudio.appcommon.R;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.event.NetErrorEvent;
import com.muxistudio.appcommon.event.RefreshSessionEvent;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.base.BaseActivity;
import com.muxistudio.common.util.Logger;
import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.support.design.widget.Snackbar.make;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseAppActivity extends BaseActivity {

    protected Menu menu;
    protected ActionBar mActionBar;

    private LoadingDialog mLoadingDialog;
    private CompositeSubscription mCompositeSubscription;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
    }

    public void showLoading(String loadingInfo) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
            //mLoadingDialog.setLoadingInfo(loadingInfo);
        }
        //mLoadingDialog.setLoadingInfo(loadingInfo);
        mLoadingDialog.show(getSupportFragmentManager(), "loading");
    }

    public void hideLoading() {
        Logger.d("hideloading");
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
        }
    }

    public void addSubscription(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化友盟统计
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void showSnackbarLong(String msg) {
        make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    public void showSnackbarShort(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void  showSnackbarLong(int resId) {
        showSnackbarLong(getString(resId));
    }

    public void showSnackbarShort(int resId) {
        showSnackbarShort(getString(resId));
    }

    public void showErrorSnackbarShort(int resId) {
        showErrorSnackbarShort(getString(resId));
    }

    public void showErrorSnackbarShort(String msg) {
        Snackbar snackbar;
        snackbar = make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .setText(msg);
        View view = snackbar.getView();
        view.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    /**
     * 给loadingDialog设置加载动画的提示，注意这个方法需要运行在创建dialog的线程上
     * @param text
     */
    //todo set it with Builder Pattern
    public void setLoadingInfo(String text){
        if(mLoadingDialog != null){
            mLoadingDialog.setLoadingInfo(text);
        }
    }
}
