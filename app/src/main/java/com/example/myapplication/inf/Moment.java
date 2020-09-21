package com.example.myapplication.inf;

import java.util.ArrayList;

public class Moment {
    private String id;
    private String userName;
    private String content;
    private String time;
    private boolean like;
    private ArrayList<String> likeUserName;
    private ArrayList<Comment> comments;

    public Moment(String userName,String id, String content, String time, boolean like,ArrayList<String> likeUserName,ArrayList<Comment> comments) {
        this.userName = userName;
        this.id = id;
        this.content = content;
        this.time = time;
        this.like = like;
        this.comments = comments;
        this.likeUserName = likeUserName;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
    public ArrayList<Comment> getComments(){
        return comments;
    }
    public String getId() {
        return id;
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
