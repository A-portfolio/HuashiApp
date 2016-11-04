package net.muxi.huashiapp.common.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.widget.AppWidgetFactory;

import java.util.ArrayList;


/**
 * Created by ybao on 16/11/2.
 */

public class WidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        HuaShiDao dao = new HuaShiDao();
        ArrayList<Course> allCourses = (ArrayList<Course>) dao.loadAllCourses();
        intent.putParcelableArrayListExtra("course",allCourses);
        Logger.d(" getviewFactory ");
        return new AppWidgetFactory(this.getApplicationContext(),intent);
    }

}
