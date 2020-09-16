package hust.sse.vini.userpart.chat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class TargetMsg {
    @Id
    private String id;

    private Integer msgId;
    private String msgPayload;

    public TargetMsg(Integer msgId, String msgPayload) {
        this.msgId=msgId;
        this.msgPayload = msgPayload;
    }

    @Override
    public String toString() {
        return String.format("TargetMsg:[msgId='%d', msgPayload='%s']", msgId, msgPayload);
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
}
