package com.muxistudio.appcommon.data;

import java.util.List;

public class StatisticsData {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pid : String
         * deviceId : String
         * type : String
         * mainCat : String
         * subCat : String
         * value : String
         * timestamp : Int
         * extra : String
         */

        private String pid;
        private String deviceId;
        private String type;
        private String mainCat;
        private String subCat;
        private String value;
        private String timestamp;
        private String extra;

        public DataBean(){}
        public DataBean(String pid, String deviceId, String type, String mainCat, String subCat, String value, String timestamp, String extra) {
            this.pid = pid;
            this.deviceId = deviceId;
            this.type = type;
            this.mainCat = mainCat;
            this.subCat = subCat;
            this.value = value;
            this.timestamp = timestamp;
            this.extra = extra;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMainCat() {
            return mainCat;
        }

        public void setMainCat(String mainCat) {
            this.mainCat = mainCat;
        }

        public String getSubCat() {
            return subCat;
        }

        public void setSubCat(String subCat) {
            this.subCat = subCat;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }


        public static class Builder{
            private String pid;
            private String deviceId;
            private String type;
            private String mainCat;
            private String subCat;
            private String value;
            private String timestamp;
            private String extra;

            public Builder(){
                pid="pid";
                deviceId="Unknow";
                type="type";
                mainCat="mainCat";
                subCat="subCat";
                value="value";
                timestamp="time";
                extra="extra";

            }

            public DataBean Build(){

                return new DataBean(pid,deviceId,type,mainCat,subCat,value,timestamp,extra);
            }
            public Builder pid(String pid){
                this.pid=pid;
                return this;
            }

            public Builder deviceId(String deviceId){
                this.deviceId=deviceId;
                return this;
            }

            public Builder type(String type){
                this.type=type;
                return this;
            }

            public Builder mainCat(String mainCat){
                this.mainCat=mainCat;
                return this;
            }
            public Builder subCat(String subCat){
                this.subCat=subCat;
                return this;
            }
            public Builder value(String value){
                this.value=value;
                return this;
            }

            public Builder timestamp(String timestamp){
                this.timestamp=timestamp;
                return this;
            }

            public Builder extra(String extra){
                this.extra=extra;
                return this;
            }

        }
    }
}
