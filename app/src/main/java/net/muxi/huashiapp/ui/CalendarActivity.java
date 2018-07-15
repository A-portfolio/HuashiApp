package net.muxi.huashiapp.ui;

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

import com.facebook.drawee.view.SimpleDraweeView;
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
    private SimpleDraweeView mDrawee;
    private ImageView mImgEmpty;
    private TextView mTvEmpty;

    public static void start(Context context) {
        Intent starter = new Intent(context, CalendarActivity.class);
        context.startActivity(starter);
    }


    private SimpleDraweeView mDraweeView;

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
        initView();
        mDraweeView = (SimpleDraweeView) findViewById(R.id.drawee);

        sp = new PreferenceUtil();
        lastTime = sp.getLong(PreferenceUtil.CALENDAR_UPDATE);
        setTitle("校历");

        //如果有本地缓存就从中读取
        if (lastTime != DEFAULT_TIME) {
            getImgSize(sp.getString(PreferenceUtil.CALENDAR_SIZE));
            if (imgWidth != 0) {
                setCalendarDrawee(sp.getString(PreferenceUtil.CALENDAR_ADDRESS));
            }
        }
        if (NetUtil.isConnected()) {
            updateImage();
        } else {
            if (lastTime == DEFAULT_TIME) {
                //当第一次没联网时则显示图片无法显示
                setImageNotFound();
            }
        }
    }

    private void setImageNotFound() {
        mImgEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    //更新图片信息
    private void updateImage() {
        CampusFactory.getRetrofitService().getCalendar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<CalendarData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final CalendarData calendarData) {
                        if (lastTime != calendarData.getUpdate()) {
                            saveCalendarData(calendarData);
                            getImgSize(calendarData.getSize());
                            setCalendarDrawee(calendarData.getImg());
                            FrescoUtil.savePicture(picUrl, CalendarActivity.this, calendarData.getFilename());
                        }
                    }
                });
    }

    /**
     * 设置校历的图片
     *
     * @param url
     */
    public void setCalendarDrawee(String url) {
        if (imgHeight != 0) {
            float ratio = (float) (imgWidth) / (float) (imgHeight);
            Logger.d(ratio + "");
            mDraweeView.setAspectRatio(ratio);
            mDraweeView.setImageURI(Uri.parse(url));
        }
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
        if(index != -1) {
            String heightStr = size.substring(index + 1, size.length());
            String widthStr = size.substring(0, index);
            imgWidth = Integer.valueOf(widthStr);
            imgHeight = Integer.valueOf(heightStr);
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
        mDrawee = findViewById(R.id.drawee);
        mImgEmpty = findViewById(R.id.img_empty);
        mTvEmpty = findViewById(R.id.tv_empty);
    }
}
