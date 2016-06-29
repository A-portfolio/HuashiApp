package net.muxi.huashiapp.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/6/27.
 */
public class ElectricityActivity extends ToolbarActivity {


    public String[] groupStrings = {"区域", "建筑", "楼层", "房间"};

    public String[][] childStrings = {
            {"西区学生宿舍", "东区学生宿舍", "元宝山学生宿舍", "南湖学生宿舍", "公共教学楼"}
    };

    MyExpandableAdapter mAdapter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);
        init();
        initToolbar("电费查询");

    }

    public void init() {
        mAdapter = new MyExpandableAdapter(this);
        mExpandList.setAdapter(mAdapter);
        mExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), childStrings[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mExpandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int i, long id) {
                Toast.makeText(getApplicationContext(), groupStrings[i], Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
