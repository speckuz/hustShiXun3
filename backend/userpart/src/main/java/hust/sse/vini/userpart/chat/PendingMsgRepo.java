package hust.sse.vini.userpart.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingMsgRepo extends MongoRepository<PendingMsg, Integer> {
    List<PendingMsg> getPendingMsgsByTargetId(Integer targetId);
    void deletePendingMsgsByTargetId(Integer targetId);
}
