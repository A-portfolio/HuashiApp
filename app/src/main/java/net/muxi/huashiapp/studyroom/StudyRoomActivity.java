package net.muxi.huashiapp.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/1.
 */

public class StudyRoomActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.area_choice)
    TextView mAreaChoice;
    @BindView(R.id.areachoice_layout)
    RelativeLayout mAreachoiceLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        ButterKnife.bind(this);

        setTitle("空闲教室");

        mAreachoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRoomActivity.this,RoomDetailActivity.class);
                startActivity(intent);

            }
        });
    }
}
