package com.muxistudio.appcommon.event;

/**
 * Created by kolibreath on 18-2-5.
 */

public class AuditCourseEvent {
    public boolean isRefresh=false;
    public AuditCourseEvent(){}
    public AuditCourseEvent(boolean isRefresh){
        this.isRefresh=isRefresh;
    }
}
