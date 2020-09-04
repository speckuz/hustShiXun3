package hust.sse.vini.userinterest;

import hust.sse.vini.APIReturn;
import hust.sse.vini.userinfo.UserInfo;
import hust.sse.vini.userinfo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserInterestController {
    @Autowired
    private UserInterestRepository userInterestRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    // 用户设置兴趣
    @PostMapping(path = "/interest/setInterests")
    // 添加兴趣json中的多条兴趣数据
    public APIReturn addInterests(@RequestBody UserInterests userInterests){
        UserInfo userInfo=userInfoRepository.getByUserId(userInterests.getUserId());
        // 想要为之添加兴趣的用户尚未注册
        if(null==userInfo){
            return APIReturn.apiError(404, "User not registered.");
        }
        UserInterest userInterest;
        // 删除原有兴趣记录
        userInterestRepository.deleteAllByUserId(userInfo.getUserId());
        for(String interest:userInterests.getInterests()){
            // 按兴趣个数分条目一条条插入到数据库表中
            userInterest = new UserInterest();
            userInterest.setSingleInterest(interest);
            userInterest.setUserId(userInfo.getUserId());
            userInterestRepository.save(userInterest);
        }
        return APIReturn.successfulResult(null);
    }

    // 查询兴趣
    @GetMapping(path = "interest/getAllByUserId")
    public APIReturn queryUserInterests(@RequestParam(name = "userId")Integer userId){
        // queryInterests表示查询后返回的UserInterests的List, interests则是提取出queryInterests中的兴趣String作为List集合
        List<UserInterest> queryInterests = userInterestRepository.getAllInterestsByUserId(userId);
        List<String> interests=new ArrayList<>();
        if(null==queryInterests){
            return APIReturn.apiError(404, "User not existed.");
        }
        for(UserInterest userInterest:queryInterests){
            interests.add(userInterest.getSingleInterest());
        }

        return APIReturn.successfulResult(interests);
    }

}
