package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by ybao on 16/6/21.
 * 搜索关键字返回的图书列表
 */
public class BookSearchResult {

    /**
     * max : 5
     * per_page : 20
     */

    private MetaBean meta;
    /**
     * bid : TP312/GJ 7
     * book : 1.Java语言规范:基于Java SE 8:Java SE 8 edition
     * intro : 机械工业出版社
     * id : 0001315657
     * author : (美) 詹姆斯·高斯林 ... [等] 著
     */

    private List<ResultsBean> results;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class MetaBean {
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
        private String bid;
        private String book;
        private String intro;
        private String id;
        private String author;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
