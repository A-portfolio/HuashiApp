package net.muxi.huashiapp.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreActivity extends ToolbarActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }


    @Override
    public void initToolbar() {
        super.initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canBack() {
        return super.canBack();
    }
}

