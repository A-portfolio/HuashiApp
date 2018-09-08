package net.muxi.huashiapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.BannerData;
import com.muxistudio.appcommon.data.Hint;
import com.muxistudio.appcommon.data.ItemData;
import com.muxistudio.appcommon.data.ProductData;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.LoginSuccessEvent;
import com.muxistudio.appcommon.event.RefreshBanner;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.FrescoUtil;
import com.muxistudio.appcommon.utils.VibratorUtil;
import com.muxistudio.common.util.ACache;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.NetUtil;
import com.muxistudio.common.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.CalendarActivity;
import net.muxi.huashiapp.ui.MoreActivity;
import net.muxi.huashiapp.ui.apartment.ApartmentActivity;
import net.muxi.huashiapp.ui.card.CardActivity;
import net.muxi.huashiapp.ui.electricity.ElectricityActivity;
import net.muxi.huashiapp.ui.electricity.ElectricityDetailActivity;
import net.muxi.huashiapp.ui.location.MapActivity;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.news.NewsActivity;
import net.muxi.huashiapp.ui.score.activtities.ScoreCreditActivity;
import net.muxi.huashiapp.ui.studyroom.StudyRoomActivity;
import net.muxi.huashiapp.ui.studyroom.StudyRoomBlankActivity;
import net.muxi.huashiapp.ui.timeTable.CourseAuditSearchActivity;
import net.muxi.huashiapp.ui.website.WebsiteActivity;
import net.muxi.huashiapp.ui.webview.WebViewActivity;
import net.muxi.huashiapp.utils.TipViewUtil;
import net.muxi.huashiapp.widget.IndicatedView.IndicatedView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ybao on 17/1/25.
 */

public class MainFragment extends BaseAppFragment implements MyItemTouchCallback.OnDragListener {

    private MainAdapter mMainAdapter;
    private ItemTouchHelper itemTouchHelper;
    private List<ItemData> mItemDatas = new ArrayList<ItemData>();
    private List<BannerData> mBannerDatas;
    private HuaShiDao dao;
    private PreferenceUtil sp;
    private ProductData mProductData;
    private String mProductJson;
    private Hint mHint = new Hint();

    public static final int DIRECTION_UP = 1;
    public static final String SCORE_ACTIVITY = "score";
    public static final String CARD_ACTIVITY = "card";
    public static final String CREDIT_ACTIVITY = "credit";
    public static final String COURSE_AUDIT_SEARCH_ACTIVITY = "course_search";
    public static final String SCORE_CREDIT_ACTIVITY = "score_credit_activity";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();

        RxBus.getDefault().toObservable(LoginSuccessEvent.class)
                .subscribe(loginSuccessEvent -> {
                    switch (loginSuccessEvent.targetActivityName) {
                        case CARD_ACTIVITY:
                            CardActivity.start(getContext());
                            break;
                        case COURSE_AUDIT_SEARCH_ACTIVITY:
                            CourseAuditSearchActivity.start(getContext());
                            break;
                        case SCORE_CREDIT_ACTIVITY:
                            ScoreCreditActivity.start(Objects.requireNonNull(getContext()));
                    }
                }, Throwable::printStackTrace);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mToolbar.setTitle("华师匣子");
        sp = new PreferenceUtil();
        dao = new HuaShiDao();
        mBannerDatas = dao.loadBannerData();
        getBannerData();
        RxBus.getDefault().toObservable(RefreshBanner.class)
                .subscribe(refreshBanner -> {
                    refresh();
                }, throwable -> throwable.printStackTrace());
        initHintView();
//        initBulletin();
        initView();
        //getHint();
        if (mProductData == null) {
            mProductData = new ProductData(0.0, null);
            getProduct();
        } else {
            Gson gson = new Gson();
            mProductData = gson.fromJson(PreferenceUtil.getString(PreferenceUtil.PRODUCT_DATA), ProductData.class);
            getProduct();
        }
        return view;
    }

    private void setData() {
        ArrayList<ItemData> items = (ArrayList<ItemData>) ACache.get(getContext()).getAsObject("items");
        if (items != null) {
            mItemDatas.addAll(items);
        } else {
            mItemDatas.add(new ItemData("成绩", R.drawable.ic_score + "", false));
            mItemDatas.add(new ItemData("校园通知", R.drawable.ic_news + "", false));
            mItemDatas.add(new ItemData("电费", R.drawable.ic_ele + "", false));
            mItemDatas.add(new ItemData("校园卡", R.drawable.ic_card + "", false));
            mItemDatas.add(new ItemData("空闲教室", R.drawable.ic_empty_room + "", false));
            mItemDatas.add(new ItemData("蹭课", R.drawable.ic_course_audit + "", false));
            mItemDatas.add(new ItemData("部门信息", R.drawable.ic_apartment + "", false));
            mItemDatas.add(new ItemData("校历", R.drawable.ic_calendar + "", false));
            mItemDatas.add(new ItemData("常用网站", R.drawable.ic_net + "", false));
            mItemDatas.add(new ItemData("地图",R.drawable.ic_map + "",false));
            mItemDatas.add(new ItemData("更多", R.drawable.ic_more + "", false));
        }

    }

    private void initHintView() {
        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_MAIN, true)) {
            IndicatedView indicatedView = new IndicatedView(getContext());
            indicatedView.setTipViewText("试试图书馆功能吧!");
            TipViewUtil.addToContent(getContext(), indicatedView, DIRECTION_UP,
                    DimensUtil.getScreenWidth() / 4,
                    DimensUtil.getScreenHeight() - DimensUtil.getNavigationBarHeight()
                            - DimensUtil.dp2px(98));
            IndicatedView indicatedView1 = new IndicatedView(getContext());
            indicatedView1.setTipViewText("新加入了蹭课功能哟!");
            TipViewUtil.addToContent(getContext(), indicatedView1, DIRECTION_UP,
                    DimensUtil.getScreenWidth() / 4,
                    (DimensUtil.getScreenHeight() - DimensUtil.getNavigationBarHeight()) / 2);
            PreferenceUtil.saveBoolean(PreferenceUtil.IS_FIRST_ENTER_MAIN, false);
        }
    }

    private void initView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mMainAdapter = new MainAdapter(mItemDatas, mBannerDatas, mHint);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mMainAdapter.isHintPosition(position)
                        || mMainAdapter.isBannerPosition(position) || mMainAdapter.isFooterPosition(
                        position) ? layoutManager.getSpanCount() : 1);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMainAdapter);

        itemTouchHelper = new ItemTouchHelper(
                new MyItemTouchCallback(mMainAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        //fixme getActivity() may produce   referring to this post https://www.jianshu.com/p/9d75e328f1de
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != mItemDatas.size() && vh.getLayoutPosition() != 0
                        && vh.getLayoutPosition() != mItemDatas.size() + 1) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 50);
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != 0
                        && vh.getLayoutPosition() != mItemDatas.size() + 1) {
                    int index = vh.getLayoutPosition() - MainAdapter.ITEM;
                    if(index < 0) return;

                    ItemData itemData = mItemDatas.get(index);
                    switch (itemData.getName()) {
                        case "校园通知":
                            NewsActivity.start(getActivity());
                            MobclickAgent.onEvent(getActivity(), "notice_info_query");
                            break;
                        case "电费":
                            String eleQuery = PreferenceUtil.getString(PreferenceUtil.ELE_QUERY_STRING);
                            if (eleQuery.equals("")) {
                                ElectricityActivity.start(getActivity());
                            } else {
                                ElectricityDetailActivity.start(getActivity(), eleQuery);
                            }
                            MobclickAgent.onEvent(getActivity(), "ele_fee_query");
                            break;
                        case "校园卡":
                            if (TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().getSid())) {
                                LoginActivity.start(getActivity(), "info", CARD_ACTIVITY);
                            } else {
                                CardActivity.start(getActivity());
                            }
                            MobclickAgent.onEvent(getActivity(), "card_query");
                            break;
                        case "空闲教室":
                            String today = DateUtil.getWeek(new Date());
                            if (today.equals("周六") || today.equals("周日")) {
                                StudyRoomBlankActivity.start(getActivity());
                            } else {
                                StudyRoomActivity.start(getActivity());
                            }
                            MobclickAgent.onEvent(getActivity(), "spare_room_query");
                            break;
                        case "部门信息":
                            ApartmentActivity.start(getActivity());
                            MobclickAgent.onEvent(getActivity(), "apartment_info_query");
                            break;
                        case "校历":
                            CalendarActivity.start(getActivity());
                            MobclickAgent.onEvent(getActivity(), "calendar_hand_in");
                            break;
                        case "常用网站":
                            WebsiteActivity.start(getActivity());
                            MobclickAgent.onEvent(getActivity(), "frequent_web_query");
                            break;
                        case "学而":
                            Intent intent = WebViewActivity.newIntent(getActivity(), mProductData.get_product().get(0).getUrl(),
                                    mProductData.get_product().get(0).getName(),
                                    mProductData.get_product().get(0).getIntro(),
                                    mProductData.get_product().get(0).getIcon());
                            startActivity(intent);
                            MobclickAgent.onEvent(getActivity(), "xueer");
                            break;
                        case "蹭课":
                            if (TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().getSid())) {
                                LoginActivity.start(getActivity(), "info", COURSE_AUDIT_SEARCH_ACTIVITY);
                            } else {
                                CourseAuditSearchActivity.start(getActivity());
                                MobclickAgent.onEvent(getActivity(), "course_audit");
                            }
                            break;
                        case "地图":
                            MapActivity.start(getActivity());
//                            MobclickAgent.onEvent(getActivity(),"");
                            break;
                        case "更多":
                            MoreActivity.start(getActivity());
                            break;

                        case "成绩":
                            if(TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().getSid())){
                                LoginActivity.start(getActivity(),"info",SCORE_CREDIT_ACTIVITY);
                            }else{
                                ScoreCreditActivity.start(Objects.requireNonNull(getActivity()));
                            }
                            MobclickAgent.onEvent(getActivity(), "score_query");
                            break;


                    }
                }
            }
        });

    }

    //更新首页视图
    public void updateProductDisplay(ProductData productData) {
        if (mItemDatas.size() - 11 != productData.get_product().size()) {
            List<ItemData> itemDataList = new ArrayList<>();
            for (int i = 0; i < productData.get_product().size(); i++) {
                itemDataList.add(new ItemData(productData.get_product().get(i).getName(),
                        productData.get_product().get(i).getIcon(), true));
            }
            mItemDatas.addAll(mItemDatas.size() - 1, itemDataList);
            mMainAdapter.swapItems(mItemDatas);
        }

    }

    public void getProduct() {
        CampusFactory.getRetrofitService().getProduct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(productData -> {
                    if (productData != null) {
                        if (productData.getUpdate() != mProductData.getUpdate()) {
                            mProductData = productData;
                            Gson gson = new Gson();
                            mProductJson = gson.toJson(mProductData);
                            PreferenceUtil.saveString(PreferenceUtil.PRODUCT_DATA, mProductJson);
                            PreferenceUtil.saveFloat(PreferenceUtil.PRODUCT_UPDATE, (float) productData.getUpdate());
                            for (int i = 0; i < productData.get_product().size(); i++) {
                                FrescoUtil.savePicture(productData.get_product().get(i).getIcon(), getContext(), productData.get_product().get(i).getName());
                            }
                            updateProductDisplay(mProductData);
                        }
                    } else {
                        PreferenceUtil.clearString(PreferenceUtil.PRODUCT_DATA);
                        PreferenceUtil.clearFloat(PreferenceUtil.PRODUCT_UPDATE);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });

    }

    private void getBannerData() {
        if (NetUtil.isConnected()) {
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
                            if (getTheLastUpdateTime(bannerDatas) > getTheLastUpdateTime(
                                    mBannerDatas) || bannerDatas.size() != mBannerDatas.size()) {
                                mBannerDatas.clear();
                                mBannerDatas.addAll(bannerDatas);
                                dao.deleteAllBannerData();
                                for (int i = 0; i < mBannerDatas.size(); i++) {
                                    dao.insertBannerData(mBannerDatas.get(i));
                                }
                                updateRecyclerView(bannerDatas);
                                RxBus.getDefault().send(new RefreshBanner());

                            }
                        }
                    });
        }
    }

    //fixme beginTransaction() may produce NPE
    // fixme referring https://stackoverflow.com/questions/27742471/nullpointerexception-fragmentmanager-begintransaction
    public void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
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
        for (ItemData itemData : mItemDatas) {
            Logger.d(itemData.getName() + " ");
        }
    }
}
