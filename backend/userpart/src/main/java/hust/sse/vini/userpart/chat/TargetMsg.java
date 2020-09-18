package hust.sse.vini.userpart.chat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document
public class TargetMsg {
    @Id
    private String id;

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


    public TargetMsg(String msgPayload, String msgType, Date sendTime, Integer sendId, Integer receiveId) {
        this.msgPayload = msgPayload;
        this.msgType = msgType;
        this.sendTime = sendTime;
        this.sendId = sendId;
        this.receiveId = receiveId;
    }

    @Override
    public String toString() {
        return String.format("TargetMsg:[msgPayload='%s',msgType='%s',sendTime='%s',sendId='%d',receiveId='%d']", msgPayload, msgType, sendTime, sendId, receiveId);
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
}
