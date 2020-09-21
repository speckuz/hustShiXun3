package sse.hust.vini.scenery;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document(collection = "sceneries")
public class SceneryPost {
    private @Id String id;

    private Integer userId;

    private Date postTime;

    private String text;

    private int nextCommentIndex=1;

    private List<byte[]> pictures;

    private List<Integer> thumbUps;

    private List<SceneryComment> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(List<byte[]> pictures) {
        this.pictures = pictures;
    }

    public List<Integer> getThumbUps() {
        return thumbUps;
    }

    public void setThumbUps(List<Integer> thumbUps) {
        this.thumbUps = thumbUps;
    }

    public List<SceneryComment> getComments() {
        return comments;
    }

    public void setComments(List<SceneryComment> comments) {
        this.comments = comments;
    }

    public int getNextCommentIndex() {
        return nextCommentIndex;
    }

    public void setNextCommentIndex(int nextCommentIndex) {
        this.nextCommentIndex = nextCommentIndex;
    }
}
