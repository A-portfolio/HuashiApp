package net.muxi.huashiapp.event;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/27
 */

public class RefreshFinishEvent {

    private boolean refreshResult;

    public RefreshFinishEvent(boolean refreshResult) {
        this.refreshResult = refreshResult;
    }

    public boolean isRefreshResult() {
        return refreshResult;
    }

    public void setRefreshResult(boolean refreshResult) {
        this.refreshResult = refreshResult;
    }
}
