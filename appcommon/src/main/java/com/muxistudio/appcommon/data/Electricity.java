package com.muxistudio.appcommon.data;

/**
 * Created by december on 16/7/28.
 */
public class Electricity {

    public String dor;

    public DegreeBean degree;

    public EleBean ele;

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public DegreeBean getDegree() {
        return degree;
    }

    public void setDegree(DegreeBean degree) {
        this.degree = degree;
    }

    public EleBean getEle() {
        return ele;
    }

    public void setEle(EleBean ele) {
        this.ele = ele;
    }

    public static class DegreeBean {
        //当月
        public String current;
        //剩余
        public double remain;
        //上月
        public String before;

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public double getRemain() {
            return remain;
        }

        public void setRemain(double remain) {
            this.remain = remain;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }
    }

    public static class EleBean {
        public String current;
        public String remain;
        public double _ele;
        public String before;

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public double get_ele() {
            return _ele;
        }

        public void set_ele(double _ele) {
            this._ele = _ele;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }
    }
}
