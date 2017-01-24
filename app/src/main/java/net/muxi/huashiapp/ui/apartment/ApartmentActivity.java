package net.muxi.huashiapp.ui.apartment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.ApartmentData;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/7/30.
 */
public class ApartmentActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.img_empty)
    ImageButton mImgEmpty;
    @BindView(R.id.tv_error)
    TextView mTvError;
    private HuaShiDao dao;
    private List<ApartmentData> mApartDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        ButterKnife.bind(this);
        dao = new HuaShiDao();
        mApartDatas = dao.loadApart();
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        if (mApartDatas.size() > 0) {
            setupRecyclerView(mApartDatas);
        } else {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
        mSwipeRefreshLayout.setEnabled(false);
        setTitle("部门信息");
        CampusFactory.getRetrofitService().getApartment()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<ApartmentData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mApartDatas.size() == 0) {
                            ToastUtil.showShort(getString(R.string.tip_net_error));
                            mSwipeRefreshLayout.setRefreshing(false);
                            mTvError.setVisibility(View.VISIBLE);
                            mImgEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(List<ApartmentData> apartmentDataList) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (apartmentDataList.size() != mApartDatas.size()) {
                            setupRecyclerView(apartmentDataList);
                            dao.deleteApartData();
                            for (ApartmentData data : apartmentDataList) {
                                dao.insertApart(data);
                            }
                        }
                    }
                });
    }

    public void setupRecyclerView(List<ApartmentData> apartmentDataList) {
        ApartmentAdapter adapter = new ApartmentAdapter(apartmentDataList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }
}
