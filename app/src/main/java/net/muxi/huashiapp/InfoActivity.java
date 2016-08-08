package net.muxi.huashiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.electricity.NonScrollExpandLv;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/8/4.
 */
public class InfoActivity extends ToolbarActivity {

    @BindView(R.id.expand_lv)
    NonScrollExpandLv mExpandLv;

    private InfoAdapter mAdapter;
    private static final String[] groupString = {"区域", "建筑"};

    //西区对应的建筑
    private static final String[][] childStrings1 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋"}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        mAdapter = new InfoAdapter(this,groupString,childStrings1);
        mExpandLv.setAdapter(mAdapter);
    }


}
