package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 17/2/4.
 */

public class TableMenuView extends LinearLayout {

    @BindView(R.id.btn_close)
    ImageView mBtnClose;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.tv_setcurweek)
    TextView mTvSetcurweek;

    public TableMenuView(Context context) {
        super(context);
    }

    public TableMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_table_menu, this);
        ButterKnife.bind(this);
    }

    public void setCurweek(int week) {
        String textStr1 = "<font color=\"#000000\">设置当前周</font>";
        String textStr2 = "<font color=\"#7B79FF\">(当前周为" + week + ")</font>";
        mTvSetcurweek.setText(Html.fromHtml(textStr1 + textStr2));
    }

    @OnClick({R.id.tv_add, R.id.tv_setcurweek})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                CourseEditActivity.start(getContext(), true, null);
                break;
            case R.id.tv_setcurweek:

                break;
        }
    }
}
