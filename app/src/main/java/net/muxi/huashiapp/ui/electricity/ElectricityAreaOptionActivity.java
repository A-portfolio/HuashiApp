package net.muxi.huashiapp.ui.electricity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.studyroom.AreaOptionAdapter;


/**
 * Created by december on 17/3/1.
 */

public class ElectricityAreaOptionActivity extends ToolbarActivity {


    private String[] mBuildings;

    private String area;

    private AreaOptionAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_area);
        initView();
        setTitle("选择楼栋");
        mBuildings = getIntent().getStringArrayExtra("buildings");
        mAdapter = new AreaOptionAdapter(mBuildings);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new AreaOptionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String[] buildings, int position) {
                area = buildings[position];
                Intent intent = new Intent();
                intent.putExtra("area", area);
                setResult(0, intent);
                ElectricityAreaOptionActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(0, intent);
        ElectricityAreaOptionActivity.this.finish();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }
}
