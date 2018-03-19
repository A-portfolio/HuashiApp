package com.muxistudio.appcommon.event;

/**
 * Created by kolibreath on 17-12-24.
 */

public class VerifyCodeSuccessEvent {

    public String code;
    public VerifyCodeSuccessEvent(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}
