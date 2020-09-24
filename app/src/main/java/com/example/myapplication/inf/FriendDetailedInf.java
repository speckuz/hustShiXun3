package com.example.myapplication.inf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class FriendDetailedInf implements Serializable {
    private int userId;
    private String userName;
    private String alias;
    private boolean gender;
    private byte[] thumbnail;
    private String sign;
    private String birthday;
    private String location;
    private ArrayList<String> interests;
    private boolean isFriend;
    private String group;


    public FriendDetailedInf(int userId, String userName, String alias,boolean gender, byte[] thumbnail, String sign,String birthday, String location, ArrayList<String> interests,boolean isFriend) {
        this.userId = userId;
        this.userName = userName;
        this.alias = alias;
        this.gender = gender;
        this.thumbnail = thumbnail;
        this.sign = sign;
        this.birthday = birthday;
        this.location = location;
        this.interests = interests;
        this.isFriend = isFriend;
    }
    public boolean isFriend(){return isFriend;}
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }
}
