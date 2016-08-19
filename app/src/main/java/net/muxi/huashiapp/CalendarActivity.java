package net.muxi.huashiapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;
import com.squareup.picasso.Picasso;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CalendarData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.FrescoUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/7/24.
 */
public class CalendarActivity extends ToolbarActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //    @BindView(R.id.img_calendar)
//    ImageView mImgCalendar;
    @BindView(R.id.img_empty)
    ImageView mImgEmpty;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    RecyclerView

    private DraweeView mDraweeView;

    private int imgWidth;
    private int imgHeight;

    private Bitmap bitmap;

    //上次距离最近的时间
    private long lastTime;
    private String picUrl;
    private PreferenceUtil sp;
    private static final long DEFAULT_TIME = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        sp = new PreferenceUtil();
        lastTime = sp.getLong(PreferenceUtil.CALENDAR_UPDATE);
        setTitle("校历");

        //如果有本地缓存就从中读取
        if (lastTime != DEFAULT_TIME) {
            getImgSize(sp.getString(PreferenceUtil.CALENDAR_SIZE));
            if (imgWidth != 0) {
                mDraweeView = new DraweeView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) ((long) (imgHeight) / (long) (imgWidth) * DimensUtil.getScreenWidth())
                );
                mScrollView.addView(mDraweeView, params);
                mDraweeView.setImageURI(Uri.parse(sp.getString(PreferenceUtil.CALENDAR_ADDRESS)));
            }
        }
        if (NetStatus.isConnected()) {
            updateImage();
        } else {
            if (lastTime == DEFAULT_TIME) {
                //当第一次没联网时则显示图片无法显示
                setImageNotFound();
            }
        }


    }

    // TODO: 16/7/24 设置图片无法显示
    private void setImageNotFound() {
        mImgEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    //更新图片信息
    private void updateImage() {
        CampusFactory.getRetrofitService().getCalendar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<CalendarData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final List<CalendarData> calendarData) {
                        if (calendarData.get(0).getUpdate() != lastTime) {
                            saveCalendarData(calendarData.get(0));
                            getImgSize(calendarData.get(0).getSize());
                            mScrollView.removeAllViews();
                            mDraweeView = new DraweeView(CalendarActivity.this);
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    (int) ((long) (imgHeight) / (long) (imgWidth) * DimensUtil.getScreenWidth())
                            );
                            mScrollView.addView(mDraweeView, params);
                            mDraweeView.setImageURI(Uri.parse(calendarData.get(0).getImg()));
                            FrescoUtil.savePicture(picUrl,CalendarActivity.this,"calendar");
                        }
                    }
                });
    }

    public void downloadBitmap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = Picasso.with(CalendarActivity.this)
                            .load(url)
                            .get();
                    File dir = new File(App.sContext.getExternalCacheDir().getAbsolutePath());
                    if (dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File(App.sContext.getExternalCacheDir().getAbsolutePath() + "/calendar.jpg");
                    Logger.d(App.sContext.getCacheDir().getAbsolutePath());
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    ostream.flush();
                    ostream.close();
                    Logger.d("create file" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //保存 calendar 的相关信息
    private void saveCalendarData(CalendarData calendarData) {
        lastTime = calendarData.getUpdate();
        picUrl = calendarData.getImg();
        sp.saveLong(PreferenceUtil.CALENDAR_UPDATE, lastTime);
        sp.saveString(PreferenceUtil.CALENDAR_ADDRESS, picUrl);
        sp.saveString(PreferenceUtil.CALENDAR_SIZE, calendarData.getSize());
    }

    public void getImgSize(String size) {
        int index = size.indexOf("x");
        String widthStr = size.substring(index + 1, size.length());
        imgWidth = Integer.valueOf(widthStr);
        String heightStr = size.substring(0, index);
        imgHeight = Integer.valueOf(heightStr);
    }

}
