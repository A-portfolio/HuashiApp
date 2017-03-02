package net.muxi.huashiapp.ui.electricity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 16/6/27.
 */
public class ElectricityActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.area_1)
    TextView mArea1;
    @BindView(R.id.area_2)
    TextView mArea2;
    @BindView(R.id.area_3)
    TextView mArea3;
    @BindView(R.id.area_4)
    TextView mArea4;
    @BindView(R.id.area_5)
    TextView mArea5;
    @BindView(R.id.area_6)
    TextView mArea6;
    @BindView(R.id.tv_area)
    TextView mTvArea;
    @BindView(R.id.et_room)
    EditText mEtRoom;
    @BindView(R.id.btn_search)
    Button mBtnSearch;

    public static void start(Context context) {
        Intent starter = new Intent(context, ElectricityActivity.class);
        context.startActivity(starter);
    }


    private String[] mBuildings = new String[19];

    private TextView[] mArea;
    private String area;


    //东区对应的建筑
    private static final String[] buildingStrings1 = {
            "东区1栋", "东区2栋", "东区3栋", "东区4栋", "东区5栋", "东区6栋", "东区7栋", "东区8栋", "东区9栋", "东区10栋", "东区11栋", "东区12栋", "13栋西",
            "东区13栋东", "东区14栋", "东区15西", "东区15栋东", "东区16栋", "东区附1栋"};

    //西区对应的建筑
    private static final String[] buildingStrings2 = {
            "西区1栋", "西区2栋", "西区3栋", "西区4栋", "西区5栋", "西区6栋", "西区7栋", "西区8栋"
    };

    //元宝山对应的建筑
    private static final String[] buildingStrings3 = {
            "元宝山1栋", "元宝山2栋", "元宝山3栋", "元宝山4栋", "元宝山5栋"
    };

    //南湖对应的建筑
    private static final String[] buildingStrings4 = {
            "南湖1栋", "南湖2栋", "南湖3栋", "南湖4栋", "南湖5栋", "南湖6栋", "南湖7栋", "南湖8栋", "南湖9栋", "南湖10栋", "南湖11栋", "南湖12栋", "南湖13栋"
    };

    //国交对应的建筑
    private static final String[] buildingStrings5 = {
            "国交3栋", "国交4栋", "国交5栋", "国交6栋", "国交9栋"
    };

    //产宿对应的建筑
    private static final String[] buildingStrings6 = {
            "产宿8栋", "产宿9栋"
    };


    //查询参数
    private String mQuery;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);
        mToolbar.setTitle("电费查询");
        Logger.d("ele oncreate");

        mArea = new TextView[]{mArea1, mArea2, mArea3, mArea4, mArea5, mArea6};

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        area = data.getStringExtra("area");
        mTvArea.setText(area);
    }


    @OnClick({R.id.area_1, R.id.area_2, R.id.area_3, R.id.area_4, R.id.area_5, R.id.area_6, R.id.tv_area, R.id.et_room, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.area_1:
                setBackground(0);
                mBuildings = buildingStrings1;
                break;
            case R.id.area_2:
                setBackground(1);
                mBuildings = buildingStrings2;
                break;
            case R.id.area_3:
                setBackground(2);
                mBuildings = buildingStrings3;
                break;
            case R.id.area_4:
                setBackground(3);
                mBuildings = buildingStrings4;
                break;
            case R.id.area_5:
                setBackground(4);
                mBuildings = buildingStrings5;
                break;
            case R.id.area_6:
                setBackground(5);
                mBuildings = buildingStrings6;
                break;
            case R.id.tv_area:
                    Intent intent = new Intent(ElectricityActivity.this, ElectricityAreaOptionActivity.class);
                    intent.putExtra("buildings", mBuildings);
                    startActivityForResult(intent,0);
                break;
            case R.id.btn_search:

                if (mTvArea.getText().length() != 0 && mEtRoom.getText().toString().length() != 0) {
                    int index = area.indexOf("栋");
                    if (area.length() > index + 1) {
                        mQuery = area.substring(0, 1) + area.substring(2, index) + "-" + area.substring(index + 1, area.length()) + mEtRoom.getText().toString();
                    } else {
                        mQuery = area.substring(0, 1) + area.substring(2, index) + "-" + mEtRoom.getText().toString();
                    }
                    PreferenceUtil sp = new PreferenceUtil();
                    sp.saveString(PreferenceUtil.ELE_QUERY_STRING, mQuery);
                    ElectricityDetailActivity.start(this, mQuery);
                    ZhugeUtils.sendEvent("查询电费", "查询电费");
                    this.finish();
                    break;
                } else {
                    showErrorSnackbarShort("请完善信息");
                }

        }

    }

    private void setBackground(int position) {
        for (int i = 0; i < 6; i++) {
            if (i == position) {
                mArea[i].setBackgroundResource(R.drawable.shape_green);
                mArea[i].setTextColor(Color.WHITE);
            } else {
                mArea[i].setBackgroundResource(R.drawable.shape_disabled);
                mArea[i].setTextColor(getResources().getColor(R.color.disable_color));
            }

        }

    }


}
