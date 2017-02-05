package net.muxi.huashiapp.ui.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.TimeTableUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/5.
 */

public class CurweekSetActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lv)
    ListView mLv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curweek_set);
        ButterKnife.bind(this);
        setTitle("选择当前周");
        String[] s = new String[Constants.WEEKS_LENGTH];
        for (int i = 0;i < Constants.WEEKS_LENGTH;i ++){
            if (i < 9){
                s[i] = String.format("第0d%周",i + 1);
            }else {
                s[i] = String.format("第d%周",i + 1);
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.item_curweek_set,s);
        mLv.setAdapter(arrayAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeTableUtil.saveCurWeek(i + 1);
                finish();
            }
        });
    }
}
