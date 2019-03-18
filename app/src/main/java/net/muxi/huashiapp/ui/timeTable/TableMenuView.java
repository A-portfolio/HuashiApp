package net.muxi.huashiapp.ui.timeTable;

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

import com.muxistudio.appcommon.Constants;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/4.
 */

public class TableMenuView extends FrameLayout {

    private LinearLayout mMenuLayout;
    private ImageView mBtnClose;
    private TextView mTvAuditClass;
    private TextView mTvAdd;

    public TableMenuView(Context context) {
        this(context, null);
    }

    public TableMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_table_menu, this);
        initView();
        this.setVisibility(INVISIBLE);
        this.setOnClickListener(v -> {
            dismiss();
        });
        mBtnClose.setOnClickListener(v -> {
            dismiss();
        });
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

    //这里有添加新课程和修改当前周设置的入口
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_audit_class){
            CourseAuditSearchActivity.start(getContext());
        }else if (id == R.id.tv_add){
            CourseEditActivity.start(getContext(), true, null);
            dismiss();
        }
    }

    private void initView() {
        mMenuLayout = findViewById(R.id.menu_layout);
        mBtnClose = findViewById(R.id.btn_close);
        mTvAuditClass = findViewById(R.id.tv_audit_class);
        mTvAdd = findViewById(R.id.tv_add);
        mTvAdd.setOnClickListener(v -> onClick(v));
        mTvAuditClass.setOnClickListener(v -> onClick(v));
    }
}
