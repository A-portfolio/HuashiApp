package net.muxi.huashiapp.common.data;

/**
 * Created by december on 16/7/28.
 */
public class Electricity {


    private String dor;

    private DegreeEntity degree;

    private EleEntity ele;

    private String type;

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public DegreeEntity getDegree() {
        return degree;
    }

    public void setDegree(DegreeEntity degree) {
        this.degree = degree;
    }

    public EleEntity getEle() {
        return ele;
    }

    public void setEle(EleEntity ele) {
        this.ele = ele;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DegreeEntity {
        private String current;  //当月用电
        private String remain;   //剩余度数
        private String before;   //上月用电

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

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }
    }

    public static class EleEntity {
        private String current;   //当月电费
        private double remain;
        private double _ele;
        private String before;    //上月月电费

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
