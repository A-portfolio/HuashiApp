package net.muxi.huashiapp.ui.location.data;

import java.util.List;

/**
 * Created by yue on 2018/8/21.
 */

public class PointDetails {

    /**
     * name : yy楼
     * url : ["string"]
     * points : [0]
     * info : string
     */

    private String name;
    private String info;
    private List<String> url;
    private List<Double> points;

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

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public List<Double> getPoints() {
        return points;
    }

    public void setPoints(List<Double> points) {
        this.points = points;
    }
}
