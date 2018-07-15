package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.utils.TimeTableUtil;

import java.util.Date;


/**
 * Created by december on 17/2/1.
 */

public class StudyRoomActivity extends ToolbarActivity {

    private RelativeLayout mStudyLayout;
    private ImageView mIvTimeChoice;
    private TextView mTvTime;
    private TextView mTvStudyTime;
    private ImageView mIvAreaChoice;
    private TextView mTvArea;
    private TextView mTvStudyArea;
    private Button mBtnSearch;

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyRoomActivity.class);
        context.startActivity(starter);
    }

    private static String DAYS[] = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private int mWeek;
    private int mDay;
    private String area;
    //查询参数
    private String mQuery;
    private StudyTimePickerDialogFragment mDialogFragment;
    private PreferenceUtil sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        setTitle("空闲教室");
        sp = new PreferenceUtil();
        mWeek = TimeTableUtil.getCurWeek();
        mDay = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        initView();

    }

    private void initView() {
        mStudyLayout = findViewById(R.id.study_layout);
        mIvTimeChoice = findViewById(R.id.iv_time_choice);
        mTvTime = findViewById(R.id.tv_time);
        mTvStudyTime = findViewById(R.id.tv_study_time);
        mIvAreaChoice = findViewById(R.id.iv_area_choice);
        mTvArea = findViewById(R.id.tv_area);
        mTvStudyArea = findViewById(R.id.tv_study_area);
        mBtnSearch = findViewById(R.id.btn_search);
        mTvTime.setOnClickListener(v -> onClick(v));
        mTvStudyTime.setOnClickListener(v -> onClick(v));
        mTvArea.setOnClickListener(v -> onClick(v));
        mTvStudyArea.setOnClickListener(v -> onClick(v));
        mBtnSearch.setOnClickListener(v -> onClick(v));
        mTvStudyTime.setText("第" + mWeek + "周" + DAYS[mDay - 1]);
        mTvStudyArea.setText("7号楼");

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
            StudyRoomCorrectView studyRoomCorrectView = new StudyRoomCorrectView(StudyRoomActivity.this);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
            studyRoomCorrectView.startAnimation(animation);
            mStudyLayout.addView(studyRoomCorrectView);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if (id == R.id.tv_time || id == R.id.tv_study_time) {
            mDialogFragment = StudyTimePickerDialogFragment.newInstance(mWeek, mDay);
            mDialogFragment.show(getSupportFragmentManager(), "picker_time");
            mDialogFragment.setOnPositiveButtonClickListener((week, day) -> {
                mTvStudyTime.setText(String.format("第%d周周%s", week + 1, Constants.WEEKDAYS[day]));
            });
        } else if (id == R.id.tv_area || id == R.id.tv_study_area) {
            intent = new Intent();
            intent.setClass(StudyRoomActivity.this, StudyAreaOptionActivity.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.btn_search) {
            if (mTvStudyTime.getText().length() != 0 && mTvStudyArea.getText().length() != 0) {
                mQuery = mTvStudyTime.getText().toString() + mTvStudyArea.getText().toString();
                sp.saveString(PreferenceUtil.STUDY_ROOM_QUERY_STRING, mQuery);
                StudyRoomDetailActivity.start(StudyRoomActivity.this, mQuery);
            } else {
                showErrorSnackbarShort("请填写完整信息");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            area = data.getStringExtra("studyArea");
            mTvStudyArea.setText(area);
        }
    }

    @Override
    public void onBackPressed() {
        StudyRoomCorrectView view = new StudyRoomCorrectView(StudyRoomActivity.this);
        if (getWindow().getDecorView().equals(view)) {
            mStudyLayout.removeView(view);
        } else {
            super.onBackPressed();
        }
    }

}
