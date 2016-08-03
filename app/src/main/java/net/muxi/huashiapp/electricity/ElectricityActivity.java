package net.muxi.huashiapp.electricity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 16/6/27.
 */
public class ElectricityActivity extends ToolbarActivity {

    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.lv_expand)
    NonScrollExpandLv mLvExpand;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_romm)
    EditText mEtRomm;

    private String[] mGroupString;
    private String[][] mChildString = new String[2][19];

    private static final String[] groupString = {"区域", "建筑"};

    //西区对应的建筑
    private static final String[][] childStrings1 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋"}
    };

    //东区对应的建筑
    private static final String[][] childStrings2 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋", "9栋", "10栋", "11栋", "12栋", "13栋西", "13栋东", "14栋", "15栋西", "15栋东", "16栋", "附1栋"}
    };

    //元宝山对应的建筑
    private static final String[][] childStrings3 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋"}
    };

    //南湖对应的建筑
    private static final String[][] childStrings4 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋", "9栋", "10栋", "11栋", "12栋", "13栋"}
    };

    //查询参数
    private String mQuery;



    private MyExpandableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);
        mToolbar.setTitle("电费查询");
        Logger.d("ele oncreate");

        mGroupString = new String[]{"区域", "建筑"};
        mChildString = childStrings1;
        initView();
    }

    public void initView() {
        mAdapter = new MyExpandableAdapter(this, mGroupString, mChildString);
        mLvExpand.setAdapter(mAdapter);
//        int width = getWindowManager().getDefaultDisplay().getWidth();
//        mLvExpand.setIndicatorBounds(width - 80, width - 10);
        mLvExpand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        mAdapter.setOnRbClickListener(new MyExpandableAdapter.OnRbClickListener() {
            @Override
            public void onRbClick(int groupPosition, int rbPosition) {
                if (groupPosition == 0) {
                    switch (rbPosition) {
                        case 0:
                            mChildString = childStrings1;
                            break;
                        case 1:
                            mChildString = childStrings2;
                            break;
                        case 2:
                            mChildString = childStrings3;
                            break;
                        case 3:
                            mChildString = childStrings4;
                            break;
                    }
                    mGroupString[0] = mChildString[0][rbPosition];
                    mGroupString[1] = groupString[1];
                    mAdapter.updateData(mGroupString, mChildString);
                    mLvExpand.collapseGroup(0);
                }
                if (groupPosition == 1) {
                    mGroupString[1] = mChildString[1][rbPosition];
                    mAdapter.updateData(mGroupString, mChildString);
                    mLvExpand.collapseGroup(1);
                }
                Log.d("rb","sdakfk");
            }
        });


    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        if (!isEmpty()) {
            int index = mGroupString[1].indexOf("栋");
            mQuery = mGroupString[0].substring(0, 1) + mGroupString[1].substring(0, index) + "-" + mEtRomm.getText().toString();
            PreferenceUtil sp = new PreferenceUtil();
            sp.saveString(PreferenceUtil.ELE_QUERY_STRING, mQuery);
            Intent intent = new Intent(ElectricityActivity.this, ElectricityDetailActivity.class);
            intent.putExtra("query", mQuery);
            startActivity(intent);
            this.finish();
        } else {
            ToastUtil.showShort("请先完善信息");
        }

    }

    /**
     * 判断 是否有未选项
     *
     * @return
     */
    public boolean isEmpty() {
        if (!mGroupString[0].equals(groupString[0])
                && !mGroupString[1].equals(groupString[1])
                && !mEtRomm.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
