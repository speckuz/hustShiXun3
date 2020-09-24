package com.feiyueve.snsdemo.inf;

import android.database.Cursor;

import com.feiyueve.snsdemo.MainActivity;
import com.feiyueve.snsdemo.MyApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Friend implements Serializable {
    private String friendHead;
    private String recentChatText;
    private String recentChatTime;
    private String recentChatLength;
    private String recentChatType;
    private String recentFileLength;
    private String friendName;
    private String friendNickName;
    private String aliasName;
    private String id;
    private String isTop;
    private String grouping;
    private String friendSignature;
    private String gender;
    private String location;
    private String birthday;

    public Friend(String id,String friendNickName,String grouping){
        this.id = id;
        this.friendNickName = friendNickName;
        this.grouping = grouping;
    }

    public Friend(String friendName,String recentChatText,String friendHead,String recentChatTime,String isTop){
        this.friendName = friendName;
        this.recentChatText = recentChatText;
        this.friendHead = friendHead;
        this.recentChatTime = recentChatTime;
        this.isTop = isTop;
    }

    public Friend() {

    }

    public void setFriendInf(){

    }

    public Friend(String id) {
        this.id = id;
    }

    public String getFriendName() {
        return friendName;
    }
    public void setFriendName(String friendName) {this.friendName = friendName;}
    public String getId() {
        return id;
    }
    public void setId(String id) {this.id = id;}

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    public String getRecentChatText() {
        return recentChatText;
    }

    public void setRecentChatText(String recentChatText) {
        this.recentChatText = recentChatText;
    }

    public String getRecentChatTime() {
        return recentChatTime;
    }

    public void setRecentChatTime(String recentChatTime) {
        this.recentChatTime = recentChatTime;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public String getFriendSignature() {
        return friendSignature;
    }

    public void setFriendSignature(String friendSignature) {
        this.friendSignature = friendSignature;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRecentChatLength() {
        return recentChatLength;
    }

    public void setRecentChatLength(String recentChatLength) {
        this.recentChatLength = recentChatLength;
    }

    public String getRecentChatType() {
        return recentChatType;
    }

    public void setRecentChatType(String recentChatType) {
        this.recentChatType = recentChatType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return getId().equals(friend.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String getRecentFileLength() {
        return recentFileLength;
    }

    public void setRecentFileLength(String recentFileLength) {
        this.recentFileLength = recentFileLength;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
