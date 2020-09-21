package sse.hust.vini.communication;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingMsgRepo extends MongoRepository<PendingMsg, Integer> {
    List<PendingMsg> getPendingMsgsByTargetId(Integer targetId);
    void deletePendingMsgsByTargetId(Integer targetId);
}
