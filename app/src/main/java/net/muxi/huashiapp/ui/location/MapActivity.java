package net.muxi.huashiapp.ui.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.MapDetailList;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.DetailEven;
import net.muxi.huashiapp.ui.location.data.PointDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapActivity extends FragmentActivity implements AMapLocationListener, TextWatcher,
        View.OnFocusChangeListener, AMap.OnMarkerClickListener, AMap.OnMapTouchListener ,View.OnClickListener{

    private MapView mMapView;
    private AMap aMap;
    private final static String TAG="GAODE";
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private String mStartName;
    private String mEndName;
    private LatLonPoint mSearchPoint;
    private String mSearchName;
    private final String FRAGMENT_TAG="detail_fragment";
    private MapSearchAdapter mAdapter;
    private MapPresenter mMapPresenter;
    private PointDetails mNowPointDetails;   //  此时底部应该显示的点数据

    private BottomFragment mBottomFragment;

    private boolean requestPermission;
    private LinearLayout mLayoutSearch;
    private LinearLayout mLayoutRoute;

    // 顶部搜索栏
    private EditText mEtSearch;
    private EditText mEtStart;
    private EditText mEtEnd;
    private ImageView mImgSearch;
    private ImageView mImgBack;
    private ImageView mImgExchange;

    private RecyclerView mRecyclerView;
    private Button mBtnRoute;
    private ImageView mImgLocate;

    private int MODE = 2;
    private static final int MODE_ROUTE = 1;   // 路线
    private static final int MODE_SEARCH = 2;  // 搜索

    private List<MapDetailList.PointsBean> mList = new ArrayList<>();

    public static void start(Context context) {
        Intent starter = new Intent(context, MapActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        requestPermission=false;
        initView();
        initAdapter();
        initListener();
        initLayout(10,10,5);
        checkPermission();
        mMapView =  findViewById(R.id.map);
        // TODO: 18-8-30 待改进
        hideFragment();

        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        if (aMap != null) {
            mMapPresenter = new MapPresenter(aMap);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapTouchListener(this);
            if (!requestPermission) {
                mMapPresenter.setLocation();
            }
        }


        RxBus.getDefault().toObservable(DetailEven.class)
                .subscribe(detailEven ->  showDetail(detailEven.getName(),detailEven.isSearchOrRoute(),mMapPresenter.getEndMarker()),
                        Throwable::printStackTrace,
                        ()-> Log.i(TAG, "detailEven"));

    }

    private void initView() {
        mLayoutSearch = findViewById(R.id.map_top_search);
        mEtSearch = findViewById(R.id.map_top_edt);
        mImgSearch = findViewById(R.id.map_top_button);
        mLayoutRoute = findViewById(R.id.map_top_route);
        mEtStart = findViewById(R.id.map_top_starting);
        mEtEnd = findViewById(R.id.map_top_destination);
        mBtnRoute = findViewById(R.id.map_route_button);
        mImgBack = findViewById(R.id.map_top_back);
        mImgExchange = findViewById(R.id.map_top_exchange);
        mRecyclerView = findViewById(R.id.map_search_recycle);
        mImgLocate = findViewById(R.id.map_btn_locate);

    }

    private void initAdapter(){
        OnClickTextList onClickTextList = (s, l) -> {
            if(mEtStart.hasFocus()){mEtStart.setText(s);mStartName=s;mStartPoint=l;mEtStart.clearFocus();ifCanSearch();}
            else if(mEtEnd.hasFocus()) {mEtEnd.setText(s);mEndName=s;mEndPoint=l;mEtEnd.clearFocus();ifCanSearch();}
            else {mEtSearch.setText(s);mSearchPoint=l;
            mSearchName=s;
            mEtSearch.clearFocus();
            }
            hideKeyboard();
        };
        mAdapter = new MapSearchAdapter(getBaseContext(), mList,onClickTextList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        mImgLocate.setOnClickListener(v -> mMapPresenter.setLocation());
        mImgBack.setOnClickListener(v -> {
            if (MODE == MODE_SEARCH){
                finish();
            }else {
                exchangeMode();
            }
        });
        mBtnRoute.setOnClickListener( v -> {
            exchangeMode();
        });

        mImgExchange.setOnClickListener(v -> {
            String temp = mEtStart.getText().toString();
            mEtStart.setText(mEtEnd.getText().toString());
            mEtEnd.setText(temp);
        });

        mImgSearch.setOnClickListener( v -> {
            initLayout(0,0,0);
            ifCanSearchPoint();
        });
        mEtSearch.setOnClickListener( v ->{
            if (mList != null){
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        mEtSearch.addTextChangedListener(this);
        mEtStart.addTextChangedListener(this);
        mEtEnd.addTextChangedListener(this);
        mEtSearch.setOnFocusChangeListener(this);
        mEtStart.setOnFocusChangeListener(this);
        mEtEnd.setOnFocusChangeListener(this);
    }

    private void initLayout(int routeBottom,int locateBottom,int locateLeft){
        RelativeLayout.LayoutParams paramsRoute = (RelativeLayout.LayoutParams) mBtnRoute.getLayoutParams();
        paramsRoute.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,routeBottom);  //  设置route按钮相对于底部的距离
        RelativeLayout.LayoutParams paramsLocate = (RelativeLayout.LayoutParams) mImgLocate.getLayoutParams();
        paramsLocate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,locateBottom);   //  设置定位按钮相对于底部的距离
        paramsLocate.addRule(RelativeLayout.ALIGN_LEFT,locateLeft);  //  设置定位按钮相对于屏幕左侧的距离
        mBtnRoute.setLayoutParams(paramsRoute);
        mImgLocate.setLayoutParams(paramsLocate);
    }

    //  搜索模式切换
    private void exchangeMode(){
        mRecyclerView.setVisibility(View.GONE);
        if (MODE == MODE_SEARCH){
            mStartName = "我的位置";
            mEtStart.setText(mStartName);
            mStartPoint = mMapPresenter.getMyLocation();
            ifCanSearch();
            mSearchPoint = null;
            mLayoutSearch.setVisibility(View.GONE);
            mLayoutRoute.setVisibility(View.VISIBLE);
            mBtnRoute.setVisibility(View.GONE);
            MODE = MODE_ROUTE;
        }else if (MODE == MODE_ROUTE){
            mStartPoint = null;
            mEndPoint = null;
            mLayoutRoute.setVisibility(View.GONE);
            mLayoutSearch.setVisibility(View.VISIBLE);
            mBtnRoute.setVisibility(View.VISIBLE);
            MODE = MODE_SEARCH;
        }
    }


    public void checkPermission(){
        String[] needPermissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        for (String s:needPermissions
             ) {
            if (ContextCompat.checkSelfPermission(this,s )
                    != PackageManager.PERMISSION_GRANTED){
                requestPermission=true;
            }
        }
        if (requestPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this,"需要定位权限",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        needPermissions,
                        1);
            }else {
                ActivityCompat.requestPermissions(this, needPermissions,
                        1);
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        initLayout(0,0,0);
        Logger.i("marker onclick");
        if (marker.getTitle()==null){

            return true;
        }
        showDetail(marker.getTitle(),false,marker);
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation){
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mMapPresenter.setLocation();
            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else if(mEtStart.hasFocus()||mEtEnd.hasFocus()||mEtSearch.hasFocus()){
            CampusFactory.getRetrofitService().getDetailList(charSequence.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list ->{
                        mList.clear();
                        mList.addAll(list.getPoints());
                        mAdapter.notifyDataSetChanged();
                    },Throwable::printStackTrace);
            if(mRecyclerView.getVisibility() != View.VISIBLE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            //暂时为空
        }else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapPresenter.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode==1){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMapPresenter.setLocation();
            }
        }else
            finish();
    }

    private void ifCanSearch() {
        //method about drawing route
        if(mStartPoint!=null && mEndPoint!=null){
            mMapPresenter.drawRoute(getApplicationContext(),mStartName,mEndName
                    ,mStartPoint,mEndPoint);


        }else {
            //暂时为空
        }
    }

    private void ifCanSearchPoint(){
        //method about searching point
        if(mSearchPoint!=null){
            aMap.clear();
            Marker marker=mMapPresenter.addMarker(mSearchPoint,mEtSearch.getText().toString());
            mEndPoint = mSearchPoint;
            mEndName = mSearchName;
            mEtEnd.setText(mEndName);
            showDetail(mSearchName,false,marker);
        }
    }

    private void hideKeyboard() { {
            InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imManager != null) {
            imManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }
    }
    }


    /**
     *
     * @param name
     * @param ifDraworSearch true=draw and false=search
     */
    public void showDetail(String name,boolean ifDraworSearch,Marker marker){
        initLayout(0,0,0);
        CampusFactory.getRetrofitService().getDetail(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detail -> {
                    mNowPointDetails=new PointDetails();
                    mNowPointDetails.setName(detail.getPlat().getName());
                    mNowPointDetails.setInfo(detail.getPlat().getInfo());
                    List<String> list=detail.getPlat().getUrl();
                    mNowPointDetails.setUrl(list.toArray(new String[list.size()]));
                    String details;
                    if (ifDraworSearch) {
                        details = String.format("%sm米  |   用时约%s分钟", mMapPresenter.getDistance(), mMapPresenter.getTime());
                    }else {
                        details = detail.getPlat().getInfo();
                    }
                    getDetailFragment().setdetail(mNowPointDetails.getName(),details,true);
                    if (getDetailFragment().isHidden())
                        showFragment();
                }, throwable -> {
                    throwable.printStackTrace();
                    int code=0;
                    if (throwable instanceof HttpException)
                        code=((HttpException) throwable).code();
                    switch (code){
                        case 404:{
                            getDetailFragment().setdetail(marker.getTitle(),marker.getSnippet(),false);
                            if (getDetailFragment().isHidden())
                                showFragment();
                            break;
                        }
                        default:break;
                    }
                });
    }

    @Override
    public void onTouch(MotionEvent event){
        if (mRecyclerView.getVisibility() == View.VISIBLE){
            mRecyclerView.setVisibility(View.GONE);
            hideKeyboard();
        }
        if (getDetailFragment().isVisible())
            hideFragment();
    }


    public BottomFragment getDetailFragment() {
        BottomFragment fragment = (BottomFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            Log.i(TAG, "getDetailFragment: create fragment");
            fragment = new BottomFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_bottom_ll, fragment, FRAGMENT_TAG)
                    .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
                    .commit();
        }
        return fragment;
    }

    public void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
                .show(getDetailFragment())
                .commit();
    }

    public void hideFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom)
                .hide(getDetailFragment())
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (mNowPointDetails!= null)
            PointDetailActivity.start(this,mNowPointDetails);
    }
}
