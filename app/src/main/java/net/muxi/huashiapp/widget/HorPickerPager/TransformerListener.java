package net.muxi.huashiapp.widget.HorPickerPager;

import android.support.v4.view.ViewPager;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/28
 */

public class TransformerListener implements ViewPager.OnPageChangeListener {

    private HorPickerPagerAdapter mPagerAdapter;

    public TransformerListener(HorPickerPagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
