package sse.hust.vini.communication;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SavedMsgRepo extends MongoRepository<SavedMsg, String> {
    SavedMsg getById(String id);
    void deleteById(String id);
}
