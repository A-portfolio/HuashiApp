package net.muxi.huashiapp.ui.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.CalendarData;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.FrescoUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.more.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/7/24.
 */
public class CalendarActivity extends ToolbarActivity {

    private SubsamplingScaleImageView mLargeImageView;
    DataSource<CloseableReference<PooledByteBuffer>> imageSource;
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
        picUrl = "https://static.muxixyz.com/calendar/2018-2019%E7%AC%AC%E4%BA%8C%E3%80%81%E4%B8%89%E5%AD%A6%E6%9C%9F%E6%A0%A1%E5%8E%86.png";
       // picUrl=PreferenceUtil.getString(PreferenceUtil.CALENDAR_ADDRESS);

        setTitle("校历");

        ViewTreeObserver vto = mLargeImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLargeImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mLargeImageView.getHeight();
                int w=mLargeImageView.getWidth();
                loadLargeImage(Uri.parse(picUrl),w);

            }
        });


    }


    private void loadLargeImage(Uri url,int w){
        clearOldCalendar();
        FileCache fileCache=Fresco.getImagePipelineFactory().getMainFileCache();
        SimpleCacheKey cacheKey=new SimpleCacheKey(url.toString());
        if (fileCache.hasKey(cacheKey)) {

            FileBinaryResource resource = (FileBinaryResource) fileCache.getResource(cacheKey);
            File file = resource.getFile();
            mLargeImageView.setImage(ImageSource.uri(Uri.fromFile(file)),new ImageViewState(fiTXY(w,file),new PointF(0,0),0));
            return;
        }


        ImageRequestBuilder builder=ImageRequestBuilder.newBuilderWithSource(url);
        ImageRequest request=builder.build();
        imageSource=Fresco.getImagePipeline()
                        .fetchEncodedImage(request,getApplicationContext());

        DataSubscriber<CloseableReference<PooledByteBuffer>>subscriber=new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished()){
                    return;
                }

                FileBinaryResource resource = (FileBinaryResource) fileCache.getResource(cacheKey);
                File file = resource.getFile();
                mLargeImageView.setImage(ImageSource.uri(Uri.fromFile(file)),new ImageViewState(fiTXY(w,file),new PointF(0,0),0));




            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                ToastUtil.showLong("加载校历出错!");
            }
        };

        imageSource.subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
    }

    public float  fiTXY(int width,File file)  {
        BitmapFactory.Options option=new BitmapFactory.Options();
        option.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(file.getPath(), option);
        float scale=(float) width/option.outWidth;

        return scale;
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
        mLargeImageView=findViewById(R.id.largeImageView);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
