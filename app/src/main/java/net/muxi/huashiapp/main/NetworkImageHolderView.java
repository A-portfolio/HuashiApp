package net.muxi.huashiapp.main;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import net.muxi.huashiapp.R;

/**
 * Created by december on 16/5/21.
 */
public class NetworkImageHolderView implements Holder<Integer> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(R.drawable.ic_default_adimage);
//        ImageLoader.getInstance().displayImage(data,imageView);

    }
}
