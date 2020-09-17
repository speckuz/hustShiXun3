package hust.sse.vini.userpart.group;

import hust.sse.vini.userpart.APIReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class GroupController {
    @Autowired
    private BiRecordRepository biRepo;
    @Autowired
    private GroupRepository groupRepo;

    //创建群聊
    @PostMapping("/group/create")
    public APIReturn createGroup(@RequestHeader(name = "Vini-User-Id") Integer founderId,@RequestBody Group group){
        //先查询是否存在该群聊
        if(null!=groupRepo.getByViniGroupName(group.getViniGroupName())){
            return APIReturn.apiError(400, "Group Exists!");
        }
        group.setFounderId(founderId);
        Group savedGroup = groupRepo.save(group);
        biRepo.save(new BiRecord(founderId, group.getId(), true));
        return APIReturn.successfulResult(savedGroup);
    }

    //查找想要添加的群聊
    @GetMapping("/group/search")
    public APIReturn searchGroup(@RequestParam(name = "groupId") Integer groupId){
        Group tempGroup=groupRepo.getById(groupId);
        if(null==tempGroup){
            return APIReturn.apiError(400, "No Such Group.");
        }
        return APIReturn.successfulResult(tempGroup);
    }

    //加入群聊
    @GetMapping("/group/add")
    public APIReturn addGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                              @RequestParam(name = "groupId") Integer groupId){
        //群聊是否存在
        if(null==groupRepo.getById(groupId)){
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        //群聊是否已经添加过
        if(null!=biRepo.getByGroupIdAndConfirmedAndMemberId(groupId, true, userId)){
            return APIReturn.apiError(400, "Already In The Group!");
        }
        BiRecord biRecord = new BiRecord(userId, groupId, false);
        biRepo.save(biRecord);
        return APIReturn.successfulResult(null);
    }

    //同意加入
    @GetMapping("/group/permission")
    public APIReturn entryPerssion(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId,
                                   @RequestParam(name = "requestId") Integer requestId){
        Group group = groupRepo.getById(groupId);
        if(!userId.equals(group.getFounderId())){
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        BiRecord request = biRepo.getByGroupIdAndMemberId(groupId, requestId);
        if(request.isConfirmed()){
            return APIReturn.apiError(400, "Already passed before!");
        }
        request.setConfirmed(true);
        group.getMembers().add(requestId);
        biRepo.save(request);
        return APIReturn.successfulResult(null);
    }

    //拒绝加入
    @GetMapping("/group/refuse")
    public APIReturn entryRefuse(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId,
                                   @RequestParam(name = "requestId") Integer requestId) {
        if(!userId.equals(groupRepo.getById(groupId).getFounderId())){
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        BiRecord request = biRepo.getByGroupIdAndMemberId(groupId, requestId);
        if(request.isConfirmed()){
            return APIReturn.apiError(400, "Already passed before!");
        }
        biRepo.deleteByGroupIdAndMemberId(groupId, requestId);
        return APIReturn.successfulResult(null);
    }


    //删除群聊
    @GetMapping("/group/delete")
    public APIReturn deleteGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId){
        if(!userId.equals(groupRepo.getById(groupId).getFounderId())){
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        if(null==groupRepo.getById(groupId)){
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        //删除所有相关人群关系
        biRepo.deleteAllByGroupId(groupId);
        //删除群聊
        groupRepo.deleteById(groupId);
        return APIReturn.successfulResult(null);
    }

    //退出群聊
    @GetMapping("/group/escape")
    public APIReturn escapeGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId){
        Group group = groupRepo.getById(groupId);
        if(userId.equals(group.getFounderId())){
            deleteGroup(userId, groupId);
        }
        if(null==biRepo.getByGroupIdAndMemberId(groupId, userId)){
            return APIReturn.apiError(400, "Not In The Group(Escape for a der)");
        }
        group.getMembers().remove(userId);
        biRepo.deleteByGroupIdAndMemberId(groupId, userId);
        return APIReturn.successfulResult(null);
    }

    //踢人出群
    @GetMapping("/group/remove")
    public APIReturn removeBadGuy(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                  @RequestParam(name = "badGuyId") Integer badGuyId,
                                 @RequestParam(name = "groupId") Integer groupId){
        Group group = groupRepo.getById(groupId);
        if(userId.equals(group.getFounderId())){
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        if(userId.equals(badGuyId)){
            return APIReturn.apiError(400, "Kick your *** instead of escaping from your own group.");
        }
        if(null==biRepo.getByGroupIdAndMemberId(groupId, badGuyId)){
            return APIReturn.apiError(400, "Bad Guy Already Gone.");
        }
        biRepo.deleteByGroupIdAndMemberId(groupId, badGuyId);
        group.getMembers().remove(userId);
        return APIReturn.successfulResult(null);
    }

}
