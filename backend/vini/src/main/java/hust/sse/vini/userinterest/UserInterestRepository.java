package hust.sse.vini.userinterest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInterestRepository extends CrudRepository<UserInterest, Integer> {
    List<UserInterest> getAllInterestsByUserId(Integer userId);
    @Transactional
    void deleteAllByUserId(Integer userId);

}
