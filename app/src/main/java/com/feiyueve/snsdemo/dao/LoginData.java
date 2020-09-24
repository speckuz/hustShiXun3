package com.feiyueve.snsdemo.dao;

import java.io.Serializable;

public class LoginData implements Serializable {
    private String userName;
    private String password;
    private String userKey;
    private String userPetName;
    private String gender;
    private String userId;
    private int birthYear;
    private int birthMonth;
    private int birthDay;

    public LoginData(){};

    public LoginData(String userName, String password){
        this.userName = userName;
        this.password = password;
    };

    public LoginData(String userName,String userKey,String password){
        this.userName = userName;
        this.password = password;
        this.userKey = userKey;
    };

    public LoginData(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String checkLoginData(){
        if(userName.isEmpty()||password.isEmpty()){
            return "有信息为空";
        }
        if(password.contains(" ")||userName.contains(" ")){
            return "包含空格";
        }
        return "ok";
    }

    public String getBirthday(){
        String strMonth = ""+birthMonth;
        String strDay = ""+birthDay;
        if(birthMonth<10)
            strMonth = "0"+birthMonth;
        if(birthDay<10)
            strDay = "0"+birthDay;
        return this.birthYear+"-"+strMonth+"-"+strDay;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
