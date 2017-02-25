package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.PreferenceUtil;

import butterknife.BindView;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 */

public class RefreshView extends RelativeLayout {

    @BindView(R.id.drawee)
    SimpleDraweeView mDrawee;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.layout_refresh)
    FrameLayout mLayoutRefresh;

    private DraweeController mDraweeController;

    enum Status {
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        REFRESH_FINISHED
    }

    public Status status = Status.REFRESH_FINISHED;

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        if (!PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            setVisibility(VISIBLE);
            LayoutInflater.from(getContext()).inflate(R.layout.view_table_refresh, this);

            Uri uri = Uri.parse("asset://net.muxi.huashiapp/table_loading.gif");
            mDraweeController = Fresco.newDraweeControllerBuilder().setUri(
                    uri).setAutoPlayAnimations(true).build();

        }
    }

    public void setTipText(String s){
        mTvTip.setText(s);

    }

    public void startRefresh(){
        mDrawee.setController(mDraweeController);
    }

    public void setReadyRefresh(){
        Uri uri = Uri.parse("res:/" + R.drawable.table_loading_final);
        mDrawee.setImageURI(uri);
    }

}
