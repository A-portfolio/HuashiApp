package net.muxi.huashiapp.event;

import net.muxi.huashiapp.common.data.User;

/**
 * Created by kolibreath on 17-12-20.
 */

public class RefreshSessionEvent {
    public User user;

    public RefreshSessionEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
