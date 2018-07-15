package net.muxi.huashiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.muxistudio.appcommon.appbase.ToolbarActivity;

import net.muxi.huashiapp.R;


/**
 * Created by december on 17/4/25.
 */

public class MoreActivity extends ToolbarActivity {

    private Button mBtnFeedback;

    public static void start(Context context) {
        Intent starter = new Intent(context, MoreActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        setTitle("更多");

        initView();
    }

    public void onViewClicked() {
        SuggestionActivity.start(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        mBtnFeedback = findViewById(R.id.btn_feedback);
        mBtnFeedback.setOnClickListener(v -> onViewClicked());
    }
}
