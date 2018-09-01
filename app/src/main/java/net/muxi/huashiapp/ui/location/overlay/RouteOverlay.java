package net.muxi.huashiapp.ui.location.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlay {
    protected List<Marker> mStationMarkers = new ArrayList<Marker>();
    private List<Polyline> mAllPolyLines = new ArrayList<Polyline>();
    protected Marker mStartMarker;
    protected Marker mEndMarker;
    protected LatLng mStartPoint;
    protected LatLng mEndPoint;
    protected AMap mAMap;
    private Bitmap mStartBit, mEndBit, mBusBit, mWalkBit, mDriveBit;
    protected boolean mNodeIconVisible = true;

    public RouteOverlay(Context context) {
        Context mContext = context;
    }

    /**
     * 去掉walkRouteOverlay上所有的Marker。
     */
    public void removeFromMap() {
        if (mStartMarker != null) {
            mStartMarker.remove();

        }
        if (mEndMarker != null) {
            mEndMarker.remove();
        }
        for (Marker marker : mStationMarkers) {
            marker.remove();
        }
        for (Polyline line : mAllPolyLines) {
            line.remove();
        }
        destroyBit();
    }

    private void destroyBit() {
        if (mStartBit != null) {
            mStartBit.recycle();
            mStartBit = null;
        }
        if (mEndBit != null) {
            mEndBit.recycle();
            mEndBit = null;
        }
        if (mBusBit != null) {
            mBusBit.recycle();
            mBusBit = null;
        }
        if (mWalkBit != null) {
            mWalkBit.recycle();
            mWalkBit = null;
        }
        if (mDriveBit != null) {
            mDriveBit.recycle();
            mDriveBit = null;
        }
    }

    /**
     * 给步行Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    // TODO: 18-8-24 根据是否有详情判断图标 
    protected BitmapDescriptor getWalkBitmapDescriptor() {

        return BitmapDescriptorFactory.fromResource(R.drawable.ic_map_oval);
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @bounds 一个矩形区域，代表当前路线的矩形区域
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mStartPoint != null) {
            if (mAMap == null)
                return;
            try {
                LatLngBounds bounds = getLatLngBounds();
                mAMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 50));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    protected LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(new LatLng(mStartPoint.latitude, mStartPoint.longitude));
        b.include(new LatLng(mEndPoint.latitude, mEndPoint.longitude));
        for (Polyline polyline : mAllPolyLines) {
            for (LatLng point : polyline.getPoints()) {
                b.include(point);
            }
        }
        return b.build();
    }

    /**
     * 路段节点图标控制显示接口。
     *
     * @param visible true为显示节点图标，false为不显示。
     * @since V2.3.1
     */
    public void setNodeIconVisibility(boolean visible) {
        try {
            mNodeIconVisible = visible;
            if (this.mStationMarkers != null && this.mStationMarkers.size() > 0) {
                for (int i = 0; i < this.mStationMarkers.size(); i++) {
                    this.mStationMarkers.get(i).setVisible(visible);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void addStationMarker(MarkerOptions options) {
        if (options == null) {
            return;
        }

        Marker marker = mAMap.addMarker(options);
        if (marker != null) {
            mStationMarkers.add(marker);
        }

    }

    protected void addPolyLine(PolylineOptions options) {
        if (options == null) {
            return;
        }
        Polyline polyline = mAMap.addPolyline(options);
        if (polyline != null) {
            mAllPolyLines.add(polyline);
        }
    }

    protected float getRouteWidth() {
        return 18f;
    }

    protected int getWalkColor() {
        return Color.parseColor("#6db74d");
    }


}
