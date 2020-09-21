package hust.sse.vini.userpart.communication;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
public class PendingMsg {
    @Id
    private String id;

    private Integer targetId;
    private Integer msgIndex;

    @Override
    public String toString() {
        return String.format("TargetIndexes:[targetId='%d', msgIndex:'%d']", targetId, msgIndex);
    }

    public PendingMsg(Integer targetId, Integer msgIndex) {
        this.targetId = targetId;
        this.msgIndex = msgIndex;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(Integer msgIndex) {
        this.msgIndex = msgIndex;
    }
}
