package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.overlay.WalkRouteOverlay;

public class MapActivity extends Check implements AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener, TextWatcher, View.OnFocusChangeListener {

    private MapView mMapView;
    private AMap aMap;
    private final static String TAG="GAODE";
    private LatLonPoint startPoint;
    private LatLonPoint to;
    private RouteSearch routeSearch;
    private WalkRouteOverlay walkRouteOverlay;

    private LinearLayout mLayoutDetails;
    private LinearLayout mLayoutSearch;
    private LinearLayout mLayoutRoute;

    private TextView mTvSite;
    private TextView mTvDetail;
    private TextView mTvMore;
    private EditText mEtSearch;
    private ImageView mImgSearch;
    private Button mBtnRoute;
    private ImageView mImgBack;
    private ImageView mImgExchange;

    private EditText mEtStart;
    private EditText mEtEnd;

    private int MODE = 0;
    private static final int MODE_ROUTE = 1;
    private static final int MODE_SEARCH = 2;

    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private LatLonPoint mSearchPoint;

    public static void start(Context context) {
        Intent starter = new Intent(context, MapActivity.class);
        context.startActivity(starter);
    }

    private void initView() {
        mLayoutDetails = findViewById(R.id.map_bottom_id);
        mTvSite = findViewById(R.id.map_bottom_site);
        mTvDetail = findViewById(R.id.map_bottom_detail);
        mTvMore = findViewById(R.id.map_bottom_more);

        mLayoutSearch = findViewById(R.id.map_top_search);
        mEtSearch = findViewById(R.id.map_top_edt);
        mImgSearch = findViewById(R.id.map_top_button);
        mLayoutRoute = findViewById(R.id.map_top_route);
        mEtStart = findViewById(R.id.map_top_starting);
        mEtEnd = findViewById(R.id.map_top_destination);
        mBtnRoute = findViewById(R.id.map_route_button);
        mImgBack = findViewById(R.id.map_top_back);
        mImgExchange = findViewById(R.id.map_top_exchange);

    }

    private void initListener(){
        mTvMore.setOnClickListener(v -> {
                Intent intent = new Intent(getBaseContext(),PointDetailActivity.class);
                intent.putExtra("地点","");
                intent.putExtra("详情","");
                intent.putExtra("图片","");
                startActivity(intent);
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
    }
    private void exchangeMode(){
        if (MODE == MODE_SEARCH){
            mLayoutSearch.setVisibility(View.GONE);
            mLayoutRoute.setVisibility(View.VISIBLE);
            mBtnRoute.setVisibility(View.GONE);
            MODE = MODE_ROUTE;
        }else{
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

        mMapView =  findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap=mMapView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);

        routeSearch=new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        initView();
        initListener();
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
    public void onMyLocationChange(Location location) {
        startPoint =new LatLonPoint(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        aMap.clear();
        if (i== AMapException.CODE_AMAP_SUCCESS){
            if (walkRouteResult!=null&&walkRouteResult.getPaths()!=null){
                WalkPath walkPath=walkRouteResult.getPaths().get(0);
                if (walkRouteOverlay!=null){
                    walkRouteOverlay.removeFromMap();
                }
                walkRouteOverlay=new WalkRouteOverlay(this,aMap,walkPath,
                        walkRouteResult.getStartPos(),walkRouteResult.getTargetPos());
                LatLonPoint l=walkRouteOverlay.getLastWalkPoint(walkPath.getSteps().get(0));
                Log.d(TAG, "onWalkRouteSearched: "+l.toString());

                walkRouteOverlay.addToMap();
                walkRouteOverlay.zoomToSpan();
            }
        }

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
}
