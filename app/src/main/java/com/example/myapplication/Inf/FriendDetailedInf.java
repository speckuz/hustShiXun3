package com.example.myapplication.Inf;

import java.util.ArrayList;
import java.util.Date;

public class FriendDetailedInf {
    private int userId;
    private String userName;
    private boolean gender;
    private byte[] thumbnail;
    private Date birthday;
    private String location;
    private ArrayList<String> interests;


    public FriendDetailedInf(int userId, String userName, boolean gender, byte[] thumbnail, Date birthday, String location, ArrayList<String> interests) {
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.thumbnail = thumbnail;
        this.birthday = birthday;
        this.location = location;
        this.interests = interests;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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
