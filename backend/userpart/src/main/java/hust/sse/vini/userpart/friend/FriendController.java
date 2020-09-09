package hust.sse.vini.userpart.friend;

import hust.sse.vini.userpart.user.APIReturn;
import hust.sse.vini.userpart.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendController {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/friend/getAll")
    public APIReturn findMyFriend(@RequestHeader(name = "Vini-User-Id") Integer userId){
        List<PersonalFriend> friends=new ArrayList<PersonalFriend>();
        List<FriendRelation> friendRelations=friendRepository.getAllByUserUserId(userId);
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
            @RequestBody PersonalFriend personalFriend){
        if(null==userRepository.getUserByUserId(personalFriend.getUserId())){
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
