package hust.sse.vini.userpart.group;

import hust.sse.vini.userpart.APIReturn;
import hust.sse.vini.userpart.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class GroupController {
    @Autowired
    private BiRecordRepository biRepo;
    @Autowired
    private GroupRepository groupRepo;
    @Autowired
    private UserRepository userRepo;

    //创建群聊
    @PostMapping("/group/create")
    public APIReturn createGroup(@RequestHeader(name = "Vini-User-Id") Integer founderId, @RequestBody Group group) {
        //先查询是否存在群主id对应的用户
        if (null == userRepo.findByUserId(founderId)) {
            return APIReturn.apiError(400, "User Not Exists");
        }
        //先查询是否存在该群聊
        if (null != groupRepo.getByFounderIdAndViniGroupName(founderId, group.getViniGroupName())) {
            return APIReturn.apiError(400, "Group Exists!");
        }
        group.setFounderId(founderId);
        group.setMembers(new ArrayList<>());
        group.getMembers().add(founderId);
        Group savedGroup = groupRepo.save(group);
        BiRecord biRecord = new BiRecord(founderId, group.getId(), true);
        biRecord.setMemberAlias(userRepo.findByUserId(founderId).getNickname());
        biRecord.setGroupAlias(savedGroup.getViniGroupName());
        biRepo.save(biRecord);
        return APIReturn.successfulResult(savedGroup);
    }

    //查找想要添加的群聊
    @GetMapping("/group/search")
    public APIReturn searchGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId) {
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        Group tempGroup = groupRepo.getById(groupId);
        if (null == tempGroup) {
            return APIReturn.apiError(400, "No Such Group.");
        }
        List<Object> res = new ArrayList<>();
        String identity;
        res.add(tempGroup);
        if(null!=biRepo.getByGroupIdAndMemberId(groupId, userId)&&biRepo.getByGroupIdAndMemberId(groupId, userId).isConfirmed()){
            if(userId.equals(tempGroup.getFounderId())){
                identity="FOUNDER";
            }else{
                identity="MEMBER";
            }
        }else{
            identity="UNKNOWN";
        }
        res.add(identity);
        return APIReturn.successfulResult(res);
    }

    //加入群聊
    @GetMapping("/group/add")
    public APIReturn addGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                              @RequestParam(name = "groupId") Integer groupId) {
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        //群聊是否已经添加过
        if (null != biRepo.getByGroupIdAndConfirmedAndMemberId(groupId, true, userId)) {
            return APIReturn.apiError(400, "Already In The Group!");
        }
        if (null == biRepo.getByGroupIdAndMemberId(groupId, userId)) {
            BiRecord biRecord = new BiRecord(userId, groupId, false);
            biRepo.save(biRecord);
        }

        return APIReturn.successfulResult(null);
    }

    //同意加入
    @GetMapping("/group/permission")
    public APIReturn entryPerssion(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId,
                                   @RequestParam(name = "requestId") Integer requestId) {
        //用户是否存在
        if (null == userRepo.findByUserId(requestId) || null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        Group group = groupRepo.getById(groupId);
        if (!userId.equals(group.getFounderId())) {
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        BiRecord request = biRepo.getByGroupIdAndMemberId(groupId, requestId);
        if (request.isConfirmed()) {
            return APIReturn.apiError(400, "Already passed before!");
        }
        request.setConfirmed(true);
        request.setGroupAlias(group.getViniGroupName());
        request.setMemberAlias(userRepo.findByUserId(userId).getNickname());
        group.getMembers().add(requestId);
        biRepo.save(request);
        return APIReturn.successfulResult(null);
    }

    //拒绝加入
    @GetMapping("/group/refuse")
    public APIReturn entryRefuse(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId,
                                 @RequestParam(name = "requestId") Integer requestId) {
        //用户是否存在
        if (null == userRepo.findByUserId(requestId) || null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        if (!userId.equals(groupRepo.getById(groupId).getFounderId())) {
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        BiRecord request = biRepo.getByGroupIdAndMemberId(groupId, requestId);
        if (request.isConfirmed()) {
            return APIReturn.apiError(400, "Already passed before!");
        }
        biRepo.deleteByGroupIdAndMemberId(groupId, requestId);
        return APIReturn.successfulResult(null);
    }


    //删除群聊
    @GetMapping("/group/delete")
    public APIReturn deleteGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId) {
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        if (!userId.equals(groupRepo.getById(groupId).getFounderId())) {
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        groupRepo.getById(groupId).setMembers(new ArrayList<>());
        //删除所有相关人群关系
        biRepo.deleteAllByGroupId(groupId);
        //删除群聊
        groupRepo.deleteById(groupId);
        return APIReturn.successfulResult(null);
    }

    //退出群聊
    @GetMapping("/group/escape")
    public APIReturn escapeGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId) {
        Group group = groupRepo.getById(groupId);
        if (userId.equals(group.getFounderId())) {
            deleteGroup(userId, groupId);
        }
        if (null == biRepo.getByGroupIdAndMemberId(groupId, userId)) {
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
                                  @RequestParam(name = "groupId") Integer groupId) {
        Group group = groupRepo.getById(groupId);
        if (!userId.equals(group.getFounderId())) {
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        if (userId.equals(badGuyId)) {
            return APIReturn.apiError(400, "You can not kick yourself out.");
        }
        if (null == biRepo.getByGroupIdAndMemberId(groupId, badGuyId)) {
            return APIReturn.apiError(400, "Bad Guy Already Gone.");
        }
        biRepo.deleteByGroupIdAndMemberId(groupId, badGuyId);
        group.getMembers().remove(Integer.valueOf(badGuyId));
        return APIReturn.successfulResult(null);
    }

    //群主修改群名
    @GetMapping("/group/updateName")
    public APIReturn updateGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                 @RequestParam(name = "groupId") Integer groupId,
                                 @RequestParam(name = "groupNewName") String name) {
        Group group = groupRepo.getById(groupId);
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == group) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        if (!userId.equals(group.getFounderId())) {
            return APIReturn.apiError(400, "You Are Not Group Founder!");
        }
        group.setViniGroupName(name);
        return APIReturn.successfulResult(name);
    }

    //设置群昵称
    @GetMapping("/group/setAlias")
    public APIReturn setGroupAlias(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId,
                                   @RequestParam(name = "relationAlias") String alias) {
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == groupRepo.getById(groupId)) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        BiRecord biRecord= biRepo.getByGroupIdAndMemberId(groupId, userId);
        if(null==biRecord){
            return APIReturn.apiError(400, "unknown user to the group.");
        }
        biRecord.setGroupAlias(alias);
        return APIReturn.successfulResult(biRecord);
    }

    //查询群内备注
    @GetMapping("group/aliasList")
    public APIReturn displayAllAlias(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                     @RequestParam(name = "groupId") Integer groupId){
        Group group = groupRepo.getById(groupId);
        HashMap<Integer, String> aliasMap = new HashMap<>();
        //用户是否存在
        if (null == userRepo.findByUserId(userId)) {
            return APIReturn.apiError(400, "User Not Exists!");
        }
        //群聊是否存在
        if (null == group) {
            return APIReturn.apiError(400, "Group Not Exists!");
        }
        BiRecord biRecord= biRepo.getByGroupIdAndConfirmedAndMemberId(groupId, true,userId);
        if(null==biRecord){
            return APIReturn.apiError(400, "unknown user to the group.");
        }
        for(Integer memberId:group.getMembers()){
            aliasMap.put(memberId, biRepo.getByGroupIdAndMemberId(groupId,memberId).getMemberAlias());
        }
        return APIReturn.successfulResult(aliasMap);
    }

}
