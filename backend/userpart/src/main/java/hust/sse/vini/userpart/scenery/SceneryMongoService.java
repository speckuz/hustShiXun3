package hust.sse.vini.userpart.scenery;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import hust.sse.vini.userpart.communication.PushMsgJson;
import hust.sse.vini.userpart.friend.FriendRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.stream.events.Comment;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SceneryMongoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private FriendRepository friendRepository;

    public SceneryPost postScenery(SceneryPost post,Integer userId){
        post.setComments(new ArrayList<>());
        post.setThumbUps(new ArrayList<>());
        post.setPostTime(new Date());
        post.setUserId(userId);
        return mongoTemplate.insert(post);
    }

    public long deleteScenery(String sceneryId,Integer userId){
        Query deleteQuery=new Query(Criteria.where("_id").is(sceneryId).
                                        and("userId").is(userId));
        DeleteResult res=mongoTemplate.remove(deleteQuery,SceneryPost.class);
        return res.getDeletedCount();
    }

    public SceneryComment comment(String sceneryId,SceneryComment comment,Integer userId){
        Criteria queryCriteria=Criteria.where("_id").is(sceneryId);
        if(!comment.getCommentToIndex().equals(0)){
            queryCriteria=queryCriteria.and("comments").elemMatch(
                    Criteria.where("commentIndex").is(comment.getCommentToIndex()));
        }
        Query sceneryQuery=new Query(queryCriteria);
        sceneryQuery.fields().include("nextCommentIndex").include("");
        SceneryPost post=mongoTemplate.findOne(sceneryQuery,SceneryPost.class);
        if(post==null){
            return null;
        }
        comment.setCommentIndex(post.getNextCommentIndex());
        comment.setCommentUserId(userId);
        mongoTemplate.updateFirst(sceneryQuery,(new Update()).push("comments",comment).inc("nextCommentIndex",1),SceneryPost.class);
        PushMsgJson msgJson;
        return comment;
    }

    public List<SceneryPost> getPublicSceneries(int pageNum){
        Query sceneryQuery=new Query();
        sceneryQuery=sceneryQuery.skip(10*(pageNum-1)).limit(10);
        Sort sort=Sort.by(Sort.Direction.DESC,"postTime");
        sceneryQuery=sceneryQuery.with(sort);
        return mongoTemplate.find(sceneryQuery,SceneryPost.class);
    }

    public List<SceneryPost> getFriendSceneries(int pageNum,Integer userId){
        ArrayList<Integer> friendUserId=new ArrayList<>();
        friendRepository.findAllByUserUserId(userId)
                .forEach(x->friendUserId.add(x.getFriendUserId()));
        friendUserId.add(userId);
        Query sceneryQuery=new Query(Criteria.where("userId").in(friendUserId));
        sceneryQuery=sceneryQuery.skip(10*(pageNum-1)).limit(10);
        Sort sort=Sort.by(Sort.Direction.DESC,"postTime");
        sceneryQuery=sceneryQuery.with(sort);
        return mongoTemplate.find(sceneryQuery,SceneryPost.class);
    }

    public boolean thumbUp(String sceneryId,Integer userId){
        Query sceneryQuery=new Query(Criteria.where("_id").is(sceneryId));
        sceneryQuery.fields().include("_id");
        SceneryPost post=mongoTemplate.findOne(sceneryQuery,SceneryPost.class);
        if(null==post){
            return false;
        }
        mongoTemplate.updateFirst(sceneryQuery,new Update().addToSet("thumbUps",userId),SceneryPost.class);
        return true;
    }

    public void deThumbUp(String sceneryId,Integer userId){
        Query sceneryQuery=new Query(Criteria.where("_id").is(sceneryId));
        mongoTemplate.updateFirst(sceneryQuery,new Update().pull("thumbUps", userId),SceneryPost.class);
    }

    public Long deleteComment(String sceneryId,Integer userId,Integer commentIndex){
        Query sceneryQuery=new Query(Criteria.where("_id").is(sceneryId));
        sceneryQuery.fields().include("comments");
        SceneryPost scenery=mongoTemplate.findOne(sceneryQuery,SceneryPost.class);
        if(null==scenery){
            return -1l;
        }
        HashMap<Integer, ArrayList<SceneryComment>> commentMap=new HashMap<>();
        SceneryComment commentToDel=null;
        for(SceneryComment sc:scenery.getComments()){
            if(!commentMap.containsKey(sc.getCommentToIndex())){
                commentMap.put(sc.getCommentToIndex(),new ArrayList<>());
            }
            if(commentIndex==sc.getCommentIndex()){
                commentToDel=sc;
            }
            commentMap.get(sc.getCommentToIndex()).add(sc);
        }
        if(null==commentToDel||!userId.equals(commentToDel.getCommentUserId())){
            return -2l;
        }
        ArrayList<Integer> commentIndices=new ArrayList<>();
        LinkedList<Integer> nextIndex=new LinkedList<>();
        nextIndex.add(commentIndex);
        while (!nextIndex.isEmpty()){
            Integer next=nextIndex.removeFirst();
            commentIndices.add(next);
            if(null!=commentMap.get(next)){
                nextIndex.addAll(commentMap.get(next).stream().map(SceneryComment::getCommentIndex).collect(Collectors.toList()));
            }
        }
        BasicDBObject dbObject=new BasicDBObject();
        dbObject.put("$pull",
                new BasicDBObject("comments",
                        new BasicDBObject("commentIndex",
                                new BasicDBObject("$in",commentIndices))));
        System.out.println(dbObject.toJson());
        Update updateComment=new BasicUpdate(dbObject.toJson());
        UpdateResult res=mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(sceneryId)),updateComment,SceneryPost.class);
        return res.getMatchedCount();
    }
}
