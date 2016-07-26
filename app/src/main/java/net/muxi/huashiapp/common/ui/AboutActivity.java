package net.muxi.huashiapp.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.net.CampusFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/7/7.
 */
public class AboutActivity extends ToolbarActivity {

    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;
    @BindView(R.id.banner)
    ConvenientBanner mBanner;

    private List<BannerData> mBannerDatas;
    private List<String> imgUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mBannerDatas = new ArrayList<>();
        imgUrls = new ArrayList<>();
        ButterKnife.bind(this);
        CampusFactory.getRetrofitService().getBanner()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<BannerData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BannerData> bannerDatas) {
                        mBannerDatas.addAll(bannerDatas);
                        for (int i = 0;i < mBannerDatas.size();i ++){
                            imgUrls.clear();
                            imgUrls.add(mBannerDatas.get(i).getImg());
                        }
                        setupBanner(mBannerDatas);

                    }
                });

    }

    private void setupBanner(List<BannerData> bannerDatas) {
        mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BaHolder();
            }
        },imgUrls)
                .setPageIndicator(new int[]{
                        R.drawable.ic_page_indicator,
                        R.drawable.ic_page_indicator_focused
                });
        mBanner.setManualPageable(true);
    }
}
