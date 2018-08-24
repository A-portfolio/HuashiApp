package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;

import net.muxi.huashiapp.R;

import net.muxi.huashiapp.ui.location.data.PointDetails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PointDetailActivity extends ToolbarActivity {

    public CardBanner mBanner;
    public TextView mTvName;
    public TextView mTvDistance;
    public TextView mTvDetail;
    public Button mBtn;

    public PointDetails mPointData;
    private List<ViewHolder<Integer>> mViewHolders = new ArrayList<>();

    //  测试用图
    private Integer[] resIds = {R.drawable.a, R.drawable.aa, R.drawable.aaa};

    private List<Integer> resIdList = new ArrayList<>();

    public static void start(Context context, PointDetails pointDetail) {
        Intent starter = new Intent(context, PointDetailActivity.class);
        starter.putExtra("name",pointDetail.getName());
        starter.putExtra("info",pointDetail.getInfo());

        context.startActivity(starter);
    }
    public static void start(Context context){
        Intent starter = new Intent(context,PointDetailActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_details);

        initView();
        initBanner();
        String name = getIntent().getStringExtra("name");
        String detail = getIntent().getStringExtra("info");
        setTitle(name);
        mTvName.setText(name);
        mTvDetail.setText(detail);

    }

    public void initView(){
        mBanner = findViewById(R.id.map_detail_banner);
        mTvName = findViewById(R.id.point_site);
        mTvDistance = findViewById(R.id.point_distance);
        mTvDetail = findViewById(R.id.point_detail);
        mBtn = findViewById(R.id.point_btn);
    }

    private void initBanner(){
        for (int i = 0;i < 3;i ++) {
            ViewHolder<Integer> viewHolder = new ViewHolder<Integer>() {
                @Override
                public View getView(Context context, Integer data) {
                    ImageView imageView = new ImageView(PointDetailActivity.this);
                    imageView.setImageResource(data);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                }
            };
            mViewHolders.add(viewHolder);
        }
        resIdList = Arrays.asList(resIds);
        mBanner.setViewHolders(mViewHolders,resIdList);
        mBanner.setScrollDuration(5000);
        mBanner.setScrollTime(500);
        mBanner.setAutoScroll(false);
    }
}
