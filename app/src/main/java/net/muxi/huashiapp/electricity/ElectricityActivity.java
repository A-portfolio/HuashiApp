package net.muxi.huashiapp.electricity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 16/6/27.
 */
public class  ElectricityActivity extends ToolbarActivity {



    public String[] groupStrings = {"区域", "建筑", "楼层", "房间"};

    public String[][] childStrings = {
            {"西区", "东区", "元宝山", "南湖"}
    };

    MyExpandableAdapter mAdapter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandList;
    @BindView(R.id.expand_select_button)
    Button mExpandSelectButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);
        init();
        mToolbar.setTitle("电费查询");
    }

    public void init() {
        mAdapter = new MyExpandableAdapter(this);
        mExpandList.setAdapter(mAdapter);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        mExpandList.setIndicatorBounds(width-80, width-10);
        mExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), childStrings[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;

            }
        });

    }

    @OnClick(R.id.expand_select_button)
    public void onClick() {
        Intent intent = new Intent(ElectricityActivity.this,ElectricityDetailActivity.class);
        startActivity(intent);

    }
}
