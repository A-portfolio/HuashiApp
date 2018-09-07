package com.muxistudio.appcommon.data;

import com.muxistudio.appcommon.user.UserAccountManager;

public class FlowUserInfo {

    /**
     * foo : 2016210942
     * bar : muxistudio
     * checkcode : 9518
     * account : 2016210942
     * userinfo : muxistudio
     */

    private String foo;
    private String bar;
    private String checkcode;
    private String account;
    private String password;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FlowUserInfo(String userCheckcode){
        User user = UserAccountManager.getInstance().getInfoUser();
        checkcode = userCheckcode;
        foo = user.getSid();
        bar = user.getPassword();
        account = user.getSid();
        password = user.getPassword();
    }
}
