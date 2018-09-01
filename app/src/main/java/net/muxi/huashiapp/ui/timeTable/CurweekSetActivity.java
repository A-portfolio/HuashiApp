package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.event.CurWeekChangeEvent;

import java.util.Locale;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.utils.TimeTableUtil;


/**
 * Created by ybao on 17/2/5.
 */


public class CurweekSetActivity extends ToolbarActivity {

    private ListView mLv;

    public static void start(Context context) {
        Intent starter = new Intent(context, CurweekSetActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curweek_set);
        initView();
        setTitle("选择当前周");
        String[] s = new String[Constants.WEEKS_LENGTH];
        for (int i = 0; i < Constants.WEEKS_LENGTH; i++) {
            if (i < 9) {
                s[i] = String.format(Locale.CHINESE,"第0%d周", i + 1);
            } else {
                s[i] = String.format(Locale.CHINESE,"第%d周", i + 1);
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_curweek_set, s);
        mLv.setDivider(null);
        mLv.setAdapter(arrayAdapter);
        mLv.setOnItemClickListener((adapterView, view, i, l) -> {
            TimeTableUtil.saveCurWeek(i + 1);
            RxBus.getDefault().send(new CurWeekChangeEvent());
            CurweekSetActivity.this.finish();
        });
    }

    private void initView() {
        mLv = findViewById(R.id.lv);
    }
}
