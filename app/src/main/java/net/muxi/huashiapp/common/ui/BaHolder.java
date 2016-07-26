package net.muxi.huashiapp.common.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.common.util.Logger;

/**
 * Created by ybao on 16/7/25.
 */
public class BaHolder implements Holder<String>{

    private SimpleDraweeView mDraweeView;

    @Override
    public View createView(Context context) {
        mDraweeView = new SimpleDraweeView(context);
        mDraweeView.setScaleType(SimpleDraweeView.ScaleType.FIT_XY);
        Logger.d("create BaHolder");
        return mDraweeView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        mDraweeView.setImageURI(Uri.parse(data));

    }
}
