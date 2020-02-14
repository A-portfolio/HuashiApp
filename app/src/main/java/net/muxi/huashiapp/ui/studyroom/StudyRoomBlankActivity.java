package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.common.util.DateUtil;

import net.muxi.huashiapp.R;

import java.util.Date;


/**
 * Created by december on 17/2/15.
 */

public class StudyRoomBlankActivity extends ToolbarActivity {


    private RelativeLayout mStudyBlankLayout;
    private Button mIvToday;
    private TextView mTvToday;
    private View mDivider;

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyRoomBlankActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom_blank);

        initView();
        setTitle("空闲教室");

        String today = DateUtil.getToday(new Date()) + DateUtil.getWeek(new Date());
        mTvToday.setText(today);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_studyroom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_correct) {
            StudyRoomCorrectView studyRoomCorrectView = new StudyRoomCorrectView(StudyRoomBlankActivity.this);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
            studyRoomCorrectView.startAnimation(animation);
            mStudyBlankLayout.addView(studyRoomCorrectView);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        StudyRoomCorrectView view = new StudyRoomCorrectView(StudyRoomBlankActivity.this);
        if (getWindow().getDecorView().equals(view)) {
            mStudyBlankLayout.removeView(view);
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        mStudyBlankLayout = findViewById(R.id.study_blank_layout);
        mIvToday = findViewById(R.id.iv_today);
        mTvToday = findViewById(R.id.tv_today);
        mDivider = findViewById(R.id.divider);
    }
}
