package net.muxi.huashiapp.common.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.LinearLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/7.
 */
public class AboutActivity extends ToolbarActivity {


    @BindView(R.id.cb_text)
    AppCompatCheckBox mCbText;
    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

//        mBanne.
        Drawable drawable = getResources().getDrawable(R.drawable.selector_cb);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        mCbText.setCompoundDrawables(null, null, drawable, null);

    }
}
