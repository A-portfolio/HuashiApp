package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by ybao on 16/6/21.
 * 搜索关键字返回的图书列表
 */

public class BookSearchResult {

    /**
     * meta : {"max":5,"per_page":20}
     * result : [{"book":"1.Java EE框架技术:SpringMVC+Spring+MyBatis","author":"主编陈永政, 张正龙","publisher":"西安电子科技大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001531112","id":"0001531112"},{"book":"2.JAVA程序设计理实一体化教程","author":"廖丽, 李学国主编","publisher":"西南交通大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001532978","id":"0001532978"},{"book":"3.Java设计模式及应用案例.2版","author":"金百东，刘德山编著","publisher":"人民邮电出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001567094","id":"0001567094"},{"book":"4.Java网络编程案例教程:微课版","author":"董相志，唐玉凯，张岳强编著","publisher":"清华大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001567068","id":"0001567068"},{"book":"5.Java语言程序设计教程","author":"骆伟主编","publisher":"电子工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001567077","id":"0001567077"},{"book":"6.Java EE核心框架实战.第2版","author":"高洪岩著","publisher":"人民邮电出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001525669","id":"0001525669"},{"book":"7.Java微服务实战","author":"赵计刚著","publisher":"电子工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001563815","id":"0001563815"},{"book":"8.Java程序设计:基础、编程抽象与算法策略","author":"(美) 埃里克 S. 罗伯茨著","publisher":"机械工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001532970","id":"0001532970"},{"book":"9.看透JavaScript:原理、方法与实践:principles, methods and practice","author":"韩路彪著","publisher":"清华大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001518096","id":"0001518096"},{"book":"10.深度学习:Java语言实现","author":"(日) 巣笼悠辅著","publisher":"机械工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001517865","id":"0001517865"},{"book":"11.JavaScript从入门到精通:标准版","author":"未来科技编著","publisher":"中国水利水电出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001517860","id":"0001517860"},{"book":"12.Web前端开发精品课:JavaScript基础教程","author":"莫振杰著","publisher":"人民邮电出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001514825","id":"0001514825"},{"book":"13.Java Web轻量级开发面试教程","author":"孟宪福, 胡书敏, 金华编著","publisher":"电子工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001514829","id":"0001514829"},{"book":"14.Java从入门到精通:实例版.第2版","author":"明日科技编著","publisher":"清华大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001517864","id":"0001517864"},{"book":"15.Java核心技术.卷Ⅱ,高级特性.Volume Ⅱ,Advanced features","author":"(美) 凯 S. 霍斯特曼著","publisher":"机械工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001544555","id":"0001544555"},{"book":"16.JavaScript concurrency.影印版","author":"Adam Boduch著","publisher":"东南大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001505003","id":"0001505003"},{"book":"17.JavaScript和jQuery实战手册","author":"David Sawyer McFarland著","publisher":"机械工业出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001496444","id":"0001496444"},{"book":"18.Java语言程序设计教程","author":"刘政怡主编","publisher":"安徽大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001465037","id":"0001465037"},{"book":"19.Java Web开发教程:基于Struts2+Hibernate+Spring","author":"丁毓峰, 毛雪涛编著","publisher":"人民邮电出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001470323","id":"0001470323"},{"book":"20.JAVA开发实用技术","author":"主编梁勇强, 蒙峭缘","publisher":"西南交通大学出版社","bid":"fff","bookurl":"http://202.114.34.15/opac/item.php?marc_no=0001476318","id":"0001476318"}]
     */

    private MetaBean meta;
    private List<ResultsBean> result;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<ResultsBean> getResult() {
        return result;
    }

    public void setResult(List<ResultsBean> result) {
        this.result = result;
    }

    public static class MetaBean {
        /**
         * max : 5
         * per_page : 20
         */

        private int max;
        private int per_page;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }
    }

    public static class ResultsBean {
        /**
         * book : 1.Java EE框架技术:SpringMVC+Spring+MyBatis
         * author : 主编陈永政, 张正龙
         * publisher : 西安电子科技大学出版社
         * bid : fff
         * bookurl : http://202.114.34.15/opac/item.php?marc_no=0001531112
         * id : 0001531112
         */

        public String book;
        public String author;
        public String publisher;
        public String bid;
        public String bookurl;
        public String id;

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBookurl() {
            return bookurl;
        }

        public void setBookurl(String bookurl) {
            this.bookurl = bookurl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
