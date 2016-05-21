package net.muxi.huashiapp.main;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by december on 16/5/21.
 */
public class LocalImageHolderView implements Holder<Integer> {
    private ImageView mImageView;
    @Override
    public View createView(Context context) {
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        mImageView.setImageResource(data);

    }
}
