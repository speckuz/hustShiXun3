package com.feiyueve.snsdemo;

public class LoginData {
    private String userName;
    private String password;

    LoginData(){};

    LoginData(String userName,String password){
        this.userName = userName;
        this.password = password;
    };

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
}
