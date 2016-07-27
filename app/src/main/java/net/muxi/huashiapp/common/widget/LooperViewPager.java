package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.facebook.drawee.view.DraweeView;

import net.muxi.huashiapp.main.LooperViewPagerAdapter;

import java.util.List;

/**
 * Created by ybao on 16/7/7.
 */
public class LooperViewPager extends ViewPager{

    private LooperViewPagerAdapter mAdapter;
    private int maxSize;
    private Context mContext;
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        private int curPositon = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            curPositon = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE){
                if (curPositon == mAdapter.getCount() - 1){
                    setCurrentItem(1);
                    curPositon = -1;
                }else if (curPositon == 0){
                    setCurrentItem(mAdapter.getCount() - 2);
                    curPositon = -1;
                }
            }
        }
    };

    public LooperViewPager(Context context) {
        super(context);
    }

    public LooperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(){
        this.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void setCurrentItem(int item) {
        this.setCurrentItem(item,false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    public void setAdapter(List<DraweeView> viewList){
        mAdapter = new LooperViewPagerAdapter(viewList);
        super.setAdapter(mAdapter);
    }




}
