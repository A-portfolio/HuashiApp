package com.muxistudio.appcommon.event;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/5/3
 */

public class LoginSuccessEvent {

    public String targetActivityName;

    public LoginSuccessEvent(String targetActivityName) {
        this.targetActivityName = targetActivityName;
    }
}
