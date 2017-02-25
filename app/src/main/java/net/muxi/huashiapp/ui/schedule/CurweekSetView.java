package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.PreferenceUtil;

import butterknife.OnClick;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 */

public class CurweekSetView extends RelativeLayout {

    public CurweekSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            this.setVisibility(VISIBLE);
            LayoutInflater.from(context).inflate(R.layout.view_table_guide, this);
        }
    }

    @OnClick(R.id.btn_set_curweek)
    public void onClick() {
        CurweekSetActivity.start(getContext());
    }
}
