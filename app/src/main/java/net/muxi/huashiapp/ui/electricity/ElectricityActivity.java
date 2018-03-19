package net.muxi.huashiapp.ui.electricity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.EleRequestData;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/6/27.
 */
public class ElectricityActivity extends ToolbarActivity {

    private RelativeLayout mEleRelativeLayout;
    private TextView mText;
    private LinearLayout mAreaLayout;
    private TextView mArea1;
    private TextView mArea2;
    private TextView mArea3;
    private TextView mArea4;
    private TextView mArea5;
    private TextView mArea6;
    private ImageView mIvChooseArea;
    private TextView mHintChooseArea;
    private TextView mTvArea;
    private ImageView mIvChooseRoom;
    private EditText mEtRoom;
    private Button mBtnSearch;

    public static void start(Context context) {
        Intent starter = new Intent(context, ElectricityActivity.class);
        context.startActivity(starter);
    }


    private String[] mBuildings = new String[19];

    private TextView[] mArea;
    private String area;


    //东区对应的建筑
    private static final String[] buildingStrings1 = {
            "东区1栋", "东区2栋", "东区3栋", "东区4栋", "东区5栋", "东区6栋", "东区7栋", "东区8栋", "东区9栋", "东区10栋", "东区11栋", "东区12栋", "东区13栋西",
            "东区13栋东", "东区14栋", "东区15栋西", "东区15栋东", "东区16栋", "东区附1栋"};

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

        mToolbar.setTitle("电费查询");
        mArea = new TextView[]{mArea1, mArea2, mArea3, mArea4, mArea5, mArea6};
        initView();
    }

    private void initView() {
        mEleRelativeLayout = findViewById(R.id.ele_relative_layout);
        mText = findViewById(R.id.text);
        mAreaLayout = findViewById(R.id.area_layout);
        mArea1 = findViewById(R.id.area_1);
        mArea2 = findViewById(R.id.area_2);
        mArea3 = findViewById(R.id.area_3);
        mArea4 = findViewById(R.id.area_4);
        mArea5 = findViewById(R.id.area_5);
        mArea6 = findViewById(R.id.area_6);
        mIvChooseArea = findViewById(R.id.iv_choose_area);
        mHintChooseArea = findViewById(R.id.hint_choose_area);
        mTvArea = findViewById(R.id.tv_area);
        mIvChooseRoom = findViewById(R.id.iv_choose_room);
        mEtRoom = findViewById(R.id.et_room);
        mBtnSearch = findViewById(R.id.btn_search);
        mArea1.setOnClickListener(v -> onClick(v));
        mArea2.setOnClickListener(v -> onClick(v));
        mArea3.setOnClickListener(v -> onClick(v));
        mArea4.setOnClickListener(v -> onClick(v));
        mArea5.setOnClickListener(v -> onClick(v));
        mArea6.setOnClickListener(v -> onClick(v));
        mTvArea.setOnClickListener(v -> onClick(v));
        mHintChooseArea.setOnClickListener(v -> onClick(v));
        mBtnSearch.setOnClickListener(v -> onClick(v));
        mArea1.setBackgroundResource(R.drawable.shape_green);
        mArea1.setTextColor(Color.WHITE);
        mTvArea.setText("东区2栋");
        mBuildings = buildingStrings1;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            area = data.getStringExtra("area");
            mTvArea.setText(area);
        }
    }

    //对应各个寝室
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.area_1) {
            setBackground(0);
            mBuildings = buildingStrings1;
        } else if (id == R.id.area_2) {
            setBackground(1);
            mBuildings = buildingStrings2;

        } else if (id == R.id.area_3) {
            setBackground(2);
            mBuildings = buildingStrings3;

        } else if (id == R.id.area_4) {
            setBackground(3);
            mBuildings = buildingStrings4;
        } else if (id == R.id.area_5) {
            setBackground(4);
            mBuildings = buildingStrings5;
        } else if (id == R.id.area_6) {
            setBackground(5);
            mBuildings = buildingStrings6;
        } else if (id == R.id.tv_area || id == R.id.hint_choose_area) {

            //选择所在区域具体的寝室号码
            Intent intent = new Intent(ElectricityActivity.this, ElectricityAreaOptionActivity.class);
            intent.putExtra("buildings", mBuildings);
            startActivityForResult(intent, 0);
        } else if (id == R.id.et_room) {
        } else if (id == R.id.btn_search) {

            if (mTvArea.getText().length() != 0 && mEtRoom.getText().toString().length() != 0) {
                int index = mTvArea.getText().toString().indexOf("栋");
                if (mTvArea.getText().length() > index + 1) {
                    mQuery = mTvArea.getText().toString().substring(0, 1) + mTvArea.getText().toString().substring(2, index) + "-" + mTvArea.getText().toString().substring(index + 1) + mEtRoom.getText().toString();
                } else {
                    if (mTvArea.getText().toString().substring(0, 3).equals("元宝山")) {
                        mQuery = mTvArea.getText().toString().substring(0, 1) + mTvArea.getText().toString().substring(index - 1, index) + "-" + mEtRoom.getText().toString();
                    } else {
                        mQuery = mTvArea.getText().toString().substring(0, 1) + mTvArea.getText().toString().substring(2, index) + "-" + mEtRoom.getText().toString();
                    }
                }
                PreferenceUtil sp = new PreferenceUtil();
                sp.saveString(PreferenceUtil.ELE_QUERY_STRING, mQuery);

                EleRequestData eleAirRequest = new EleRequestData();
                eleAirRequest.setDor(mQuery);
                eleAirRequest.setType("air");
                EleRequestData eleLightRequest = new EleRequestData();
                eleLightRequest.setDor(mQuery);
                eleLightRequest.setType("light");
                showLoading();
                CampusFactory.getRetrofitService().getElectricity(eleLightRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                            /*
                            subscriber<T>
                            electricityResponse是 Retrofit onSuccess()的一个参数 所以可以这样Lambda化
                             */
                        .subscribe(electricityResponse -> {
                                    if (electricityResponse.code() == 404) {
                                        sp.clearString(PreferenceUtil.ELE_QUERY_STRING);
                                        showErrorSnackbarShort(getString(R.string.ele_room_not_found));
                                    } else {
                                        ElectricityDetailActivity.start(this, mQuery);
                                        this.finish();

                                    }
                                }, throwable -> {
                                    throwable.printStackTrace();
                                }, () -> {
                                    hideLoading();

                                }
                        );
            }
        }

    }

    //选中的时候设置为绿色 其他时候为黑色
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
