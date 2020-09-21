package sse.hust.vini.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import sse.hust.vini.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgInceptor implements HandshakeInterceptor {
    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        //获取request里面的参数等信息，设置session
        System.out.println("握手开始");
        Integer linkId = Integer.parseInt(serverHttpRequest.getHeaders().get("Vini-User-Id").get(0));
        if(null==userRepo.findByUserId(linkId)){
            return false;
        }
        map.put("linkId" , linkId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("握手结束");
    }
}
