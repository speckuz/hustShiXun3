package hust.sse.vini.userpart.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public APIReturn updateUser(@RequestParam(name = "userId")Integer userId,
                                @RequestParam(name = "userName", required = false)String userName,
                                @RequestParam(name = "passwordHash", required = false)String passwordHash,
                                @RequestParam(name = "gender", required = false)Boolean userGender,
                                @RequestParam(name = "thumbnail", required = false)byte[] userThumbnail,
                                @RequestParam(name = "birthday", required = false) Date userBirthday,
                                @RequestParam(name = "location", required = false)String userLocation,
                                @RequestParam(name = "interests", required = false) List<String> userInterests){
        User user = userRepository.getUserByUserId(userId);
        if(null==user){
            return APIReturn.apiError(400, "Not allowed to update a non-existing user's info.");
        }
        if(null!=userName){
            //可能想改的名字已经被使用了
            User potentialUser=userRepository.getUserByUserName(userName);
            if(null!=potentialUser&&!userId.equals(potentialUser.getUserId())){
                return APIReturn.apiError(400, "Name is already used, try another one");
            }
            user.setUserName(userName);
        }
        if(null!=passwordHash){
            user.setPasswordHash(passwordHash);
        }
        if(null!=userGender){
            user.setGender(userGender);
        }
        if(null!=userThumbnail){
            user.setThumbnail(userThumbnail);
        }
        if(null!=userBirthday){
            user.setBirthday(userBirthday);
        }
        if(null!=userLocation){
            user.setLocation(userLocation);
        }
        if(null!=userInterests){
            user.setInterests(userInterests);
        }
        userRepository.save(user);
        return APIReturn.successfulResult(user);
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
