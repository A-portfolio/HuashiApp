package net.muxi.huashiapp.ui.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.muxistudio.appcommon.appbase.ToolbarActivity;

import net.muxi.huashiapp.R;


/**
 * Created by december on 17/2/6.
 */

public class StudyAreaOptionActivity extends ToolbarActivity {

    //自习楼栋查询参数
    private String area;

    String[] buildings = {"7号楼", "8号楼"};

    private AreaOptionAdapter mAdapter;
    private RecyclerView mBuildingRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_area);
        initView();
        setTitle("选择自习地点");

        mAdapter = new AreaOptionAdapter(buildings);
        mBuildingRecyclerView.setAdapter(mAdapter);
        mBuildingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new AreaOptionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String[] buildings, int position) {
                area = buildings[position];
                Intent intent = new Intent();
                intent.putExtra("studyArea", area);
                setResult(0, intent);
                StudyAreaOptionActivity.this.finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(0, intent);
        this.finish();
    }


    private void initView() {
        mBuildingRecyclerView = findViewById(R.id.building_recycler_view);
    }
}
