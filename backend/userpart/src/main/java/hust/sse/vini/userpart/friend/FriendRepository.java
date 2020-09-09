package hust.sse.vini.userpart.friend;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends CrudRepository<FriendRelation,Integer> {
    FriendRelation findByUserUserIdAndFriendUserId(Integer userUserId,Integer friendUserId);
    List<FriendRelation> getAllByUserUserId(Integer userUserId);

    @Transactional
    @Modifying
    void deleteByUserUserIdAndFriendUserId(Integer userUserId,Integer friendUserId);
}
