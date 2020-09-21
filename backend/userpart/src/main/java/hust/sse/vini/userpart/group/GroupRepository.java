package hust.sse.vini.userpart.group;

import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Integer>{
    Group getByViniGroupName(String name);
    Group getById(Integer groupId);
    Group getByFounderIdAndViniGroupName(Integer founderId, String viniGroupName);
}
