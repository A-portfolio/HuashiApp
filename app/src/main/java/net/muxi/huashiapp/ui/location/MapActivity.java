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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.amap.api.services.core.LatLonPoint;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.MapDetailList;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.DetailEven;
import net.muxi.huashiapp.ui.location.data.PointDetails;
import net.muxi.huashiapp.ui.location.overlay.WalkRouteOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.HttpException;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapActivity extends AppCompatActivity implements AMapLocationListener, TextWatcher,
        View.OnFocusChangeListener, AMap.OnMarkerClickListener,ChangeListenner {

    private MapView mMapView;
    private AMap aMap;
    private final static String TAG="GAODE";
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private String mStartName;
    private String mEndName;
    private LatLonPoint mSearchPoint;
    private String mSearchName;
    private LatLonPoint mNowPoint;

    private MapSearchAdapter mAdapter;
    private MapPresent mMapPresent;
    private PointDetails mNowPointDetails;   //  此时底部应该显示的点数据

    private LinearLayout mLayoutDetails;
    private LinearLayout mLayoutSearch;
    private LinearLayout mLayoutRoute;
    private LinearLayout mLayoutResult;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout.LayoutParams mParamsRoute;
    private RelativeLayout.LayoutParams mParamsLocate;
    private boolean requestPermission;

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

    private List<MapDetailList.PointsBean> mList = new ArrayList<>();

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
        mParamsRoute = (RelativeLayout.LayoutParams) mBtnRoute.getLayoutParams();
        mParamsLocate = (RelativeLayout.LayoutParams) mImgLocate.getLayoutParams();

        //初始化我的位置
    }

    private void initLayout(int route,int locateBottom,int locateLeft){
        mParamsRoute.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,route);
        mParamsLocate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,locateBottom);
        mParamsLocate.addRule(RelativeLayout.ALIGN_LEFT,locateLeft);
        mBtnRoute.setLayoutParams(mParamsRoute);
        mImgLocate.setLayoutParams(mParamsLocate);

    }

    private void initListener(){
        mImgLocate.setOnClickListener(v -> mMapPresent.setlocation());
        mBtnMore.setOnClickListener(v -> {
            if (mNowPointDetails!=null) {
                PointDetailActivity.start(getBaseContext(), mNowPointDetails);
            }
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
            mLayoutDetails.setVisibility(View.VISIBLE);
            initLayout(0,0,0);
            ifCanSearchPoint();
        });
        mEtSearch.addTextChangedListener(this);
        mEtStart.addTextChangedListener(this);
        mEtEnd.addTextChangedListener(this);
        mEtSearch.setOnFocusChangeListener(this);
        mEtStart.setOnFocusChangeListener(this);
        mEtEnd.setOnFocusChangeListener(this);

    }

    private void initAdapter(){
        OnClickTextList onClickTextList = new OnClickTextList() {
            @Override
            public void changeEditText(String s, LatLonPoint l) {
                if(mEtStart.hasFocus()){mEtStart.setText(s);mStartName=s;mStartPoint=l;mEtStart.clearFocus();ifCanSearch();}
                else if(mEtEnd.hasFocus()) {mEtEnd.setText(s);mEndName=s;mEndPoint=l;mEtEnd.clearFocus();ifCanSearch();}
                else {mEtSearch.setText(s);mSearchPoint=l;mSearchName=s;mEtSearch.clearFocus();}
                hideKeyboard();
            }
        };
        mAdapter = new MapSearchAdapter(getBaseContext(), mList,onClickTextList);
        mRecyclerView = findViewById(R.id.map_search_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    //  搜索模式切换
    private void exchangeMode(){
        mLayoutResult.setVisibility(View.GONE);
        if (MODE == MODE_SEARCH){
            mStartName = "我的位置";
            mEtStart.setText(mStartName);
            mStartPoint = mMapPresent.getMyLocation();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        requestPermission=false;
        initView();
        initListener();
        initLayout(10,10,5);
        initAdapter();
        checkPermission();
        mMapView =  findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        if (aMap != null) {
            mMapPresent = new MapPresent(aMap);
            aMap.setOnMarkerClickListener(this);
            if (!requestPermission)
            mMapPresent.setlocation();

        }

        RxBus.getDefault().toObservable(DetailEven.class)
                .subscribe(detailEven ->  showDetail(detailEven.getName(),detailEven.isSearchOrRoute()),
                        Throwable::printStackTrace,
                        ()-> Log.i(TAG, "detailEven"));

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
        mLayoutDetails.setVisibility(View.VISIBLE);
        initLayout(0,0,0);
        Logger.i("marker onclick");
        if (marker.getTitle()==null){

            return true;
        }
        CampusFactory.getRetrofitService().getDetail(marker.getTitle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detail -> {

                    mNowPointDetails=new PointDetails();
                    mNowPointDetails.setName(detail.getPlat().getName());
                    mNowPointDetails.setInfo(detail.getPlat().getInfo());
                    List<String> list=detail.getPlat().getUrl();
                    mNowPointDetails.setUrl(list.toArray(new String[list.size()]));
                    mTvSite.setText(mNowPointDetails.getName());
                    mTvDetail.setText(marker.getSnippet());
                    mBtnMore.setEnabled(true);
                },throwable -> {
                    throwable.printStackTrace();
                    mBtnMore.setEnabled(false);
                    int code=0 ;
                    if(throwable instanceof HttpException){
                        code = ((HttpException) throwable).code();
                    }
                    switch (code) {
                        case 404: {
                            mTvSite.setText(marker.getTitle());
                            mTvDetail.setText(marker.getSnippet());

                            break;
                        }
                        case 400:{
                            Toast.makeText(getApplicationContext(),"信息错误",Toast.LENGTH_LONG).show();
                            break;
                        }
                        default:break;
                    }
                },()-> Logger.i("onMarkerClock"));
        return true;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation){
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mMapPresent.setlocation();
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
            mLayoutResult.setVisibility(View.GONE);
        } else if(mEtStart.hasFocus()||mEtEnd.hasFocus()||mEtSearch.hasFocus()){
            CampusFactory.getRetrofitService().getDetailList(charSequence.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list ->{
                        mList.clear();
                        if(list.getPoints() != null) mList.addAll(list.getPoints());
                        mAdapter.notifyDataSetChanged();
                    },Throwable::printStackTrace);
            if(mLayoutResult.getVisibility() != View.VISIBLE) {
                mLayoutResult.setVisibility(View.VISIBLE);
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
            mLayoutResult.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapPresent.onDestory();
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
                mMapPresent.setlocation();
            }
        }else
            finish();
    }

    private void ifCanSearch() {
        //method about drawing route
        if(mStartPoint!=null && mEndPoint!=null){
            mMapPresent.drawRoute(getApplicationContext(),mStartName,mEndName
                    ,mStartPoint,mEndPoint,this);


        }else {
            //暂时为空
        }
    }

    private void ifCanSearchPoint(){
        //method about searching point
        if(mSearchPoint!=null){
            mMapPresent.addMarker(mSearchPoint,mEtSearch.getText().toString());
            mEndPoint = mSearchPoint;
            mEndName = mSearchName;
            mEtEnd.setText(mEndName);
            showDetail(mEndName,false);
        }
    }

    private void hideKeyboard() {
        View viewFocus = this.getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
    }


    /**
     *
     * @param name
     * @param ifDraworSearch true=draw and false=search
     */
    @Override
    public void showDetail(String name,boolean ifDraworSearch){
        mLayoutDetails.setVisibility(View.VISIBLE);
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
                    mTvSite.setText(mNowPointDetails.getName());
                    if (ifDraworSearch) {
                        mTvDetail.setText(String.format("%sm米  |   用时约%s分钟", String.valueOf(mMapPresent.getDistance()), String.valueOf(mMapPresent.getTime())));
                    }else
                        mTvDetail.setText(detail.getPlat().getInfo());
//                    if (ifDraworSearch)
//                        mTvDetail.setText(String.format("%sm米  |   用时约%s分钟", String.valueOf(mMapPresent.getDistance()), String.valueOf(mMapPresent.getTime())));
//                    else
//                        mTvDetail.setText(detail.getPlat().getInfo());
                    mBtnMore.setEnabled(true);
                }, Throwable::printStackTrace);
    }

}
