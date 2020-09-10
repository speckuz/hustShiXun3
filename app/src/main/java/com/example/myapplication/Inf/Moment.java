package com.example.myapplication.Inf;

public class Moment {
    String id;
    String userName;
    String content;
    String time;
    boolean like;

    public Moment(String userName,String id, String content, String time, boolean like) {
        this.userName = userName;
        this.id = id;
        this.content = content;
        this.time = time;
        this.like = like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getId() {
        return id;
    }

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
