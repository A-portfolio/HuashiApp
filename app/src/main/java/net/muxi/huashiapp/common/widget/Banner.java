package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.DraweeView;

import java.util.List;

/**
 * Created by ybao on 16/7/6.
 */
public class Banner extends RelativeLayout{

//    private LooperViewPager mViewPager;

    private LooperViewPager mViewPager;
    private Context mContext;
    private List<DraweeView> mViewList;
    //滑动时间
    private int mScrollTime;
    //间隔时间
    private int mIntervalTime;

    List<ImageView> mImageViews;

    public Banner(Context context, List<DraweeView> viewList) {
        this(context,null,viewList);
    }

    public Banner(Context context, AttributeSet attrs,List<DraweeView> viewList) {
        super(context, attrs);
        mContext = context;
        mViewList = viewList;
        initView();
    }

    private void initView() {
        mViewPager = new LooperViewPager(mContext);
        mViewPager.setAdapter(mViewList);
        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        };
    }

    public void setIntervalTime(int time){
        mIntervalTime = time;
    }

    public void setScrollTime(int scrollTime){
        mScrollTime = scrollTime;
    }

}
