package com.example.myapplication.inf;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private int groupId;
    private String groupName;
    private int membersNum;
    private ArrayList<Integer> membersId;
    private ArrayList<String> membersAliasList;
    private ArrayList<Friend> members;
    private String identity;

    public String getIdentity() {
        return identity;
    }
    public void setMembersId(ArrayList<Integer> membersId) {
        this.membersId = membersId;
    }

    public void setMembersAliasList(ArrayList<String> membersAliasList) {
        this.membersAliasList = membersAliasList;
    }

    public void setMembers(ArrayList<Friend> members) {
        this.members = members;
    }

    public int getMembersNum() {
        return membersNum;
    }

    public ArrayList<Integer> getMembersId() {
        return membersId;
    }

    public ArrayList<String> getMembersAliasList() {
        return membersAliasList;
    }

    public ArrayList<Friend> getMembers() {
        return members;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Group(int groupId, String groupName,int membersNum,ArrayList<Integer> membersId,ArrayList<String> membersAliasList,String identity) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.membersNum = membersNum;
        this.membersId = membersId;
        this.membersAliasList = membersAliasList;
        this.identity = identity;
    }

}
