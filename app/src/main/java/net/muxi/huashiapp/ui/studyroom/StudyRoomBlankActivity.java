package net.muxi.huashiapp.ui.studyroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/15.
 */

public class StudyRoomBlankActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ic_today)
    ImageView mIcToday;
    @BindView(R.id.tv_weekday)
    TextView mTvWeekday;
    @BindView(R.id.iv_blank)
    ImageView mIvBlank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom_blank);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_studyroom,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_correct){
            StudyRoomCorrectView studyRoomCorrectView = new StudyRoomCorrectView(StudyRoomBlankActivity.this);
            setContentView(studyRoomCorrectView);
        }
        return super.onOptionsItemSelected(item);
    }
}
