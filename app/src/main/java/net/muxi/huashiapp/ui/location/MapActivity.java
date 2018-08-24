package net.muxi.huashiapp.ui.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.muxistudio.appcommon.data.Detail;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.PointDetails;
import net.muxi.huashiapp.ui.location.data.PointSearch;
import net.muxi.huashiapp.ui.location.overlay.WalkRouteOverlay;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapActivity extends AppCompatActivity implements AMapLocationListener, TextWatcher, View.OnFocusChangeListener,AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    private MapView mMapView;
    private AMap aMap;
    private final static String TAG="GAODE";
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private LatLonPoint mSearchPoint;
    private LatLonPoint mNowPoint;
    private LocationListener mLocationListener;
    private RouteSearch routeSearch;
    private WalkRouteOverlay walkRouteOverlay;
    private MapSearchAdapter mAdapter;
    private MapPresent mMapPresent;
    private Marker mMarker;
    private PointDetails mNowPointDetails;   //  此时底部应该显示的点数据

    private LinearLayout mLayoutDetails;
    private LinearLayout mLayoutSearch;
    private LinearLayout mLayoutRoute;
    private LinearLayout mLayoutResult;
    private RelativeLayout mRelativeLayout;

    private TextView mTvSite;
    private TextView mTvDetail;
    private Button mBtnMore;
    private EditText mEtSearch;
    private ImageView mImgSearch;
    private Button mBtnRoute;
    private ImageView mImgBack;
    private ImageView mImgExchange;
    private ImageView mImgLocate;
    private RecyclerView mRecyclerView;

    private EditText mEtStart;
    private EditText mEtEnd;

    private int MODE = 2;
    private static final int MODE_ROUTE = 1;   // 路线
    private static final int MODE_SEARCH = 2;  // 搜索

//    private int id = 0;

    private List<PointSearch> mList;

    public static void start(Context context) {
        Intent starter = new Intent(context, MapActivity.class);
        context.startActivity(starter);
    }

    private void initView() {
        mLayoutDetails = findViewById(R.id.map_bottom_id);
        mTvSite = findViewById(R.id.map_bottom_site);
        mTvDetail = findViewById(R.id.map_bottom_detail);
        mBtnMore = findViewById(R.id.map_bottom_more);
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
        mLayoutResult = findViewById(R.id.map_search_layout);
        mImgLocate = findViewById(R.id.map_btn_locate);
    }

    private void initListener(){
        mBtnMore.setOnClickListener(v -> {
            mNowPointDetails = new PointDetails();
            mNowPointDetails.setName("");
            mNowPointDetails.setInfo("详细信息详细信息");
            PointDetailActivity.start(getBaseContext(),mNowPointDetails);

        });
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

        });
        mEtSearch.addTextChangedListener(this);
        mEtStart.addTextChangedListener(this);
        mEtEnd.addTextChangedListener(this);

    }

    private void initAdapter(){
        mAdapter = new MapSearchAdapter(getBaseContext(), mList);
        mRecyclerView = new RecyclerView(getBaseContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MapSearchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String name) {
                mEtSearch.setText(name);
                mLayoutResult.setVisibility(View.GONE);
            }
        });
    }

    public void initData(){
        mList = new ArrayList<>();
        PointSearch pointSearch1 = new PointSearch();
        pointSearch1.setName("八号楼");
        PointSearch pointSearch2= new PointSearch();
        pointSearch2.setName("七号楼");
        mList.add(pointSearch1);
        mList.add(pointSearch2);
    }

    //  搜索模式切换
    private void exchangeMode(){
        mLayoutResult.setVisibility(View.GONE);
        if (MODE == MODE_SEARCH){
            mLayoutSearch.setVisibility(View.GONE);
            mLayoutRoute.setVisibility(View.VISIBLE);
            mBtnRoute.setVisibility(View.GONE);
            MODE = MODE_ROUTE;
        }else if (MODE == MODE_ROUTE){
            mLayoutRoute.setVisibility(View.GONE);
            mLayoutSearch.setVisibility(View.VISIBLE);
            mBtnRoute.setVisibility(View.VISIBLE);
            MODE = MODE_SEARCH;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initListener();
        initData();
        initAdapter();
        checkPermission();
        mMapView =  findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        if (aMap != null) {
            mMapPresent = new MapPresent(aMap);
            mMapPresent.setlocation();
        }

        if (MODE == MODE_SEARCH){

        } else if (MODE == MODE_ROUTE){
            mStartPoint = new LatLonPoint(39.996678,116.479271);
            mEndPoint = new LatLonPoint(39.997796,116.468939);
//            mNowPoint = mMapPresent.getMyLocation();
//            mStartPoint = mNowPoint;
            drawRoute(mStartPoint,mEndPoint);
        }
    }


    public void checkPermission(){
        String[] needPermissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        boolean request=false;
        for (String s:needPermissions
             ) {
            if (ContextCompat.checkSelfPermission(this,s )
                    != PackageManager.PERMISSION_GRANTED){
                request=true;
            }
        }
        if (request) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this,"需要定位权限",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }

        }
    }

    public void drawRoute(LatLonPoint startPoint, LatLonPoint endPoint){
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint,endPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
        routeSearch = new RouteSearch(this);
        routeSearch.calculateWalkRouteAsyn(query);//开始算路
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                aMap.clear();
                if (i== AMapException.CODE_AMAP_SUCCESS){
                    if (walkRouteResult!=null&&walkRouteResult.getPaths()!=null){
                        WalkPath walkPath=walkRouteResult.getPaths().get(0);
                        if (walkRouteOverlay!=null){
                            walkRouteOverlay.removeFromMap();
                        }
                        walkRouteOverlay=new WalkRouteOverlay(getBaseContext(),aMap,walkPath,
                                walkRouteResult.getStartPos(),walkRouteResult.getTargetPos());
                        LatLonPoint l=walkRouteOverlay.getLastWalkPoint(walkPath.getSteps().get(0));
                        Log.d(TAG, "onWalkRouteSearched: "+l.toString());
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                    }
                }
            }

            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }

    //  检查marker 用坐标or名称？？
    public void checkMarker(LatLonPoint latLonPoint){

    }

    public void checkMarker(String name){

    }

    @Override
    public boolean onMarkerClick(Marker marker){

        CampusFactory.getRetrofitService().getDetail(marker.getTitle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detail -> {
                    mTvSite.setText(detail.getPlat().getName());
                    mTvDetail.setText(detail.getPlat().getInfo());
                    mBtnMore.setEnabled(true);
                },throwable -> {
                    throwable.printStackTrace();
                    mTvSite.setText(marker.getTitle());
                    mTvDetail.setText(marker.getSnippet());
                    mBtnMore.setEnabled(false);
                },()->{
                    Logger.i("onMarkerClock");});
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation){
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mMapPresent.setlocation();
//                mSearchPoint.setLatitude(amapLocation.getLatitude());
//                mSearchPoint.setLongitude(amapLocation.getLongitude());
//                if (mMarker == null) {
//                    mMarker = aMap.addMarker(new MarkerOptions()
//                            .position(AMapUtil.convertToLatLng(mSearchPoint))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
//                } else {
//                    mMarker.setPosition(AMapUtil.convertToLatLng(mSearchPoint));
//                }
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(mSearchPoint), 10));
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
        if (charSequence.length() == 0) mLayoutResult.setVisibility(View.GONE);
        else if(mLayoutResult.getVisibility() != View.VISIBLE) {
            mLayoutResult.setVisibility(View.VISIBLE);
//            mRelativeLayout.setAlpha(Float.valueOf("0.7"));
            mMapPresent.ToPoiSearch(charSequence.toString(),getBaseContext());
            mAdapter.notifyDataSetChanged();
        }
//        mList.clear();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void onTouch(MotionEvent event){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

            }
        }else
            finish();
    }

}
