package net.muxi.huashiapp.common.data;

/**
 * Created by kolibreath on 18-2-25.
 */


//后台缓存用户信息
public class UserInfo {
    /**
     * sid : 0
     * password : string
     */

    private int sid;
    private String password;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfo(int sid,String password){
        this.sid = sid;
        this.password = password;
    }
}
