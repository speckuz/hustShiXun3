package hust.sse.vini.userinfo;

import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo,Integer> {
    UserInfo getByUserName(String userName);
    UserInfo getByUserId(Integer userId);
}
