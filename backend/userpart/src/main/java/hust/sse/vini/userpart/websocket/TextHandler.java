package hust.sse.vini.userpart.websocket;

import com.alibaba.fastjson.JSON;
import hust.sse.vini.userpart.communication.*;
import hust.sse.vini.userpart.group.Group;
import hust.sse.vini.userpart.group.GroupRepository;
import hust.sse.vini.userpart.user.User;
import hust.sse.vini.userpart.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    private final HashSet<String> srcSet = new HashSet<>();
    private final HashSet<String> typeSet = new HashSet<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        srcSet.add("group");
        srcSet.add("friend");
        srcSet.add("scenery");

        typeSet.add("text");
        typeSet.add("image");
        typeSet.add("location");
        typeSet.add("voice");
        //检查之前是否有离线消息
        Integer linkId = (Integer) session.getAttributes().get("linkId");
        SessionMap.addSession(linkId, session);
        System.out.println(linkId+"接入连接池，寻找可能的残留信息");
        List<PendingMsg> tempMsgIds = pendingMsgRepo.getPendingMsgsByTargetId(linkId);
        for(PendingMsg msg:tempMsgIds){
            SavedMsg savedMsg = savedMsgRepo.getByMsgId(msg.getMsgIndex());
            PushMsgJson pushMsgJson = new PushMsgJson(savedMsg.getMsgPayload(), savedMsg.getMsgType(), savedMsg.getSendTime(), savedMsg.getSendId(), savedMsg.getMsgSource(), savedMsg.getSceneryId(), savedMsg.getCommentId(), savedMsg.getGroupId());
            session.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
            System.out.println("收到消息"+ savedMsgRepo.getByMsgId(msg.getMsgIndex()).getMsgPayload());
            savedMsgRepo.deleteByMsgId(msg.getMsgIndex());
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
        String msgSource = receiveMsgJson.getMsgSource();
        String msgType = receiveMsgJson.getMsgType();
        Integer targetId = receiveMsgJson.getReceiveId();
        //处理不合法逻辑
        if((!typeSet.contains(msgType))||(!srcSet.contains(msgSource))){
            session.sendMessage(new TextMessage("wrong msg source or type"));
            return;
        }
        if(!msgSource.equals("scenery")&&((null!=receiveMsgJson.getCommentId())||(null!=receiveMsgJson.getSceneryId()))){
            session.sendMessage(new TextMessage("forbidding commentId or sceneryId"));
            return;
        }
        if(msgSource.equals("scenery")&&(null==receiveMsgJson.getCommentId())&&(null==receiveMsgJson.getSceneryId())){
            session.sendMessage(new TextMessage("commentId and sceneryId missing."));
            return;
        }
        if("group".equals(msgSource)){
            Group group = groupRepo.getById(targetId);
            if(null==group){
                session.sendMessage(new TextMessage("Invalid groupId"));
                return;
            }
            List<Integer> targetIds = group.getMembers();
            for(Integer memberId:targetIds){
                //获取到文本信息存储到SavedMsg(String id;Integer msgId;TextMessage msg;)的数据Collection中:
                if(memberId.equals(sendId))
                    continue;
                if(SessionMap.contains(memberId)){
                    WebSocketSession memberSession = SessionMap.querySession(memberId);

                    PushMsgJson pushMsgJson = new PushMsgJson(msgPayload, msgType, new Date(), sendId, msgSource, receiveMsgJson.getSceneryId(), receiveMsgJson.getCommentId(), targetId);

                    memberSession.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
                }else {
                    int oldCount = (int) savedMsgRepo.count();
                    //计算时间
                    SavedMsg savedMsg = new SavedMsg(msgPayload, msgType, new Date(), sendId,memberId, msgSource, receiveMsgJson.getSceneryId(), receiveMsgJson.getCommentId(), targetId);
                    savedMsg.setMsgId(oldCount+1);
                    Integer msgId = savedMsgRepo.save(savedMsg).getMsgId();
                    //把msg存到待接收队列中
                    pendingMsgRepo.save(new PendingMsg(memberId, msgId));
                    System.out.println(msgId + "号消息未发送给" + memberId + "号用户");
                }
            }
        }else{
            //获取到文本信息存储到SavedMsg(String id;Integer msgId;TextMessage msg;)的数据Collection中:
            User user = userRepo.findByUserId(targetId);
            if(null==user){
                session.sendMessage(new TextMessage("Invalid receiveUserId."));
                return;
            }
            if(SessionMap.contains(targetId)){
                WebSocketSession targetSession = SessionMap.querySession(targetId);

                PushMsgJson pushMsgJson = new PushMsgJson(msgPayload, msgType, new Date(), sendId, msgSource, receiveMsgJson.getSceneryId(), receiveMsgJson.getCommentId(), null);

                targetSession.sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(pushMsgJson, "yyyy-MM-dd HH:mm:ss")));
            }else {
                int oldCount = (int) savedMsgRepo.count();
                //计算时间
                SavedMsg savedMsg = new SavedMsg(msgPayload, msgType, new Date(), sendId,targetId, msgSource, receiveMsgJson.getSceneryId(), receiveMsgJson.getCommentId(), null);
                savedMsg.setMsgId(oldCount+1);
                Integer msgId = savedMsgRepo.save(savedMsg).getMsgId();
                //把msg存到待接收队列中
                pendingMsgRepo.save(new PendingMsg(targetId, msgId));
                System.out.println(msgId + "号消息发送给" + targetId + "号用户");
            }
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object linkId = session.getAttributes().get("linkId");
        SessionMap.removeSession((Integer)linkId);
        System.out.println("连接关闭");
    }

}
