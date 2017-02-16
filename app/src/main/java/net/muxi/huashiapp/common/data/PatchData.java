package net.muxi.huashiapp.common.data;

/**
 * Created by ybao on 16/8/15.
 */
public class PatchData {

    /**
     * version : v1
     * download : http://xxx.com
     * update : 2016-0802
     * intro : 这是一次史无前例的更新
     * size : 100M
     */

    public String version;
    public String download;
    public String update;
    public String intro;
    public String size;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
