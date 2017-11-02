package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 17/2/4.
 */

//手动设置周数！
public class TableMenuView extends FrameLayout {

    @BindView(R.id.btn_close)
    ImageView mBtnClose;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.tv_setcurweek)
    TextView mTvSetcurweek;
    @BindView(R.id.menu_layout)
    LinearLayout mMenuLayout;

    public TableMenuView(Context context) {
        this(context,null);
    }

    public TableMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_table_menu, this);
        ButterKnife.bind(this);
        this.setVisibility(INVISIBLE);
        this.setOnClickListener(v -> {
            dismiss();
        });
        mBtnClose.setOnClickListener(v -> {
            dismiss();
        });
    }
    public void setCurweek(int week) {
        String textStr1 = "<font color=\"#000000\">设置当前周</font>";
        String textStr2 = "<font color=\"#7B79FF\">(当前周设置为" + week + ")</font>";
        mTvSetcurweek.setText(Html.fromHtml(textStr1 + textStr2));
    }
    public void show() {
        this.setVisibility(VISIBLE);
        TranslateAnimation a = new TranslateAnimation(0, 0, -mMenuLayout.getHeight(), 0);
        a.setDuration(Constants.ANIMATION_DURATION);
        a.setFillAfter(true);
        mMenuLayout.startAnimation(a);
        this.setFocusable(true);
    }
    public void dismiss() {
        TranslateAnimation a = new TranslateAnimation(0, 0, 0, -mMenuLayout.getHeight());
        a.setDuration(Constants.ANIMATION_DURATION);
        a.setFillAfter(true);
        mMenuLayout.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.setFocusable(false);
    }
    @OnClick({R.id.tv_add, R.id.tv_setcurweek})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                CourseEditActivity.start(getContext(), true, null);
                dismiss();
                break;
            case R.id.tv_setcurweek:
                CurweekSetActivity.start(getContext());
                dismiss();
                break;
        }
    }
}
