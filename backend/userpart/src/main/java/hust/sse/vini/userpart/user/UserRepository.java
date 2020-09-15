package hust.sse.vini.userpart.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends CrudRepository <User, Integer> {
    //方法按需设置
    User findByUserName(String userName);
    List<User> getUsersByInterests(String userInterest);
    User findByUserId(Integer userId);
    List<User> getUserByUserIdIn(Collection<Integer> userIds);
    User findTopByOrderByUserIdDesc();
}
