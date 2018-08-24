package net.muxi.huashiapp.ui.location.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by yue on 2018/8/21.
 */

public class PointDetails implements Parcelable {

    /**
     * name : yy楼
     * url : ["string"]
     * points : [0]
     * info : string
     */

    private String name;
    private String info;
    private String []url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(info);
        dest.writeStringArray(url);
    }
    public static final Parcelable.Creator<PointDetails> CREATOR= new Creator<PointDetails>() {
        @Override
        public PointDetails createFromParcel(Parcel source) {
            PointDetails p=new PointDetails();
            p.name=source.readString();
            p.info=source.readString();
            p.url=source.createStringArray();
            return null;
        }

        @Override
        public PointDetails[] newArray(int size) {
            return new PointDetails[size];
        }
    };
}
