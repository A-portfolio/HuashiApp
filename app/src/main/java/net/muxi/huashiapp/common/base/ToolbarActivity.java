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
//            toolbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            if (canBack()){
                ActionBar actionbar = getSupportActionBar();
                if (actionbar != null){
                    actionbar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            this.finish();
            return true;
        }else return super.onOptionsItemSelected(item);
    }

    protected boolean canBack() {
        return true;
    }
}
