package net.muxi.huashiapp.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.widget.AppWidgetFactory;


/**
 * Created by ybao on 16/11/2.
 * 开启桌面挂件时触发
 */

public class WidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Logger.d("widget service trigger");
        return new AppWidgetFactory(this.getApplicationContext(),intent);
    }

}
