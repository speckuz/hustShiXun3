package sse.hust.vini.group;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import sse.hust.vini.APIReturn;
import sse.hust.vini.communication.*;
import sse.hust.vini.user.UserRepository;
import sse.hust.vini.websocket.SessionMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private SavedMsgRepo savedMsgRepo;
    @Autowired
    private PendingMsgRepo pendingMsgRepo;

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
        JSONObject res = JSONObject.parseObject(tempGroup.toString());
        String identity;
        if(null!=biRepo.getByGroupIdAndMemberId(groupId, userId)&&biRepo.getByGroupIdAndMemberId(groupId, userId).isConfirmed()){
            if(userId.equals(tempGroup.getFounderId())){
                identity="FOUNDER";
            }else{
                identity="MEMBER";
            }
        }else{
            identity="UNKNOWN";
        }
        res.put("identity", identity);
        return APIReturn.successfulResult(res);
    }

    //加入群聊
    @GetMapping("/group/add")
    public APIReturn addGroup(@RequestHeader(name = "Vini-User-Id") Integer userId,
                              @RequestParam(name = "groupId") Integer groupId) throws IOException {
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
            Group group=groupRepo.getById(groupId);
            Integer founderId=group.getFounderId();
            if(SessionMap.contains(founderId)){
                PushMsgJson msgJson=new PushMsgJson(userId.toString(),0,new Date(),
                        userId,4,null,null,groupId);
                SessionMap.querySession(founderId).sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(msgJson,"yyyy-MM-dd HH:mm:ss")));
            }else{
                SavedMsg msg=new SavedMsg(userId.toString(),0,new Date(),userId,founderId,
                        4,null,null,groupId);
                String msgId=savedMsgRepo.save(msg).getId();
                pendingMsgRepo.save(new PendingMsg(founderId,msgId));
            }
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
        request.setMemberAlias(userRepo.findByUserId(requestId).getNickname());
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
        groupRepo.save(group);
        return APIReturn.successfulResult(name);
    }

    //设置群昵称
    @GetMapping("/group/setAlias")
    public APIReturn setGroupAlias(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId,
                                   @RequestParam(name = "groupAlias", required = false) String groupAlias,
                                   @RequestParam(name = "memberAlias", required = false) String memberAlias) {
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
        if(null!=groupAlias){
            biRecord.setGroupAlias(groupAlias);
        }
        if(null!=memberAlias){
            biRecord.setMemberAlias(memberAlias);
        }
        biRepo.save(biRecord);
        return APIReturn.successfulResult(biRecord);
    }

    //查询群内备注
    @GetMapping("/group/memberAlias")
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

    @GetMapping("/group/groupAlias")
    public APIReturn getGroupAlias(@RequestHeader(name = "Vini-User-Id") Integer userId,
                                   @RequestParam(name = "groupId") Integer groupId) {
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
        String groupAlias = biRepo.getByGroupIdAndMemberId(groupId,groupId).getGroupAlias();
        return APIReturn.successfulResult(groupAlias);
    }

    @GetMapping("/group/userIn")
    public APIReturn getOnesGroup(@RequestHeader(name="Vini-User-Id") Integer userId){
        List<BiRecord> groups=biRepo.getAllByMemberIdAndConfirmed(userId,true);
        ArrayList<Group> resGroup=new ArrayList<>();
        groups.forEach(birec->{
            groupRepo.findById(birec.getGroupId()).ifPresent(group->{
                if(null!=birec.getGroupAlias()){
                    group.setViniGroupName(birec.getGroupAlias());
                }
                group.setMembers(null);
                group.setGroupThumbNail(null);
                resGroup.add(group);
            });
        });
        return APIReturn.successfulResult(resGroup);
    }

    @PostMapping("/group/batchQuery")
    public APIReturn batchQueryGroup(@RequestBody List<Integer> groupIds){
        ArrayList<Group> groups=new ArrayList<>();
        groupIds.forEach(gId->groupRepo.findById(gId).ifPresent(groups::add));
        return APIReturn.successfulResult(groups);
    }
}
