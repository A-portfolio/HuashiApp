package net.muxi.huashiapp.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.widget.TimeTable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity{

    @Bind(R.id.schedule_linearlayout)
    LinearLayout mScheduleLinearlayout;
    private TimeTable mTimeTable;
    private float mx,my;
    private VScrollView vScroll;
    private HScrollView hScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        mTimeTable = new TimeTable(this);
        FrameLayout.LayoutParams timeTableParams = new
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleLinearlayout.addView(mTimeTable);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
