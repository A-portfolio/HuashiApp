package net.muxi.huashiapp.common.data;

/**
 * Created by december on 16/7/28.
 */
public class Electricity {

    private String dor;

    private DegreeBean degree;

    private EleBean ele;

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
        private String current;
        //剩余
        private double remain;
        //上月
        private String before;

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
        private String current;
        private String remain;
        private double _ele;
        private String before;

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
