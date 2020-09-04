package hust.sse.vini.userinfo;

import hust.sse.vini.APIReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepo;

    @PostMapping(path="/user/create")
    public APIReturn createUser(@RequestBody UserInfo newUserInfo){
        if(userInfoRepo.getByUserName(newUserInfo.getUserName())!=null){
            return APIReturn.apiError(400,"User already exists!");
        }
        UserInfo savedInfo = userInfoRepo.save(newUserInfo);
        return APIReturn.successfulResult(savedInfo.getUserId());
    }

    @GetMapping(path = "/user/exists")
    public APIReturn userExists(@RequestParam(name="userName") String userName){
        UserInfo userId=userInfoRepo.getByUserName(userName);
        if(userId==null){
            return APIReturn.successfulResult(false);
        }else{
            return APIReturn.successfulResult(true);
        }
    }

    @GetMapping(path="/user/getUserByUserName")
    public APIReturn getUserByUserName(@RequestParam(name = "userName") String userName){
        UserInfo res=userInfoRepo.getByUserName(userName);
        if(res==null){
            return APIReturn.apiError(404,"No such user.");
        }else{
            return APIReturn.successfulResult(res);
        }
    }
}
