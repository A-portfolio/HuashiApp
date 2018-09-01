package com.muxistudio.common.jsbridge;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;


/**
 * Created by ybao on 16/10/8.
 */

public class BridgeWebView extends WebView {

    public HashMap<String, BridgeHandler> handlers;
    public HashMap<String, BridgeHandler> responseHandlers;
    private InjectedObject mInjectedObject;

    private int uniqueId = 1000;

    public static final String INJECTED_OBJECT_NAME = "javaInterface";

    public static final String JS_SEND_DATA_FORMAT = "javascript:window.YAJB_INSTANCE._trigger('%s');";

    public BridgeWebView(Context context) {
        this(context, null);
    }

    public BridgeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        handlers = new HashMap<>();
        responseHandlers = new HashMap<>();
        mInjectedObject = new InjectedObject();
        addJavascriptInterface(mInjectedObject, INJECTED_OBJECT_NAME);
        setWebViewClient(new BridgeWebClient(this));
    }


    /**
     * inject init data like {"platform":"android","data":"jsondata"}
     */
    public void setInitData(Object data) {
        mInjectedObject.setData(data);
    }

    public void send(String event, int param) {
        send(event, param, null);
    }

    public void send(String event, boolean param) {
        send(event, param, null);
    }

    public void send(String event, float param) {
        send(event, param, null);
    }

    public void send(String event, String param) {
        send(event, param, null);
    }

    /**
     * send event,the object should be jsonObject
     */
    public void send(String event, Object jsonObject) {
        send(event, jsonObject, null);
    }

    /**
     * send event , handle returned data with BridgeHandler
     *
     * @param event           event name
     * @param callbackHandler do something with returned data
     */
    public void send(String event, Object jsonObject, BridgeHandler callbackHandler) {
        send(event, jsonObject, callbackHandler, ++uniqueId);
    }

    public void send(String event, Object jsonObject, BridgeHandler callbackHandler, int id) {
        Message message = new Message();
        message.event = event;
        message.data = jsonObject;
        message.id = id;
        try {
            String json = new Gson().toJson(message);
            if (callbackHandler != null) {
                responseHandlers.put(event + "Resolved" + (message.id), callbackHandler);
            }
            Log.d("jsbridge", String.format(JS_SEND_DATA_FORMAT, json));
            this.loadUrl(String.format(JS_SEND_DATA_FORMAT, json), null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("the object should be jsonObject");
        }
    }

    /**
     * register Java Handler to handle js event
     */
    public void register(String event, BridgeHandler handler) {
        handlers.put(event, handler);
    }

}
