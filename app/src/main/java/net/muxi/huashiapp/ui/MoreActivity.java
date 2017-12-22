package net.muxi.huashiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 17/4/25.
 */

public class MoreActivity extends ToolbarActivity {

    @BindView(R.id.btn_feedback)
    Button mBtnFeedback;

    public static void start(Context context) {
        Intent starter = new Intent(context, MoreActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        setTitle("更多");

    }

    @OnClick(R.id.btn_feedback)
    public void onViewClicked() {
        SuggestionActivity.start(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
