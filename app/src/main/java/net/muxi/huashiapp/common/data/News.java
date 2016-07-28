package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by december on 16/7/28.
 */
public class News {

    private String content;
    private String date;
    private String title;
    private List<?> appendix_list;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<?> getAppendix_list() {
        return appendix_list;
    }

    public void setAppendix_list(List<?> appendix_list) {
        this.appendix_list = appendix_list;
    }
}
