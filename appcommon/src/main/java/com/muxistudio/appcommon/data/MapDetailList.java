package com.muxistudio.appcommon.data;

import java.util.List;

public class MapDetailList {


    private List<PointsBean> points;

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public static class PointsBean {
        /**
         * name : 八号教学楼
         * points : [30.526619,114.365561]
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
