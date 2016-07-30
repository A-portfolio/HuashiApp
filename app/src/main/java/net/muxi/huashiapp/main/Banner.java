package net.muxi.huashiapp.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/5/20.
 */
public class Banner extends AppCompatActivity {


    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_bannertext)
    TextView mTvBannertext;
    @BindView(R.id.points)
    LinearLayout mLinearLayout;
    private List<ImageView> mList;

    private int[] bannerImages = {R.drawable.image1_banner, R.drawable.image1_banner};


    //ViewPager适配器和监听器
    private BannerAdapter mAdapter;
    private BannerListener mbannerListenr;

    //圆圈位置
    private int pointIndex = 0;

    //线程标志
    private boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_main_banner);
        ButterKnife.bind(this);
        initData();
        initAction();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
    }


    private void initAction() {
        mbannerListenr = new BannerListener();
        mViewPager.setOnPageChangeListener(mbannerListenr);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mList.size());
        //用来出发监听器
        mViewPager.setCurrentItem(index);
        mLinearLayout.getChildAt(pointIndex).setEnabled(true);
    }


    private void initData() {
        mList = new ArrayList<ImageView>();
        View view;
        LinearLayout.LayoutParams params;
        ImageView imageView;
        for (int i = 0; i < bannerImages.length; i++) {
            // 设置广告图
            imageView = new ImageView(Banner.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(bannerImages[i]);
            mList.add(imageView);
            // 设置圆圈点
            view = new View(Banner.this);
            params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;
//            view.setBackgroundResource(R.drawable.point_background);
            view.setLayoutParams(params);
            view.setEnabled(false);

            mLinearLayout.addView(view);
        }

        mAdapter = new BannerAdapter(mList);
        mViewPager.setAdapter(mAdapter);
    }


    class BannerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {

            int newPosition = position % bannerImages.length;
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            mLinearLayout.getChildAt(pointIndex).setEnabled(false);
            // 更新标志位
            pointIndex = newPosition;
        }


    }

    @Override
    protected void onDestroy() {
        // 关闭定时器
        isStop = true;
        super.onDestroy();
    }
}





