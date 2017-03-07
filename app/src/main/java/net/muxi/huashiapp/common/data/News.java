package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by december on 16/7/28.
 */
public class News {


    /**
     * appendix_list : ["http://www.ccnuyouth.com/system/_content/download.jsp?urltype=news.DownloadAttachUrl&owner=1268342907&wbfileid=2038945","http://www.ccnuyouth.com/__local/8/91/95/1C3B492AD4DA0B1AADB6571C12F_EBB75B99_4DE5.docx"]
     * content : 一、接待领导：校团委副书记 陈娟
     二、时间：2017年3月7日（周二）下午14：30——17：00。三、预约方式：详见附件《校团委关于举办“书记接待日”活动的通知》陈娟：分管校团委宣传部、校园文化部、青年研究中心、校学生会、大学生艺术团等。  
      

     附件：校团委书记接待日（通知）.docx



     校团委办公室2017年3月3日

     编辑：校团委
     * date : 2017-03-03
     * title : 关于第五十五期校团委书记接待日的安排
     */

    private String content;
    private String date;
    private String title;
    private List<String> appendix_list;

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

    public List<String> getAppendix_list() {
        return appendix_list;
    }

    public void setAppendix_list(List<String> appendix_list) {
        this.appendix_list = appendix_list;
    }
}
