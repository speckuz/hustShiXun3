package com.example.myapplication.inf;

public class Friend {
    private String friendName;
    private String friendText;
    private int id;
    public Friend(int id, String friendName,String friendText){
        this.id = id;
        this.friendName = friendName;
        this.friendText = friendText;
    }


    public String getFriendName() {
        return friendName;
    }
    public int getId() {
        return id;
    }
    public String getFriendText() {
        return friendText;
    }
}
