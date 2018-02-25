package net.muxi.huashiapp.common.data;

/**
 * Created by kolibreath on 18-2-24.
 */

public class Hint {


    /**
     * msg : 这是信息
     * detail : 这是细节
     * version : 这是版本号
     * type : 这是类型
     * time : 这是类型
     * update : 1519538141
     */

    private String msg;
    private String detail;
    private String version;
    private String type;
    private String time;
    private int update;

    public Hint(){}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }
}
