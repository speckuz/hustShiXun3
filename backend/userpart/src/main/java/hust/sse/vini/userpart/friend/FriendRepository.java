package hust.sse.vini.userpart.friend;

import org.hibernate.annotations.SortComparator;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface FriendRepository extends CrudRepository<FriendRelation,Integer> {
    FriendRelation findByUserUserIdAndFriendUserId(Integer userUserId,Integer friendUserId);
    List<FriendRelation> findAllByUserUserId(Integer userUserId);
    FriendRelation findTopByOrderByFriendRelationIdDesc();
    List<FriendRelation> findAllByFriendRelationIdIn(Collection<Integer> friendRelationId);

    @Transactional
    @Modifying
    void deleteByUserUserIdAndFriendUserId(Integer userUserId,Integer friendUserId);
}
