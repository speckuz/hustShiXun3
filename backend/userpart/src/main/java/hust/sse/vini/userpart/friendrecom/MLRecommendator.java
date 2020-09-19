package hust.sse.vini.userpart.friendrecom;

import hust.sse.vini.userpart.friend.FriendRelation;
import hust.sse.vini.userpart.friend.FriendRepository;
import hust.sse.vini.userpart.user.User;
import hust.sse.vini.userpart.user.UserRepository;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KDtreeKNN;
import net.sf.javaml.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MLRecommendator {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    public static MLRecommendator mlRecommendator;
    Classifier classifier;


    @PostConstruct
    public void init(){
        mlRecommendator=this;
        mlRecommendator.friendRepository=this.friendRepository;
        mlRecommendator.userRepository=this.userRepository;
        this.trainModel();
        mlRecommendator.classifier=this.classifier;
    }

    public void trainModel(){
        FriendRelation maxfr=friendRepository.findTopByOrderByFriendRelationIdDesc();
        if(null==maxfr){
            this.classifier=null;
            return;
        }
        Integer maxFrId=maxfr.getFriendRelationId();
        User maxUser= userRepository.findTopByOrderByUserIdDesc();
        Integer maxUserId=maxUser.getUserId();
        if(maxFrId<=20&&maxUserId<=20){
            this.classifier=null;
            return;
        }
        Random rnd=new Random();
        Dataset ds=new DefaultDataset();
        ArrayList<Integer> frIds=new ArrayList<>();
        for(int i=0;i<10;i++){
            frIds.add(rnd.nextInt(maxFrId+1));
        }
        List<FriendRelation> friendRelations=friendRepository.findAllByFriendRelationIdIn(frIds);
        ArrayList<Integer> userIds=new ArrayList<>();
        for(FriendRelation fr:friendRelations){
            userIds.add(fr.getFriendUserId());
            userIds.add(fr.getUserUserId());
        }
        List<User> users=userRepository.getUserByUserIdIn(userIds);
        HashMap<Integer,User> userMap=new HashMap<>();
        for(User user:users){
            userMap.put(user.getUserId(),user);
        }
        for(FriendRelation fr:friendRelations){
            double[] attr1=DataProcessing.interestsToOneHot(userMap.get(fr.getUserUserId()).getInterests());
            double[] attr2=DataProcessing.interestsToOneHot(userMap.get(fr.getFriendUserId()).getInterests());
            DenseInstance ins=new DenseInstance(DataProcessing.attributeMinus(attr1,attr2),1);
            ds.add(ins);
        }
        ArrayList<Instance> unlikes=new ArrayList<>();
        while(unlikes.size()<10){
            User user1=users.get(rnd.nextInt(users.size()));
            User user2=users.get(rnd.nextInt(users.size()));
            if(user1.getUserId().equals(user2.getUserId())){
                continue;
            }
            if(null!=friendRepository.findByUserUserIdAndFriendUserId(
                    user1.getUserId(), user2.getUserId()
            )){
                continue;
            }
            double[] attr1=DataProcessing.interestsToOneHot(user1.getInterests());
            double[] attr2=DataProcessing.interestsToOneHot(user2.getInterests());
            DenseInstance ins=new DenseInstance(DataProcessing.attributeMinus(attr1,attr2),0);
            unlikes.add(ins);
        }
        ds.addAll(unlikes);
        this.classifier=new KDtreeKNN(3);
        this.classifier.buildClassifier(ds);
    }

    public List<User> mlRecommendate(Integer userId){
        return mlRecommendate(userId,3);
    }

    public List<User> mlRecommendate(Integer userId,int recommendateNum){
        ArrayList<User> result=new ArrayList<>();
        int i=5*recommendateNum;
        int maxUserId=userRepository.findTopByOrderByUserIdDesc().getUserId();
        User user= userRepository.findByUserId(userId);
        double[] attr1=DataProcessing.interestsToOneHot(user.getInterests());
        Random rnd=new Random();
        while(i>0&&result.size()<recommendateNum){
            int friendUserId= rnd.nextInt(maxUserId+1);
            if(friendRepository.findByUserUserIdAndFriendUserId(userId,friendUserId)!=null){
                continue;
            }
            User friend=userRepository.findByUserId(friendUserId);
            if(friend==null){
                continue;
            }
            double[] attr2=DataProcessing.interestsToOneHot(friend.getInterests());
            DenseInstance ins=new DenseInstance(DataProcessing.attributeMinus(attr1,attr2));
            Object classifyRes=classifier.classify(ins);
            if(classifyRes!=null && classifyRes.equals(1)){
                result.add(friend);
            }
        }
        return result;
    }
}
