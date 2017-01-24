package net.muxi.huashiapp.ui.main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by december on 16/5/25.
 */
public class BannerAdapter extends PagerAdapter {

    private List<ImageView> mList;

    public BannerAdapter(List<ImageView> list){
        this.mList = list;

    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position% mList.size()));
        return mList.get(position%mList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position% mList.size()));
    }
}
