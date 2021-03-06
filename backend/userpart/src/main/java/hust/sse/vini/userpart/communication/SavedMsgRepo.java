package hust.sse.vini.userpart.communication;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SavedMsgRepo extends MongoRepository<SavedMsg, Integer> {
    SavedMsg getByMsgId(Integer msgId);
    void deleteByMsgId(Integer msgId);
}
