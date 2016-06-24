package net.muxi.huashiapp.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/4/26.
 */
public class ScoreActivity extends ToolbarActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @Bind(R.id.expand_listview)
    ExpandableListView mExpandListview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setTitle("成绩查询");
        ExpandListAdapter expandListAdapter = new ExpandListAdapter(this);
        mExpandListview.setAdapter(expandListAdapter);
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

