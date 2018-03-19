package com.muxistudio.appcommon.event;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/27
 */

public class RefreshFinishEvent {

    //如果没有联网的时候就会丢出 UnknownHostException
    //所以自定义了一个code
    public static int SELF_DEFINE_CODE = 600;
    private boolean refreshResult;
    private int code;
    public RefreshFinishEvent(boolean refreshResult){
        this.refreshResult =refreshResult;
    }
    public RefreshFinishEvent(boolean refreshResult,int code) {
        this.refreshResult = refreshResult;
        this.code = code;
    }

    public boolean isRefreshResult() {
        return refreshResult;
    }

    public int getCode(){
        return code;
    }

    public void setRefreshResult(boolean refreshResult) {
        this.refreshResult = refreshResult;
    }
}
