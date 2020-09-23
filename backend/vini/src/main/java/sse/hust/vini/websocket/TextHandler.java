package sse.hust.vini.websocket;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sse.hust.vini.APIReturn;
import sse.hust.vini.communication.*;
import sse.hust.vini.group.Group;
import sse.hust.vini.group.GroupRepository;
import sse.hust.vini.user.User;
import sse.hust.vini.user.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;


@Component
public class TextHandler extends TextWebSocketHandler {
    @Autowired
    private SavedMsgRepo savedMsgRepo;
    @Autowired
    private PendingMsgRepo pendingMsgRepo;
    @Autowired
    private GroupRepository groupRepo;
    @Autowired
    private UserRepository userRepo;

    private final HashSet<Integer> srcSet = new HashSet<>();
    private final HashSet<Integer> typeSet = new HashSet<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        srcSet.add(0);//group
        srcSet.add(1);//friend
        srcSet.add(2);//scenery
        srcSet.add(3);//friendAdd
        srcSet.add(4);//groupAdd

        typeSet.add(0);//text
        typeSet.add(1);//image
        typeSet.add(2);//location
        typeSet.add(3);//voice
        //检查之前是否有离线消息
        Integer linkId = (Integer) session.getAttributes().get("linkId");
        SessionMap.addSession(linkId, session);
        //Set the max limit for text message.
        session.setTextMessageSizeLimit(10485760);
        System.out.println(linkId+"接入连接池，寻找可能的残留信息");
        List<PendingMsg> tempMsgIds = pendingMsgRepo.getPendingMsgsByTargetId(linkId);
        for(PendingMsg msg:tempMsgIds){
            SavedMsg savedMsg = savedMsgRepo.getById(msg.getMsgIndex());
            PushMsgJson pushMsgJson = new PushMsgJson(savedMsg.getMsgPayload(), savedMsg.getMsgType(), savedMsg.getSendTime(), savedMsg.getSendId(), savedMsg.getMsgSource(), savedMsg.getSceneryId(), savedMsg.getCommentId(), savedMsg.getGroupId());
            session.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
            System.out.println("收到消息"+ savedMsgRepo.getById(msg.getMsgIndex()).getMsgPayload());
            savedMsgRepo.deleteById(msg.getMsgIndex());
        }
        pendingMsgRepo.deletePendingMsgsByTargetId(linkId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msgContent = message.getPayload();
        Integer sendId=(Integer) session.getAttributes().get("linkId");
        System.out.println("发送方:"+sendId+"\t"+"发送内容:"+msgContent);
        //解析文本信息
        ReceiveMsgJson receiveMsgJson = JSON.parseObject(msgContent, ReceiveMsgJson.class);
        String msgPayload = receiveMsgJson.getMsgPayload();
        Integer msgSource = receiveMsgJson.getMsgSource();
        Integer msgType = receiveMsgJson.getMsgType();
        Integer targetId = receiveMsgJson.getReceiveId();
        //处理不合法逻辑
        if((!typeSet.contains(msgType))||(!srcSet.contains(msgSource))){
            session.sendMessage(new TextMessage(JSON.toJSONString(APIReturn.apiError(400, "wrong msg source or type"))));
            return;
        }
        if(0==msgSource){
            Group group = groupRepo.getById(targetId);
            if(null==group){
                session.sendMessage(new TextMessage(JSON.toJSONString(APIReturn.apiError(400, "Invalid groupId"))));
                return;
            }
            List<Integer> targetIds = group.getMembers();
            for(Integer memberId:targetIds){
                //获取到文本信息存储到SavedMsg(String id;Integer msgId;TextMessage msg;)的数据Collection中:
                if(memberId.equals(sendId))
                    continue;
                if(SessionMap.contains(memberId)){
                    WebSocketSession memberSession = SessionMap.querySession(memberId);

                    PushMsgJson pushMsgJson = new PushMsgJson(msgPayload, msgType, new Date(), sendId, msgSource, null,null,targetId);

                    memberSession.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
                }else {
                    //int oldCount = (int) savedMsgRepo.count();
                    //计算时间
                    SavedMsg savedMsg = new SavedMsg(msgPayload, msgType, new Date(), sendId,memberId, msgSource, null,null, targetId);
                    //savedMsg.setMsgId(oldCount+1);
                    String msgId = savedMsgRepo.save(savedMsg).getId();
                    //把msg存到待接收队列中
                    pendingMsgRepo.save(new PendingMsg(memberId, msgId));
                    System.out.println(msgId + "号消息未发送给" + memberId + "号用户");
                }
            }
        }else{
            //获取到文本信息存储到SavedMsg(String id;Integer msgId;TextMessage msg;)的数据Collection中:
            User user = userRepo.findByUserId(targetId);
            if(null==user){
                session.sendMessage(new TextMessage(JSON.toJSONString(APIReturn.apiError(400, "Invalid receiveUserId."))));
                return;
            }
            if(SessionMap.contains(targetId)){
                WebSocketSession targetSession = SessionMap.querySession(targetId);

                PushMsgJson pushMsgJson = new PushMsgJson(msgPayload, msgType, new Date(), sendId, msgSource, null,null, null);

                targetSession.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
            }else {
                //int oldCount = (int) savedMsgRepo.count();
                //计算时间
                SavedMsg savedMsg = new SavedMsg(msgPayload, msgType, new Date(), sendId,targetId, msgSource, null, null, null);
                //savedMsg.setMsgId(oldCount+1);
                String msgId = savedMsgRepo.save(savedMsg).getId();
                //把msg存到待接收队列中
                pendingMsgRepo.save(new PendingMsg(targetId, msgId));
                System.out.println(msgId + "号消息发送给" + targetId + "号用户");
            }
        }
        session.sendMessage(new TextMessage(JSON.toJSONString(APIReturn.successfulResult(null))));

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            Object linkId = session.getAttributes().get("linkId");
            SessionMap.removeSession((Integer)linkId);
            System.out.println("连接关闭");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
