package hust.sse.vini.userpart.communication;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document
public class SavedMsg {
    @Id
    //mongo用id
    private String id;
    //消息id
    private Integer msgId;
    //消息主体
    private String msgPayload;
    //消息类型
    private String msgType;
    //消息时间
    private Date sendTime;
    //消息发送者
    private Integer sendId;
    //消息接收者
    private Integer receiveId;
    //消息来源
    private String msgSource;
    //动态id
    private Integer sceneryId;
    //评论序号
    private Integer commentId;
    //通过的群聊序号
    private Integer groupId;

    public SavedMsg(String msgPayload, String msgType, Date sendTime, Integer sendId, Integer receiveId, String msgSource, Integer sceneryId, Integer commentId, Integer groupId) {
        this.msgPayload = msgPayload;
        this.msgType = msgType;
        this.sendTime = sendTime;
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.msgSource = msgSource;
        this.sceneryId = sceneryId;
        this.commentId = commentId;
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return String.format("SavedMsg:[msgPayload='%s',msgType='%s',sendTime='%s',sendId='%d',receiveId='%d',msgSource='%s']", msgPayload, msgType, sendTime, sendId, receiveId, msgSource);
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getMsgPayload() {
        return msgPayload;
    }

    public void setMsgPayload(String msgPayload) {
        this.msgPayload = msgPayload;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public Integer getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(Integer sceneryId) {
        this.sceneryId = sceneryId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
