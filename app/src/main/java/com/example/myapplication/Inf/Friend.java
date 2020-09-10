package com.example.myapplication.Inf;

public class Friend {
    private String friendName;
    private String friendText;
    private String id;
    public Friend(String id, String friendName,String friendText){
        this.id = id;
        this.friendName = friendName;
        this.friendText = friendText;
    }

    public String getFriendName() {
        return friendName;
    }
    public String getId() {
        return id;
    }
    public String getFriendText() {
        return friendText;
    }
}
