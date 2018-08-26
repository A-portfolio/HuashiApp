package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.graphics.Color;
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
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.overlay.WalkRouteOverlay;

public class MapPresent {
    private LatLonPoint mMyLocation;

    private AMap aMap;
    private RouteSearch routeSearch;
    private WalkRouteOverlay walkRouteOverlay;

    private float time;
    private float distance;

    public float getTime() {
        return time;
    }

    public float getDistance() {
        return distance;
    }

    private final static String TAG="GAODE";

    public MapPresent(AMap aMap){
        this.aMap=aMap;
    }

    public void setlocation(){
        final MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000*30); //设置连续定位模式下的定位间隔，一分钟定位一次
        myLocationStyle.strokeWidth(0.5F);
        myLocationStyle.strokeColor(Color.argb(130,197,229,227));
        myLocationStyle.radiusFillColor(Color.argb(130,197,229,227));
        Logger.i(Float.toString(myLocationStyle.getStrokeWidth()));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.setMyLocationEnabled(true);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
               mMyLocation=new LatLonPoint(location.getLatitude(),location.getLongitude());
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
                //to=latLonPoint;
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
                //to=latLonPoint;
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

    // TODO: 18-8-24 算出总距离时长 
    public boolean drawRoute(final Context context,String startName,String endName,LatLonPoint startPoint,
                             LatLonPoint endPoint,ChangeListenner change){
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
                        walkRouteOverlay.addToMap();

                        time=walkRouteOverlay.getTime()/60;
                        distance=walkRouteOverlay.getDistance();
                        addStartAndEndMarker(aMap,AMapUtil.convertToLatLng(startPoint),AMapUtil.convertToLatLng(endPoint),startName,endName);
                        change.showDetail(endName,true);
                        walkRouteOverlay.zoomToSpan();

                    }
                }
            }
            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

        if (startPoint!=null&&endPoint!=null){
            RouteSearch.WalkRouteQuery query=new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(startPoint,endPoint));
            routeSearch.calculateWalkRouteAsyn(query);
        }
                return true;
    }
    public void addStartAndEndMarker(AMap aMap,LatLng startPoint,LatLng endPoint,String startName,String endName) {
        aMap.addMarker(new MarkerOptions()
                .position(startPoint).icon(AMapUtil.getStartBitmapDescriptor())
                .title(startName));
        // startMarker.showInfoWindow();

        aMap.addMarker((new MarkerOptions()).position(endPoint)
                .icon(AMapUtil.getEndBitmapDescriptor()).title(endName))
                .setSnippet(distance+"米"+"  |   用时约"+time+"分钟");
        // mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,
        // getShowRouteZoom()));
    }

    public void addMarker(LatLonPoint latLonPoint,String name){
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap. moveCamera(CameraUpdateFactory.changeLatLng(AMapUtil.convertToLatLng(latLonPoint)));
        Marker marker=aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(latLonPoint)).title(name));
    }

    public void onDestory(){
        aMap=null;
    }
}
