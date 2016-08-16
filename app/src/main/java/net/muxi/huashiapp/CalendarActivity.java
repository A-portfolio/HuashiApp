package net.muxi.huashiapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CalendarData;
import net.muxi.huashiapp.common.net.CampusFactory;
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
    @BindView(R.id.img_calendar)
    ImageView mImgCalendar;
    @BindView(R.id.img_empty)
    ImageView mImgEmpty;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;


//    private PhotoViewAttacher mAttacher;

    private int imgWidth;
    private int imgHeight = 400;

    private Bitmap bitmap;

    private float x, y;

    private boolean isImageShow = false;

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

        if (lastTime != DEFAULT_TIME) {
            picUrl = sp.getString(PreferenceUtil.CALENDAR_ADDRESS);
            File file = new File(App.sContext.getExternalCacheDir().getAbsolutePath() + "/calendar.jpg");
            if (file.exists()) {
                Picasso.with(this).load(file).into(mImgCalendar);
                isImageShow = true;
            } else {
                isImageShow = false;
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
                        if (calendarData.get(0).getUpdate() == lastTime) {
                            if (!isImageShow) {
                                saveCalendarData(calendarData.get(0));
                                downloadBitmap(picUrl);
                                Picasso.with(CalendarActivity.this).load(calendarData.get(0).getImg()).into(mImgCalendar);
                            }
                        } else {
                            saveCalendarData(calendarData.get(0));
                            downloadBitmap(picUrl);
                            Logger.d("download image");
                            Picasso.with(CalendarActivity.this).load(calendarData.get(0).getImg()).into(mImgCalendar);
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
    }

    public void getImgSize(String size) {
        int index = size.indexOf("x");
        String widthStr = size.substring(index + 1, size.length());
        imgWidth = Integer.valueOf(widthStr);
        String heightStr = size.substring(0, index);
        imgHeight = Integer.valueOf(heightStr);
    }

}
