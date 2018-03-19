package net.muxi.huashiapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.muxistudio.appcommon.appbase.ToolbarActivity;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 16/8/4.
 */
public class InfoActivity extends ToolbarActivity {


    private InfoAdapter mAdapter;
    private static final String[] groupString = {"区域", "建筑"};

    //西区对应的建筑
    private static final String[][] childStrings1 = {
            {"西区", "东区", "元宝山", "南湖"},
            {"1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋"}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }


}
