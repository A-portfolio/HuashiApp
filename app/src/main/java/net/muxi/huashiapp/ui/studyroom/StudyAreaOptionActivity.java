package net.muxi.huashiapp.ui.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/6.
 */

public class StudyAreaOptionActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.building_recycler_view)
    RecyclerView mBuildingRecyclerView;

    //自习楼栋查询参数
    private String area;

    String[] buildings = {"7号楼", "8号楼"};

    private AreaOptionAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_area);
        ButterKnife.bind(this);
        setTitle("选择自习地点");

        mAdapter = new AreaOptionAdapter(buildings);
        mBuildingRecyclerView.setAdapter(mAdapter);
        mBuildingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new AreaOptionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String[] buildings, int position) {
                area = buildings[position];
                Intent intent = new Intent();
                intent.putExtra("studyArea",area);
                setResult(0,intent);
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
        super.onBackPressed();
        this.finish();
    }



}
