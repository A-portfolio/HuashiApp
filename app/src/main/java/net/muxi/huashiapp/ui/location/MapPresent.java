package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.overlay.WalkRouteOverlay;

public class MapPresent {
    private LatLonPoint mMyLocation;
    private LatLonPoint from;
    private LatLonPoint to;


    private AMap aMap;
    private RouteSearch routeSearch;
    private WalkRouteOverlay walkRouteOverlay;

    private final static String TAG="GAODE";

    public MapPresent(AMap aMap){
        this.aMap=aMap;
    }

    public void setlocation(){
        final MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000*30); //设置连续定位模式下的定位间隔，一分钟定位一次
        myLocationStyle.strokeWidth((float)0.1);// TODO: 2018/8/24 参数无效 
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
               from= mMyLocation=new LatLonPoint(location.getLatitude(),location.getLongitude());
            }
        });
    }

    //可能为null
    public LatLonPoint getMyLocation() {
        return mMyLocation;
    }

    //起点的搜索，初始值为自己的坐标
    // TODO: 18-8-24 搜索重构 
    public void fromSearch(final String keyWord, Context context){
        PoiSearch.Query query=new PoiSearch.Query(keyWord,"","武汉");
        PoiSearch poiSearch=new PoiSearch(context,query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.d(TAG, "onPoiSearched-result code: "+i);
                LatLonPoint latLonPoint=poiResult.getPois().get(0).getLatLonPoint();
                to=latLonPoint;
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                LatLng p=new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude());
                aMap. moveCamera(CameraUpdateFactory.changeLatLng(p));
                Marker marker=aMap.addMarker(new MarkerOptions().position(p).title(keyWord));
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    //终点的搜索
    public void ToPoiSearch(final String keyWord, Context context){
        PoiSearch.Query query=new PoiSearch.Query(keyWord,"","武汉");
        PoiSearch poiSearch=new PoiSearch(context,query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.d(TAG, "onPoiSearched-result code: "+i);
                LatLonPoint latLonPoint=poiResult.getPois().get(0).getLatLonPoint();
                to=latLonPoint;
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                LatLng p=new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude());
                aMap. moveCamera(CameraUpdateFactory.changeLatLng(p));
                Marker marker=aMap.addMarker(new MarkerOptions().position(p).title(keyWord));
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    public void drawRoute(final Context context){
        routeSearch=new RouteSearch(context);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                aMap.clear();
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                        WalkPath walkPath = walkRouteResult.getPaths().get(0);
                        if (walkRouteOverlay != null) {
                            walkRouteOverlay.removeFromMap();
                        }
                        walkRouteOverlay = new WalkRouteOverlay(context, aMap, walkPath,
                                walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                        LatLonPoint l = walkRouteOverlay.getLastWalkPoint(walkPath.getSteps().get(0));
                        Log.d(TAG, "onWalkRouteSearched: " + l.toString());

                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                    }
                }
            }
            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

        if (from!=null&&to!=null){
            RouteSearch.WalkRouteQuery query=new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(from,to));
            routeSearch.calculateWalkRouteAsyn(query);
        }

    }


}
