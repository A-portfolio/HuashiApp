package net.muxi.huashiapp.apartment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.ApartmentData;
import net.muxi.huashiapp.common.net.CampusFactory;
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
    @BindView(R.id.apartment_rey)
    RecyclerView mApartmentRey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        ButterKnife.bind(this);
        CampusFactory.getRetrofitService().getApartment()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<ApartmentData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ApartmentData> apartmentDataList) {
                        setupRecyclerView(apartmentDataList);

                    }
                });
    }

    public void setupRecyclerView(List<ApartmentData> apartmentDataList){
        ApartmentAdapter adapter = new ApartmentAdapter(apartmentDataList);
        mApartmentRey.setAdapter(adapter);
        mApartmentRey.setLayoutManager(new LinearLayoutManager(this));
        mApartmentRey.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("部门信息");

    }
}
