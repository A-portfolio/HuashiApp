package com.muxistudio.appcommon.data;

import java.util.List;

public class MapDetailList {

    private List<PointBean> point;

    public List<PointBean> getPoint() {
        return point;
    }

    public void setPoint(List<PointBean> point) {
        this.point = point;
    }

    public static class PointBean {
        /**
         * name : string
         * points : [0]
         */

        private String name;
        private List<Double> points;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Double> getPoints() {
            return points;
        }

        public void setPoints(List<Double> points) {
            this.points = points;
        }
    }
}
