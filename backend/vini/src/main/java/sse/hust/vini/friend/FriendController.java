package sse.hust.vini.friend;

import com.alibaba.fastjson.JSON;
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
import java.util.List;

@RestController
public class FriendController {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SavedMsgRepo savedMsgRepo;
    @Autowired
    private PendingMsgRepo pendingMsgRepo;

    @GetMapping(path = "/friend/getAll")
    public APIReturn findMyFriend(@RequestHeader(name = "Vini-User-Id") Integer userId){
        List<PersonalFriend> friends=new ArrayList<PersonalFriend>();
        List<FriendRelation> friendRelations=friendRepository.findAllByUserUserId(userId);
        for (FriendRelation relation:friendRelations) {
            if(!relation.getPendingConfirm()){
                friends.add(new PersonalFriend(
                        relation.getFriendUserId(),
                        relation.getFriendAlias(),
                        relation.getFriendGroupName()));
            }
        }
        return APIReturn.successfulResult(friends);
    }

    @PostMapping(path = "/friend/add")
    public APIReturn addFriend(
            @RequestHeader(name = "Vini-User-Id") Integer myId,
            @RequestBody PersonalFriend personalFriend) throws IOException {
        if(myId.equals(personalFriend.getUserId())){
            return APIReturn.apiError(400,"不能添加自己为好友！");
        }
        if(null==userRepository.findByUserId(personalFriend.getUserId())){
            return APIReturn.apiError(404,"没有该用户！");
        }
        if(null!=friendRepository.findByUserUserIdAndFriendUserId(myId, personalFriend.getUserId())){
            return APIReturn.apiError(400,"好友已经存在！");
        }
        FriendRelation relation=new FriendRelation();
        relation.setUserUserId(myId);
        relation.setFriendUserId(personalFriend.getUserId());
        relation.setFriendAlias(personalFriend.getAlias());
        relation.setFriendGroupName(personalFriend.getGroupName());
        relation.setPendingConfirm(true);
        friendRepository.save(relation);
        if(SessionMap.contains(personalFriend.getUserId())){
            PushMsgJson msgJson=new PushMsgJson(myId.toString(),0,new Date(),
                    myId,3,null,null,null);
            SessionMap.querySession(personalFriend.getUserId()).sendMessage(new TextMessage(JSON.toJSONStringWithDateFormat(msgJson,"yyyy-MM-dd HH:mm:ss")));
        }else{
            SavedMsg msg=new SavedMsg(myId.toString(),0,new Date(),myId,personalFriend.getUserId(),
                    3,null,null,null);
            String msgId=savedMsgRepo.save(msg).getId();
            pendingMsgRepo.save(new PendingMsg(personalFriend.getUserId(),msgId));
        }
        return APIReturn.successfulResult(personalFriend);
    }

    @PostMapping(path = "/friend/confirm")
    public APIReturn confirmFriend(
            @RequestHeader(name = "Vini-User-Id") Integer myId,
            @RequestBody PersonalFriend friend
    ){
        FriendRelation friendRequest=friendRepository.findByUserUserIdAndFriendUserId(friend.getUserId(),myId);
        if(null==friendRequest){
            return APIReturn.apiError(400,"未找到好友申请，无法通过好友请求！");
        }
        friendRequest.setPendingConfirm(false);
        friendRepository.save(friendRequest);
        if(null==friendRepository.findByUserUserIdAndFriendUserId(myId, friend.getUserId())){
            FriendRelation friendRelation=new FriendRelation();
            friendRelation.setUserUserId(myId);
            friendRelation.setFriendUserId(friend.getUserId());
            friendRelation.setFriendAlias(friend.getAlias());
            friendRelation.setFriendGroupName(friend.getGroupName());
            friendRepository.save(friendRelation);
        }
        return APIReturn.successfulResult(null);
    }

    @GetMapping(path = "/friend/refuse")
    public APIReturn refuseFriend(
            @RequestHeader(name = "Vini-User-Id") Integer myId,
            @RequestParam(name = "friendUserId") Integer friendUserId
    ){
        friendRepository.deleteByUserUserIdAndFriendUserId(friendUserId,myId);
        return APIReturn.successfulResult(null);
    }

    @PostMapping(path = "/friend/change")
    public APIReturn changeAlias(
            @RequestHeader(name = "Vini-User-Id") Integer myId,
            @RequestBody PersonalFriend personalFriend
    ){
        FriendRelation friendRelation= friendRepository.findByUserUserIdAndFriendUserId(myId,personalFriend.getUserId());
        if(null==friendRelation){
            return APIReturn.apiError(404,"未找到该名好友！");
        }
        if(null!=personalFriend.getAlias()){
            friendRelation.setFriendAlias(personalFriend.getAlias());
        }
        if(null!=personalFriend.getGroupName()){
            friendRelation.setFriendGroupName(personalFriend.getGroupName());
        }
        friendRepository.save(friendRelation);
        return APIReturn.successfulResult(personalFriend);
    }

    @GetMapping(path = "/friend/delete")
    public APIReturn deleteFriend(
            @RequestHeader(value = "Vini-User-Id") Integer myId,
            @RequestParam(value = "friendUserId") Integer friendUserId
    ){
        friendRepository.deleteByUserUserIdAndFriendUserId(myId, friendUserId);
        friendRepository.deleteByUserUserIdAndFriendUserId(friendUserId,myId);
        return APIReturn.successfulResult(friendUserId);
    }
}
