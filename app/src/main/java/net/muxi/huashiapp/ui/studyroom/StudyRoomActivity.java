package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 17/2/1.
 */

public class StudyRoomActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_study_time)
    TextView mTvStudyTime;
    @BindView(R.id.tv_study_area)
    TextView mTvStudyArea;
    @BindView(R.id.btn_search)
    Button mBtnSearch;

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyRoomActivity.class);
        context.startActivity(starter);
    }

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
        ButterKnife.bind(this);
        setTitle("空闲教室");

        sp = new PreferenceUtil();

        initView();

    }

    private void initView() {
        mTvStudyTime.setText("第13周周二");
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
            StudyRoomCorrectView studyRoomCorrectView = new StudyRoomCorrectView(StudyRoomActivity.this, "work");
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
            studyRoomCorrectView.startAnimation(animation);
            setContentView(studyRoomCorrectView);


        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_study_time, R.id.tv_study_area, R.id.btn_search})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_study_time:
                mDialogFragment = StudyTimePickerDialogFragment.newInstance(
                        mWeek,
                        mDay
                );
                mDialogFragment.show(getSupportFragmentManager(), "picker_time");
                mDialogFragment.setOnPositiveButtonClickListener((week, day) -> {
                    mWeek = week;
                    mDay = day;
                    mTvStudyTime.setText(String.format("第%d周周%s", mWeek + 1, Constants.WEEKDAYS[mDay]));
                });
                break;
            case R.id.tv_study_area:
                intent = new Intent();
                intent.setClass(StudyRoomActivity.this, StudyAreaOptionActivity.class);
                startActivityForResult(intent, 0);

                break;
            case R.id.btn_search:
                if (mTvStudyTime.getText().length() != 0 && mTvStudyArea.getText().length() != 0) {
                    mQuery = mTvStudyTime.getText().toString() + mTvStudyArea.getText().toString();
                    sp.saveString(PreferenceUtil.STUDY_ROOM_QUERY_STRING,mQuery);
                    StudyRoomDetailActivity.start(StudyRoomActivity.this, mQuery);
                    this.finish();
                    break;
                } else {
                    showSnackbarShort("请填写完整信息");
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
        StudyRoomCorrectView view = new StudyRoomCorrectView(StudyRoomActivity.this, "work");
        if (getWindow().getDecorView().equals(view)) {
            view.removeAllViews();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
