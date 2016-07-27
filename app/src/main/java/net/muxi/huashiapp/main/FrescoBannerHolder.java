package net.muxi.huashiapp.main;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.common.util.Logger;

/**
 * Created by ybao on 16/7/25.
 */
public class FrescoBannerHolder implements Holder<String>{

    private SimpleDraweeView mDraweeView;

    @Override
    public View createView(Context context) {
        mDraweeView = new SimpleDraweeView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDraweeView.setLayoutParams(params);
        mDraweeView.setScaleType(SimpleDraweeView.ScaleType.FIT_CENTER);
        Logger.d("create banner");
        return mDraweeView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        mDraweeView.setImageURI(Uri.parse(data));
        Logger.d("save banner success");
        Logger.d(data);
    }
}
