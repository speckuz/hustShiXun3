package sse.hust.vini.communication;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class PendingMsg {
    @Id
    private String id;

    private Integer targetId;
    private String msgIndex;

    @Override
    public String toString() {
        return String.format("TargetIndexes:[targetId='%d', msgIndex:'%d']", targetId, msgIndex);
    }

    public PendingMsg(Integer targetId, String msgIndex) {
        this.targetId = targetId;
        this.msgIndex = msgIndex;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(String msgIndex) {
        this.msgIndex = msgIndex;
    }
}
