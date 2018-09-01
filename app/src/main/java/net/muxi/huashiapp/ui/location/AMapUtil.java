package net.muxi.huashiapp.ui.location;

//fixme ??? dandling JavaDoc comment?????????
/**
 *
 */

import android.graphics.Bitmap;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

public class AMapUtil {
    public static int BUFFER_SIZE = 2048;
    //fixme 没有去除的废弃的代码片段？
/*
    public static byte[] inputStreamToByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }
   */

    public static BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_map_start_marker);
    }

    /**
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    public static  BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_map_end_marker);
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }


    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }


    public static Bitmap zoomBitmap(Bitmap bitmap, float res) {
        if (bitmap == null) {
            return null;
        }
        int width, height;
        width = (int) (bitmap.getWidth() * res);
        height = (int) (bitmap.getHeight() * res);
        Bitmap newbmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return newbmp;
    }

}