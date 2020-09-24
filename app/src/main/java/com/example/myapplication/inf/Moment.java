package com.example.myapplication.inf;

import com.example.myapplication.webService.SceneryWebService;

import java.util.ArrayList;

public class Moment {
    private int userId;
    private String userName;
    private String momentId;
    private String content;
    private String time;
    private boolean like;
    private int nextCommentIndex;

    private ArrayList<Integer> likeUserId;
    private ArrayList<String> likeUserName;
    private ArrayList<Comment> comments;

    public Moment(String userName,String momentId,int userId, String content, String time, boolean like,int nextCommentIndex,ArrayList<Integer> likeUserId,
                  ArrayList<String> likeUserName,ArrayList<Comment> comments) {
        this.userName = userName;
        this.momentId = momentId;
        this.userId = userId;
        this.content = content;
        this.nextCommentIndex = nextCommentIndex;
        this.time = time;
        this.like = like;
        this.comments = comments;
        this.likeUserId = likeUserId;
        this.likeUserName = likeUserName;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }
    public int getNextCommentIndex() {
        return nextCommentIndex;
    }

    public void setNextCommentIndex(int nextCommentIndex) {
        this.nextCommentIndex = nextCommentIndex;
    }
    public void setLike(boolean like) {
        this.like = like;
    }
    public ArrayList<Comment> getComments(){
        return comments;
    }
    public int getUserId() {
        return userId;
    }
    public ArrayList<String> getLikeUserName(){return likeUserName;}
    public String getUserName() {
        return userName;
    }
    public boolean getLike(){
        return  like;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
