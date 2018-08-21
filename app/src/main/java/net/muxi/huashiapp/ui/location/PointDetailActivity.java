package net.muxi.huashiapp.ui.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;


public class PointDetailActivity extends Activity {

    public CardBanner mBanner;
    public TextView mDetails;
    public PointData mPointData;
    private List<ViewHolder<Integer>> mViewHolders = new ArrayList<>();

    //  测试用图
//    private Integer[] resIds = {R.drawable.a, R.drawable.b,R.drawable.c};

    private List<Integer> resIdList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_details);


        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude",0.0);
        double longitude = intent.getDoubleExtra("longitude",0.0);
        initView(latitude,longitude);
    }



    public void initView(double latitude,double longitude){
        mBanner = findViewById(R.id.map_detail_banner);

    }
}
