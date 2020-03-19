package net.muxi.huashiapp.login;

import retrofit2.HttpException;

public class SingleSignException extends Exception {

    public SingleSignException(){
        super("单点登录异常");
    }
}
