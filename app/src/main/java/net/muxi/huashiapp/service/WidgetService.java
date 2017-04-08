package net.muxi.huashiapp.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.widget.AppWidgetFactory;

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
        return new AppWidgetFactory(this.getApplicationContext(),intent);
    }

}
