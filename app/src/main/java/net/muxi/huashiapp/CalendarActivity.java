package net.muxi.huashiapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CalendarData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.FrescoUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;

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

    @BindView(R.id.drawee)
    SimpleDraweeView mDrawee;

    DraweeView draweeView;

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

        if (lastTime != DEFAULT_TIME){
            picUrl = sp.getString(PreferenceUtil.CALENDAR_ADDRESS);
            mDrawee.setImageURI(Uri.parse(picUrl));
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
                    public void onNext(List<CalendarData> calendarData) {
                        if (calendarData.get(0).getUpdate() == lastTime) {
                            mDrawee.setImageURI(Uri.parse(calendarData.get(0).getImg()));
                        } else {
                            saveCalendarData(calendarData.get(0));
                            Logger.d(calendarData.get(0).getImg());
                            Logger.d(picUrl);
                            mDrawee.setImageURI(Uri.parse(picUrl));
                            FrescoUtil.savePicture(picUrl, CalendarActivity.this, "calendar");
                        }
                    }
                });
    }

    //保存 calendar 的相关信息
    private void saveCalendarData(CalendarData calendarData) {
        lastTime = calendarData.getUpdate();
        picUrl = calendarData.getImg();
        sp.saveLong(PreferenceUtil.CALENDAR_UPDATE, lastTime);
        sp.saveString(PreferenceUtil.CALENDAR_ADDRESS, picUrl);
    }
}
