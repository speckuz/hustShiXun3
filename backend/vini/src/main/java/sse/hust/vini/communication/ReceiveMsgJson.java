package sse.hust.vini.communication;

public class ReceiveMsgJson {
    //消息主体
    private String msgPayload;
    //消息类型
    private Integer msgType;
    //消息接收者
    private Integer receiveId;
    //消息来源
    private Integer msgSource;

    public ReceiveMsgJson(String msgPayload, Integer msgType, Integer receiveId, Integer msgSource) {
        this.msgPayload = msgPayload;
        this.msgType = msgType;
        this.receiveId = receiveId;
        this.msgSource = msgSource;
    }

    public String getMsgPayload() {
        return msgPayload;
    }

    public void setMsgPayload(String msgPayload) {
        this.msgPayload = msgPayload;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(Integer msgSource) {
        this.msgSource = msgSource;
    }


}
