package hust.sse.vini.userpart.websocket;


import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

//创建一个session的map记录每个连接建立后的session与用户id的对应关系
public class SessionMap {
    //建立一个统一的sessionMap
    private static final ConcurrentHashMap<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    //自定义的增删查函数
    static void addSession(Integer linkId, WebSocketSession session){
        sessionMap.put(linkId, session);
    }

    static void removeSession(Integer linkId){
        sessionMap.remove(linkId);
    }

    static WebSocketSession querySession(Integer linkId){
        return sessionMap.get(linkId);
    }

    static boolean contains(Integer linkId){
        return sessionMap.containsKey(linkId);
    }
}
