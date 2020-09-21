package hust.sse.vini.userpart.scenery;

import hust.sse.vini.userpart.APIReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class SceneryController {
    @Resource
    SceneryMongoService sceneryMongoService;

    @PostMapping(path="/scenery/post")
    public APIReturn postScenery(@RequestBody SceneryPost sceneryPost,
                                 @RequestHeader("Vini-User-Id") Integer userId){
        if(sceneryPost.getPictures().isEmpty()&&sceneryPost.getText().isBlank()){
            return APIReturn.apiError(400,"请勿发送空白动态！");
        }
        return APIReturn.successfulResult(sceneryMongoService.postScenery(sceneryPost,userId));
    }

    @GetMapping(path = "/scenery/delete")
    public APIReturn deleteScenery(@RequestParam(name = "sceneryId") String sceneryId,
                                   @RequestHeader("Vini-User-Id") Integer userId){
        return APIReturn.successfulResult(sceneryMongoService.deleteScenery(sceneryId,userId));
    }

    @PostMapping(path="/scenery/comment")
    public APIReturn postComment(@RequestParam(name = "sceneryId")String sceneryId,
                                 @RequestBody SceneryComment comment,
                                 @RequestHeader("Vini-User-Id") Integer userId){
        if(comment.getCommentText().isBlank()){
            return APIReturn.apiError(400,"请勿发送空白评论！");
        }
        SceneryComment resComment=sceneryMongoService.comment(sceneryId,comment,userId);
        if(null==resComment){
            return APIReturn.apiError(404,"评论的楼层不存在！");
        }
        return APIReturn.successfulResult(resComment);
    }

    @GetMapping(path = "/scenery/get")
    public APIReturn getScenery(@RequestParam(name="page") int page){
        return  APIReturn.successfulResult(sceneryMongoService.getPublicSceneries(page));
    }

    @GetMapping(path="/scenery/getFriendScenery")
    public APIReturn getFriendScenery(@RequestParam(name="page") int page,
                                      @RequestHeader("Vini-User-Id") Integer userId){
        return APIReturn.successfulResult(sceneryMongoService.getFriendSceneries(page, userId));
    }

    @GetMapping(path = "/scenery/thumbUp")
    public APIReturn thumbUp(@RequestParam(name="sceneryId") String sceneryId,
                             @RequestHeader("Vini-User-Id") Integer userId){
        boolean res= sceneryMongoService.thumbUp(sceneryId, userId);
        if(res){
            return APIReturn.successfulResult(null);
        }
        else{
            return APIReturn.apiError(404,"Scenery动态不存在！");
        }
    }

    @GetMapping(path = "/scenery/deThumbUp")
    public APIReturn deThumbUp(@RequestParam(name="sceneryId") String sceneryId,
                               @RequestHeader("Vini-User-Id") Integer userId){
        sceneryMongoService.deThumbUp(sceneryId, userId);
        return APIReturn.successfulResult(null);
    }

    @GetMapping(path = "/scenery/deleteComment")
    public APIReturn deleteComment(@RequestParam(name="sceneryId") String sceneryId,
                                   @RequestParam(name="commentIndex") Integer commentIndex,
                                   @RequestHeader("Vini-User-Id") Integer userId){
        Long res=sceneryMongoService.deleteComment(sceneryId,userId,commentIndex);
        if(res.equals(-1l)){
            return APIReturn.apiError(404,"该动态不存在!");
        }else {
            if (res.equals(-2l)) {
                return APIReturn.apiError(400,"无法删除别人的评论！");
            }
            else{
                return APIReturn.successfulResult(res);
            }
        }
    }
}
