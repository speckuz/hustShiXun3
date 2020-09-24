package com.feiyueve.snsdemo;

import android.app.Application;

import com.feiyueve.snsdemo.dao.LoginData;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.webservice.JWebSocketClient;
import com.feiyueve.snsdemo.webservice.JWebSocketClientService;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {
    private LoginData loginData;
    private PersonalInf personalInf;
    private Friend currentFriend;
    private HashMap<String, ArrayList<Friend>> friendLists;
    private ArrayList<String> groupName;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;

    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }


    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public PersonalInf getPersonalInf() {
        return personalInf;
    }

    public void setPersonalInf(PersonalInf personalInf) {
        this.personalInf = personalInf;
    }

    public Friend getCurrentFriend() {
        return currentFriend;
    }

    public void setCurrentFriend(Friend currentFriend) {
        this.currentFriend = currentFriend;
    }

    public HashMap<String, ArrayList<Friend>> getFriendLists() {
        return friendLists;
    }

    public void setFriendLists(HashMap<String, ArrayList<Friend>> friendLists) {
        this.friendLists = friendLists;
    }

    public ArrayList<String> getGroupName() {
        return groupName;
    }

    public void setGroupName(ArrayList<String> groupName) {
        this.groupName = groupName;
    }

    public JWebSocketClient getClient() {
        return client;
    }

    public void setClient(JWebSocketClient client) {
        this.client = client;
    }

    public JWebSocketClientService.JWebSocketClientBinder getBinder() {
        return binder;
    }

    public void setBinder(JWebSocketClientService.JWebSocketClientBinder binder) {
        this.binder = binder;
    }

    public JWebSocketClientService getjWebSClientService() {
        return jWebSClientService;
    }

    public void setjWebSClientService(JWebSocketClientService jWebSClientService) {
        this.jWebSClientService = jWebSClientService;
    }

}
