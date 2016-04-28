package net.muxi.huashiapp.normal;

import android.animation.TimeAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.widget.TimeTable;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        TimeTable timeTable = new TimeTable(this);
        this.addvi
    }
}
