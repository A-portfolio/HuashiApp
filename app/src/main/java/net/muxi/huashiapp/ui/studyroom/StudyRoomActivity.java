package net.muxi.huashiapp.ui.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.ToastUtil;
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

    private int mWeek;
    private int mDay;

    private String area;

    //查询参数
    private String mQuery;

    private StudyTimePickerDialogFragment mDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        ButterKnife.bind(this);
        setTitle("空闲教室");

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
//                intent = new Intent(StudyRoomActivity.this, StudyAreaOptionActivity.class);
//                startActivity(intent);
                intent = new Intent();
                intent.setClass(StudyRoomActivity.this, StudyAreaOptionActivity.class);
                startActivityForResult(intent,0);

                break;
            case R.id.btn_search:
                if (mTvStudyTime.getText().length() !=0  && mTvStudyArea.getText().length() != 0) {
                    ZhugeUtils.sendEvent("查看空闲教室","查看空闲教室");
                    mQuery = mTvStudyTime.getText().toString() + mTvStudyArea.getText().toString();
                    intent = new Intent(StudyRoomActivity.this, StudyRoomDetailActivity.class);
                    intent.putExtra("query", mQuery);
                    startActivity(intent);
                    mTvStudyTime.setText(null);
                    mTvStudyArea.setText(null);
                    break;
                } else {
                    ToastUtil.showShort("请填写完整信息");
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        area = data.getStringExtra("studyArea");
        mTvStudyArea.setText(area);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
