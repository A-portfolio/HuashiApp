package net.muxi.huashiapp.common.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.ZhugeUtils;
import net.muxi.huashiapp.widget.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.support.design.widget.Snackbar.make;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseActivity extends AppCompatActivity {

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
        sendComponentNameByZG();
    }

    private void sendComponentNameByZG() {
         if (getComponentName() != null) {
             ZhugeUtils.sendEvent(getComponentName());
         }
        Logger.d(getComponentName().getClassName());
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), "loading");
    }

    public void hideLoading() {
        Logger.d("hideloading");
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
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
        //初始化分析跟踪
        ZhugeSDK.getInstance().init(getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
        ZhugeSDK.getInstance().flush(getApplicationContext());
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
    public void showErrorView(int resId){
        View errorView = LayoutInflater.from(this).inflate(resId,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams
        .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addContentView(errorView,params);
    }

    public boolean isError(boolean isError){
        return isError;
    }
}
