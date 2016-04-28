package net.muxi.huashiapp.common.util;

import java.io.Serializable;

/**
 * Created by december on 16/4/27.
 */
public class News implements Serializable {
    //新闻标题，内容，图片
    private String title;
    private String desc;
    private int photoId;

    public News(String name,String age,int photoId){
        this.title=name;
        this.desc = age;
        this.photoId = photoId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPhotoId() {
        return photoId;
    }
}
