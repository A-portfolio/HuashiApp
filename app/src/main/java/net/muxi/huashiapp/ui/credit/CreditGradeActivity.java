package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditGradeActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    public static void start(Context context, int start, int end) {
        Intent starter = new Intent(context, CreditGradeActivity.class);
        starter.putExtra("start", start);
        starter.putExtra("end", end);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_grade);
        ButterKnife.bind(this);
    }
}
