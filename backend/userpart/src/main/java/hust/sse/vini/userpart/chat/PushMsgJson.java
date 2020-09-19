package hust.sse.vini.userpart.chat;

import java.util.Date;

public class PushMsgJson {
    //消息主体
    private String msgPayload;
    //消息类型
    private String msgType;
    //
    private Date sendTime;
    //消息发送者
    private Integer sendId;
    //消息来源
    private String msgSource;
    //动态属性啥的放在这里
    //动态id
    private Integer sceneryId;
    //评论序号
    private Integer commentId;
    //通过的群聊序号
    private Integer groupId;

    public PushMsgJson(String msgPayload, String msgType, Date sendTime, Integer sendId, String msgSource, Integer sceneryId, Integer commentId, Integer groupId) {
        this.msgPayload = msgPayload;
        this.msgType = msgType;
        this.sendTime = sendTime;
        this.sendId = sendId;
        this.msgSource = msgSource;
        this.sceneryId = sceneryId;
        this.commentId = commentId;
        this.groupId = groupId;
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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
