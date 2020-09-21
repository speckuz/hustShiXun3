package hust.sse.vini.userpart.communication;

public class ReceiveMsgJson {
    //消息主体
    private String msgPayload;
    //消息类型
    private String msgType;
    //消息接收者
    private Integer receiveId;
    //消息来源
    private String msgSource;

    public ReceiveMsgJson(String msgPayload, String msgType, Integer receiveId, String msgSource) {
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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


}
