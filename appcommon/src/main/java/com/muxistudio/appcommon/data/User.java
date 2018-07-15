package com.muxistudio.appcommon.data;

/**
 * Created by ybao on 16/4/28.
 * 觉得只是用来存储及读字段也不用调用 get，set，太冗余了
 */
public class User {

    public String sid;
    public String password;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(){}
    public User(String sid,String password){
        this.sid = sid;
        this.password = password;
    }
}
