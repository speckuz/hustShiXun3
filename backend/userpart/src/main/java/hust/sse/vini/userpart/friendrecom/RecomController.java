package hust.sse.vini.userpart.friendrecom;

import hust.sse.vini.userpart.APIReturn;
import hust.sse.vini.userpart.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecomController {
    @GetMapping(path = "/friend/recommend")
    public APIReturn friendRecommend(@RequestHeader(name = "Vini-User-Id") Integer userId){
        if(MLRecommendator.mlRecommendator.classifier==null){
            return APIReturn.successfulResult(RandomRecommendator.randomRecommendator.randomRecommendate(userId));
        }
        List<User> userIds=MLRecommendator.mlRecommendator.mlRecommendate(userId);
        if(userIds==null||userIds.size()==0){
            return APIReturn.successfulResult(RandomRecommendator.randomRecommendator.randomRecommendate(userId));
        }
        return APIReturn.successfulResult(userIds);
    }
}
