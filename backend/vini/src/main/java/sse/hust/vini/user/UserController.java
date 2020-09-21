package sse.hust.vini.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sse.hust.vini.APIReturn;
import sse.hust.vini.auth.TokenUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    //创建用户、查询用户、返回用户信息
    @PostMapping(path = "/user/create")
    public APIReturn createUser(@RequestBody User user){
        //先查询用户在不在
        if (null!=userRepository.findByUserName(user.getUserName())){
            return APIReturn.apiError(400, "Users already exists!");
        }
        //创建用户
        if(null==user.getNickname()){
            user.setNickname(user.getUserName());
        }
        User savedUser = userRepository.save(user);
        return APIReturn.successfulResult(savedUser.getUserId());
    }

    @GetMapping(path = "/user/exists")
    public APIReturn userExists(@RequestParam(name = "userName") String userName){
        //观察查询的结果是否为空
        if (null==userRepository.findByUserName(userName)){
            return APIReturn.successfulResult(false);
        }
        return APIReturn.successfulResult(true);
    }

    @GetMapping(path="/user/getUserByUserName")
    public APIReturn getUserIdByUserName(@RequestParam(name = "userName")String userName){
        User user = userRepository.findByUserName(userName);
        if(null==user){
            return APIReturn.apiError(404, "No such user!");
        }
        user.setPasswordHash(null);
        return APIReturn.successfulResult(user);
    }

    @PostMapping(path = "/login")
    public APIReturn userLogin(@RequestBody User user){
        User queryUser = userRepository.findByUserName(user.getUserName());
        if(null==queryUser){
            return APIReturn.apiError(401, "No such user!");
        }
        if(!queryUser.getPasswordHash().equals(user.getPasswordHash())){
            return APIReturn.apiError(404, "Wrong password");
        }
        Map<String, Object> userIdAndToken = new HashMap<>();
        String token = TokenUtils.generateToken(queryUser.getUserId());
        userIdAndToken.put("userId", queryUser.getUserId());
        userIdAndToken.put("token", token);

        return APIReturn.successfulResult(userIdAndToken);
    }

    //修改用户信息
    @PostMapping(path = "/user/update")
    public APIReturn updateUser(
            @RequestBody User user,
            @RequestHeader(name = "Vini-User-Id") Integer myId){
        User oldUser = userRepository.findByUserName(user.getUserName());
        if(null==oldUser){
            return APIReturn.apiError(400, "Not allowed to update a non-existing user's info.");
        }
        if(!myId.equals(oldUser.getUserId())){
            return APIReturn.apiError(400,"只能修改自己的用户信息！");
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
        if(null!=user.getSignature()){
            oldUser.setSignature(user.getSignature());
        }
        if(null!=user.getNickname()){
            oldUser.setNickname(user.getNickname());
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

    @GetMapping("/user/interestOptions")
    public APIReturn getInterestOptions(){
        return APIReturn.successfulResult(UserInterestOptions.getAllInterests());
    }

    @GetMapping("/user/searchByNickname")
    public APIReturn getSimilarUser(@RequestParam(name = "nickname") String nickname){
        return APIReturn.successfulResult(userRepository.findByNicknameLike(nickname));
    }

    @PostMapping("/user/batchGetUserName")
    public APIReturn batchGetUserName(@RequestBody List<Integer> userIdList){
        List<User> users=userRepository.findAllByUserIdIn(userIdList);
        users.forEach(x->x.setPasswordHash(null));
        return APIReturn.successfulResult(users);
    }
}
