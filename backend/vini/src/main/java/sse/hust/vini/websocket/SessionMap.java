package sse.hust.vini.websocket;


import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

//创建一个session的map记录每个连接建立后的session与用户id的对应关系
public class SessionMap {
    //建立一个统一的sessionMap
    private static final ConcurrentHashMap<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    //自定义的增删查函数
    public static void addSession(Integer linkId, WebSocketSession session){
        sessionMap.put(linkId, session);
    }

    public static void removeSession(Integer linkId){
        sessionMap.remove(linkId);
    }

    public static WebSocketSession querySession(Integer linkId){
        return sessionMap.get(linkId);
    }

    public static boolean contains(Integer linkId){
        return sessionMap.containsKey(linkId);
    }
}
