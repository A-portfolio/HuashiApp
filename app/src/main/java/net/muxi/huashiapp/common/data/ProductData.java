package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by ybao on 16/8/20.
 */
public class ProductData {


    /**
     * _products : [{"icon":"http://static.muxixyz.com/ic-xueer.png","intro":"华师课程经验挖掘机","name":"学而","nickname":null,"url":"https://xueer.muxixyz.com"}]
     * update : 1.471793717565404E9
     */

    private double update;
    /**
     * icon : http://static.muxixyz.com/ic-xueer.png
     * intro : 华师课程经验挖掘机
     * name : 学而
     * nickname : null
     * url : https://xueer.muxixyz.com
     */

    private List<ProductsBean> _products;

    public double getUpdate() {
        return update;
    }

    public void setUpdate(double update) {
        this.update = update;
    }

    public List<ProductsBean> get_products() {
        return _products;
    }

    public void set_products(List<ProductsBean> _products) {
        this._products = _products;
    }

    public static class ProductsBean {
        private String icon;
        private String intro;
        private String name;
        private Object nickname;
        private String url;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
