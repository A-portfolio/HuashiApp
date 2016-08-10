package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.FrescoUtil;

/**
 * Created by ybao on 16/8/10.
 */
public class LoadingView extends RelativeLayout{

    private SimpleDraweeView mDraweeView;
    private Context mContext;

    public LoadingView(Context context) {
        this(context,null,0);
        mContext = context;
        initLoading();
        this.setBackgroundColor(getResources().getColor(R.color.color_white_loading));
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLoading();
        this.setBackgroundColor(getResources().getColor(R.color.color_white_loading));
    }

    private void initLoading() {
        mDraweeView = new SimpleDraweeView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                DimensUtil.dp2px(100),
                DimensUtil.dp2px(100)
        );
        params.addRule(CENTER_HORIZONTAL);
        params.setMargins(0,DimensUtil.dp2px(8),0,0);
        this.addView(mDraweeView,params);
        FrescoUtil.setLoading(mDraweeView);
    }
}
