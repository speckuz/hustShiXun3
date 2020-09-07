package hust.sse.vini.userpart.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    //创建用户、查询用户、返回用户信息
    @PostMapping(path = "/user/create")
    public APIReturn createUser(@RequestBody User user){
        //先查询用户在不在
        if (null!=userRepository.getUserByUserName(user.getUserName())){
            return APIReturn.apiError(400, "Users already exists!");
        }
        //创建用户
        User savedUser = userRepository.save(user);
        return APIReturn.successfulResult(savedUser.getUserId());
    }

    @GetMapping(path = "user/exists")
    public APIReturn userExists(@RequestParam(name = "userName") String userName){
        //观察查询的结果是否为空
        if (null==userRepository.getUserByUserName(userName)){
            return APIReturn.successfulResult(false);
        }
        return APIReturn.successfulResult(true);
    }

    @GetMapping(path="/user/getUserByUserName")
    public APIReturn getUserIdByUserName(@RequestParam(name = "userName")String userName){
        User user = userRepository.getUserByUserName(userName);
        if(null==user){
            return APIReturn.apiError(404, "No such user!");
        }
        return APIReturn.successfulResult(user);
    }
    //修改用户信息
    @PostMapping(path = "/user/update")
    public APIReturn updateUser(@RequestBody User user){
        User oldUser = userRepository.getUserByUserName(user.getUserName());
        if(null==oldUser){
            return APIReturn.apiError(400, "Not allowed to update a non-existing user's info.");
        }
        if (null!=user.getPasswordHash()){
            oldUser.setPasswordHash(user.getPasswordHash());
        }
        if (null!=user.getGender()){
            oldUser.setGender(user.getGender());
        }
        if (null!=user.getThumbnail()){
            oldUser.setThumbnail(user.getThumbnail());
        }
        if (null!=user.getBirthday()){
            oldUser.setBirthday(user.getBirthday());
        }
        if (null!=user.getLocation()){
            oldUser.setLocation(user.getLocation());
        }
        if (!user.getInterests().isEmpty()){
            oldUser.setInterests(user.getInterests());
        }
        userRepository.save(oldUser);
        return APIReturn.successfulResult(oldUser);
    }

    //查询同一爱好的用户列表
    @GetMapping("/user/sameInterest")
    public APIReturn getUserNamesBySameInterest(@RequestParam(name = "interest")String interest){
        List<User> userList = userRepository.getUsersByInterests(interest);
        if(0==userList.size()){
            return APIReturn.apiError(400, "No users like this.");
        }
        return APIReturn.successfulResult(userList);
    }
}
