package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.SuggestionActivity;
import net.muxi.huashiapp.ui.location.data.PointDetails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.muxistudio.common.util.DimensUtil.dp2px;


public class PointDetailActivity extends ToolbarActivity {

    public CardBanner mBanner;
    public TextView mTvName;
    public TextView mTvDistance;
    public TextView mTvDetail;
    public Button mBtn;

    public PointDetails mPointData;
    private List<ViewHolder<String>> mViewHolders = new ArrayList<>();

    private String[] resId;

    private List<String> resIdList = new ArrayList<>();

    public static void start(Context context, PointDetails point) {
        Intent starter = new Intent(context, PointDetailActivity.class);
        starter.putExtra("detail",point);

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
        mPointData = getIntent().getParcelableExtra("detail");

        initView();
        initBanner();
        setTitle(mPointData.getName());
        mTvName.setText(mPointData.getName());
        mTvDetail.setText(mPointData.getInfo());


    }

    public void initView(){
        mBanner = findViewById(R.id.map_detail_banner);
        mTvName = findViewById(R.id.point_site);
        mTvDistance = findViewById(R.id.point_distance);
        mTvDetail = findViewById(R.id.point_detail);
        mBtn = findViewById(R.id.point_btn);
        mBtn.setOnClickListener( v-> {
            SuggestionActivity.start(getBaseContext());
        });
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        width = metrics.widthPixels;
//        height = metrics.heightPixels;
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mBanner.getLayoutParams();
//        params.height = width;
//        mBanner.setLayoutParams(params);
    }

    private void initBanner(){
        resId = mPointData.getUrl();
        for (int i = 0; i < mPointData.getUrl().length; i++) {
            ViewHolder<String> viewHolder = new ViewHolder<String>() {
                @Override
                public View getView(Context context, String data) {
                    Uri uri = Uri.parse(data);
                    SimpleDraweeView image=new SimpleDraweeView(PointDetailActivity.this);
                    // TODO: 18-8-28 添加动画资源或占位图 
                     image.getHierarchy().setProgressBarImage(R.drawable.ic_holder);
                     image.setImageURI(uri);
                    return image;
                }
            };
            mViewHolders.add(viewHolder);
        }
        resIdList = Arrays.asList(resId);
        mBanner.setViewHolders(mViewHolders,resIdList);
        mBanner.setScrollDuration(5000);
        mBanner.setScrollTime(500);
        mBanner.setAutoScroll(false);
    }
}
