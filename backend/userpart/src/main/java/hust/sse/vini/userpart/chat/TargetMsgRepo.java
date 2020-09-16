package hust.sse.vini.userpart.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TargetMsgRepo extends MongoRepository<TargetMsg, Integer> {
    TargetMsg getByMsgId(Integer msgId);
    void deleteByMsgId(Integer msgId);
}
