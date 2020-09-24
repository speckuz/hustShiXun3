package com.feiyueve.snsdemo.webservice;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

public class JWebSocketClient extends WebSocketClient implements Serializable {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }


    public JWebSocketClient(URI serverUri, Map<String, String> var3) {
        //1请求地址, 2版本号需和服务端一直, 3上传参数, 4请求超时时间
        super(serverUri,new Draft_6455(),var3, 120000);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("JWebSocketClientonOpen()");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("JWebSocketClientonMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("JWebSocketClientonClose()"+reason);
    }

    @Override
    public void onError(Exception ex) { System.out.println("JWebSocketClientonError()");
    }
}