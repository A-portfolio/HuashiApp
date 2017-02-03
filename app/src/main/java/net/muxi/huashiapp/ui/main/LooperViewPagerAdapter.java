package net.muxi.huashiapp.ui.main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.DraweeView;

import net.muxi.huashiapp.common.data.BannerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/7/7.
 */
public class LooperViewPagerAdapter extends PagerAdapter{

    //最初的数据
    private List<BannerData> mBannerDatas;
    //修改之后的 list
    private List<BannerData> realBannerDatas;
    private List<DraweeView> mViewList;
    private int mSize;

    public LooperViewPagerAdapter(List<DraweeView> draweeViews) {
        super();
        mViewList = new ArrayList<>();
        mViewList.add(draweeViews.get(draweeViews.size() - 1));
        mViewList.addAll(draweeViews);
        mViewList.add(draweeViews.get(0));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }


}
