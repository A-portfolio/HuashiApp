package net.muxi.huashiapp.ui.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.CalendarData;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.FrescoUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.more.ShareDialog;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/7/24.
 */
public class CalendarActivity extends ToolbarActivity {

    private ScrollView mScrollView;
    private SimpleDraweeView mDraweeView;

    private String picUrl;


    public static void start(Context context) {
        Intent starter = new Intent(context, CalendarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        //picUrl = "https://static.muxixyz.com/calendar/2018-2019%E7%AC%AC%E4%BA%8C%E3%80%81%E4%B8%89%E5%AD%A6%E6%9C%9F%E6%A0%A1%E5%8E%86.png";
        picUrl=PreferenceUtil.getString(PreferenceUtil.CALENDAR_ADDRESS);

        setTitle("校历");
        loadImage(Uri.parse(picUrl));





    }



    private void loadImage(Uri uri){
        clearOldCalendar();

        ImageRequestBuilder builder=ImageRequestBuilder.newBuilderWithSource(uri);
        builder.setResizeOptions(new ResizeOptions(760,3246));
        ImageRequest request=builder.build();
        DraweeController controller=Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        mDraweeView.setAspectRatio((float)760/(float)3246);
        mDraweeView.setController(controller);

    }


    private void clearOldCalendar(){
        String oldUrl=PreferenceUtil.getString(PreferenceUtil.OLD_CALENDAR_ADDRESS,"first");
        if (oldUrl.equals("first")){
            PreferenceUtil.saveString(PreferenceUtil.OLD_CALENDAR_ADDRESS,picUrl);
            return;
        }else {
            if (oldUrl.equals(picUrl))
                return;
            else {
                ImagePipeline imagePipeline= Fresco.getImagePipeline();
                imagePipeline.evictFromCache(Uri.parse(oldUrl));

            }
        }
    }












    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_option) {
            ShareDialog shareDialog = ShareDialog.newInstance(1);
            shareDialog.show(getSupportFragmentManager(), "share_dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mScrollView = findViewById(R.id.scroll_view);
        mDraweeView = findViewById(R.id.drawee);
    }
}
