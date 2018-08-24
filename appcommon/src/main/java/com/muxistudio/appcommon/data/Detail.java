package com.muxistudio.appcommon.data;

import java.util.List;

public class Detail {

    /**
     * plat : {"name":"123","url":["djk","hsu"],"points":[0,0],"info":"等待添加"}
     */

    private PlatBean plat;

    public PlatBean getPlat() {
        return plat;
    }

    public void setPlat(PlatBean plat) {
        this.plat = plat;
    }

    public static class PlatBean {
        /**
         * name : 123
         * url : ["djk","hsu"]
         * points : [0,0]
         * info : 等待添加
         */

        private String name;
        private String info;
        private List<String> url;
        private List<Double> points;

        public PlatBean(String name, String info, List<String> url, List<Double> points) {
            this.name = name;
            this.info = info;
            this.url = url;
            this.points = points;
        }

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
}
