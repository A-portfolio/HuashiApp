package net.muxi.huashiapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.data.ItemData;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.ui.CalendarActivity;
import net.muxi.huashiapp.ui.apartment.ApartmentActivity;
import net.muxi.huashiapp.ui.card.CardActivity;
import net.muxi.huashiapp.ui.credit.SelectCreditActivity;
import net.muxi.huashiapp.ui.electricity.ElectricityActivity;
import net.muxi.huashiapp.ui.news.NewsActivity;
import net.muxi.huashiapp.ui.score.ScoreSelectActivity;
import net.muxi.huashiapp.ui.studyroom.StudyRoomActivity;
import net.muxi.huashiapp.ui.website.WebsiteActivity;
import net.muxi.huashiapp.util.ACache;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/1/25.
 */

public class MainFragment extends BaseFragment implements MyItemTouchCallback.OnDragListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MainAdapter mMainAdapter;

    private ItemTouchHelper itemTouchHelper;

    private List<ItemData> mItemDatas = new ArrayList<ItemData>();

    private List<BannerData> mBannerDatas;

    private HuaShiDao dao;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("华师匣子");

        setData();

        dao = new HuaShiDao();
        mBannerDatas = dao.loadBannerData();

        initView();
        getBannerDatas();

        return view;
    }

    private void setData() {
        ArrayList<ItemData> items = (ArrayList<ItemData>) ACache.get(getActivity()).getAsObject("items");

        if (items != null) {
            mItemDatas.addAll(items);
        } else {
            mItemDatas.add(new ItemData("成绩", R.drawable.ic_score + ""));
            mItemDatas.add(new ItemData("校园通知", R.drawable.ic_news + ""));
            mItemDatas.add(new ItemData("电费", R.drawable.ic_ele+ ""));
            mItemDatas.add(new ItemData("校园卡", R.drawable.ic_card + ""));
            mItemDatas.add(new ItemData("算学分", R.drawable.ic_credit + ""));
            mItemDatas.add(new ItemData("空闲教室", R.drawable.ic_empty_room + ""));
            mItemDatas.add(new ItemData("部门信息", R.drawable.ic_apartment + ""));
            mItemDatas.add(new ItemData("校历", R.drawable.ic_calendar + ""));
            mItemDatas.add(new ItemData("常用网站", R.drawable.ic_net + ""));
            mItemDatas.add(new ItemData("学而", R.drawable.ic_xueer + ""));
            mItemDatas.add(new ItemData("更多", R.drawable.ic_more + ""));
        }
    }

    private void initView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mMainAdapter = new MainAdapter(mItemDatas,mBannerDatas);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mMainAdapter.isBannerPosition(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMainAdapter);

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mMainAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != mItemDatas.size()) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 50);
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                ItemData itemData = mItemDatas.get(vh.getLayoutPosition());
                switch (itemData.getName()) {
                    case "成绩":
                        ScoreSelectActivity.start(getContext());
                        break;
                    case "校园通知":
                        NewsActivity.start(getContext());
                        break;
                    case "电费":
                        ElectricityActivity.start(getContext());
                        break;
                    case "校园卡":
                        CardActivity.start(getContext());
                        break;
                    case "算学分":
                        SelectCreditActivity.start(getContext());
                        break;
                    case "空闲教室":
                        StudyRoomActivity.start(getContext());
                        break;
                    case "部门信息":
                        ApartmentActivity.start(getContext());
                        break;
                    case "校历":
                        CalendarActivity.start(getContext());
                        break;
                    case "常用网站":
                        WebsiteActivity.start(getContext());
                        break;
                    case "学而":
                        App.logoutUser();
                        App.logoutLibUser();
                        break;
                    case "更多":
                        break;
                }
            }
        });
    }

    private void getBannerDatas() {
        if (NetStatus.isConnected()) {
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
                            if (getTheLastUpdateTime(bannerDatas) > getTheLastUpdateTime(mBannerDatas) || bannerDatas.size() != mBannerDatas.size()) {
                                mBannerDatas.clear();
                                mBannerDatas.addAll(bannerDatas);
                                dao.deleteAllBannerData();
                                for (int i = 0; i < mBannerDatas.size(); i++) {
                                    dao.insertBannerData(mBannerDatas.get(i));
                                }
                                updateRecyclerView(bannerDatas);
                                Logger.d("update recyclerview");
                            }
                            Logger.d("get bannerdatas");
                        }
                    });
        }

    }

    public long getTheLastUpdateTime(List<BannerData> bannerDatas) {
        long lastTime = -1;
        if (bannerDatas.size() > 0) {
            for (int i = 0; i < bannerDatas.size(); i++) {
                if (lastTime < bannerDatas.get(i).getUpdate()) {
                    lastTime = bannerDatas.get(i).getUpdate();
                }
            }
        }
        return lastTime;
    }

    public void updateRecyclerView(List<BannerData> bannerDatas) {
        mMainAdapter.swapBannerData(bannerDatas);
    }


    @Override
    public void onFinishDrag() {
        ACache.get(getActivity()).put("items", (ArrayList<ItemData>) mItemDatas);
    }
}
