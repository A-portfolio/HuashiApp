package net.muxi.huashiapp.ui.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
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

import okhttp3.OkHttpClient;
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
    private String cachePath;

    public static void start(Context context) {
        Intent starter = new Intent(context, CalendarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        setTitle("校历");

        picUrl=PreferenceUtil.getString(PreferenceUtil.CALENDAR_ADDRESS);
        cachePath=getDiskCacheDir(this);

        ViewTreeObserver vto = mLargeImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLargeImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mLargeImageView.getHeight();
                int w=mLargeImageView.getWidth();
                loadLargeImage(picUrl,w);

            }
        });


    }


    private void loadLargeImage(String url,int w){
      //  String cacheKey= Base64.encodeToString();
    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
    public float  fiTXY(int width,File file)  {
        BitmapFactory.Options option=new BitmapFactory.Options();
        option.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(file.getPath(), option);
        float scale=(float) width/option.outWidth;

        return scale;
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
