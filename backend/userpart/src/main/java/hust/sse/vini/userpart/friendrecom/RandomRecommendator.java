package hust.sse.vini.userpart.friendrecom;

import hust.sse.vini.userpart.friend.FriendRelation;
import hust.sse.vini.userpart.friend.FriendRepository;
import hust.sse.vini.userpart.user.User;
import hust.sse.vini.userpart.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class RandomRecommendator {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    public static RandomRecommendator randomRecommendator;

    @PostConstruct
    public void init(){
        randomRecommendator=this;
        randomRecommendator.friendRepository=this.friendRepository;
        randomRecommendator.userRepository=this.userRepository;
    }

    public List<User> randomRecommendate(Integer userId){
        return randomRecommendate(userId,3);
    }

    public List<User> randomRecommendate(Integer userId,int recommendNum){
        ArrayList<Integer> result=new ArrayList<>();
        ArrayList<Integer> alreadyFriend=new ArrayList<>();
        List<FriendRelation> fr=friendRepository.findAllByUserUserId(userId);
        for(FriendRelation friendRelation:fr){
            alreadyFriend.add(friendRelation.getFriendUserId());
        }
        User maxUser=userRepository.findTopByOrderByUserIdDesc();
        Integer maxUserId=maxUser.getUserId();
        if(maxUserId<2*recommendNum+ alreadyFriend.size()){
            IntStream.rangeClosed(1,maxUserId)
                    .filter(
                            (int x)->(alreadyFriend.indexOf(x)==-1)
                    ).forEach(x->{
                        result.add(x);
                        return;
            });
            if(result.size()<=recommendNum){
                return userRepository.getUserByUserIdIn(result);
            }else{
                Random rnd=new Random();
                while (result.size()>recommendNum){
                    result.remove(rnd.nextInt(result.size()));
                    return userRepository.getUserByUserIdIn(result);
                }
            }
        }
        Random rnd=new Random();
        HashSet<Integer> resSet=new HashSet<>();
        while (resSet.size()<recommendNum){
            int nextUserId=rnd.nextInt(maxUserId+1);
            if (alreadyFriend.indexOf(nextUserId)==-1){
                resSet.add(nextUserId);
            }
        }
        result.addAll(resSet);
        return userRepository.getUserByUserIdIn(result);
    }

}
