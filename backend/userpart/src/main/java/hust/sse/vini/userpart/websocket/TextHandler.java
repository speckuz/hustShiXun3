package hust.sse.vini.userpart.websocket;

import hust.sse.vini.userpart.chat.PendingMsg;
import hust.sse.vini.userpart.chat.PendingMsgRepo;
import hust.sse.vini.userpart.chat.TargetMsgRepo;
import hust.sse.vini.userpart.chat.TargetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;
import java.util.List;


@Component
public class TextHandler extends TextWebSocketHandler {
    @Autowired
    private TargetMsgRepo targetMsgRepo;
    @Autowired
    private PendingMsgRepo pendingMsgRepo;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer linkId = (Integer) session.getAttributes().get("linkId");
        SessionMap.addSession(linkId, session);
        System.out.println(linkId+"接入连接池，寻找可能的残留信息");
        List<PendingMsg> tempMsgIds = pendingMsgRepo.getPendingMsgsByTargetId(linkId);
        for(PendingMsg msg:tempMsgIds){
            session.sendMessage(new TextMessage(targetMsgRepo.getByMsgId(msg.getMsgIndex()).toString()));
            System.out.println("收到消息"+targetMsgRepo.getByMsgId(msg.getMsgIndex()).getMsgPayload());
            targetMsgRepo.deleteByMsgId(msg.getMsgIndex());
        }
        pendingMsgRepo.deletePendingMsgsByTargetId(linkId);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msgPayload = message.getPayload();
        System.out.println("发送方:"+session.getAttributes().get("linkId")+"\t"+"发送内容:"+msgPayload);
        //获取到文本信息存储到TargetMsg(String id;Integer msgId;TextMessage msg;)的数据Collection中:
        Integer targetId = (Integer) session.getAttributes().get("targetId");
        if(SessionMap.contains(targetId)){
            WebSocketSession targetSession = SessionMap.querySession(targetId);
            TargetMsg targetMsg = new TargetMsg(message.getPayload(), "text", new Date(), (Integer) session.getAttributes().get("linkId"),targetId);
            targetSession.sendMessage(new TextMessage(targetMsg.toString()));
        }else {
            int oldCount = (int) targetMsgRepo.count();
            //计算时间
            TargetMsg targetMsg= new TargetMsg(message.getPayload(), "text", new Date(), (Integer) session.getAttributes().get("linkId"),targetId);
            targetMsg.setMsgId(oldCount+1);
            Integer msgId = targetMsgRepo.save(targetMsg).getMsgId();
            //把msg存到待接收队列中
            pendingMsgRepo.save(new PendingMsg(targetId, msgId));
            System.out.println(msgId + "号消息发送给" + targetId + "号用户");
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object linkId = session.getAttributes().get("linkId");
        SessionMap.removeSession((Integer)linkId);
        System.out.println("连接关闭");
    }

}
