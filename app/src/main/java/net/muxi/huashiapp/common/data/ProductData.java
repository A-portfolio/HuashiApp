package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by ybao on 16/8/20.
 */
public class ProductData {


    /**
     * update : 1.49760317E9
     * _product : [{"url":"https://xueer.muxixyz.com","intro":"huashi xuanke","name":"学而","icon":"http://static.muxixyz.com/ic_xueer.png"}]
     */

    public double update;
    public List<ProductEntity> _product;

    public static class ProductEntity {
        /**
         * url : https://xueer.muxixyz.com
         * intro : huashi xuanke
         * name : 学而
         * icon : http://static.muxixyz.com/ic_xueer.png
         */

        public String url;
        public String intro;
        public String name;
        public String icon;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }


    public ProductData(double update, List<ProductEntity> _product) {
        this.update = update;
        this._product = _product;
    }


    public double getUpdate() {
        return update;
    }

    public void setUpdate(double update) {
        this.update = update;
    }

    public List<ProductEntity> get_product() {
        return _product;
    }

    public void set_product(List<ProductEntity> _product) {
        this._product = _product;
    }
}
