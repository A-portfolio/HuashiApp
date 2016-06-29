package net.muxi.huashiapp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.muxi.huashiapp.R;

import butterknife.ButterKnife;

/**
 * Created by ybao on 16/4/26.
 */
public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        initToolbar();

    }

    public void initToolbar() {
        mAppBarLayout = ButterKnife.findById(this,R.id.appbar_layout);
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("校园通");
            Log.d("toolbar",mToolbar.getHeight() + "");
            this.setSupportActionBar(mToolbar);

            if (canBack()){
                ActionBar actionbar = getSupportActionBar();
                if (actionbar != null){
                    actionbar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }

    }


    public void setTitle(String title){
        if (mToolbar != null){
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //如果当前的Activity不能退出则需要改写
    protected boolean canBack() {
        return true;
    }
}
