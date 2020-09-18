package hust.sse.vini.userpart.websocket;

import cn.hutool.http.HttpUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgInceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        //获取request里面的参数等信息，设置session
        System.out.println("握手开始");
        HashMap<String, String> paramMap = HttpUtil.decodeParamMap(serverHttpRequest.getURI().getQuery(), "utf-8");
        Integer linkId = Integer.parseInt(paramMap.get("linkId"));
        Integer targetId = Integer.parseInt(paramMap.get("targetId"));
        map.put("linkId" , linkId);
        map.put("targetId", targetId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("握手结束");
        //System.out.println(serverHttpRequest.getHeaders().get("Vini-User-Id").get(0));
    }
}
