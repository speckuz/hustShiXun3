package com.feiyueve.snsdemo.dao;

public class RegisterData {

    private String userName;
    private String password;
    private String email;
    private String birthday;

    RegisterData(){

    }

    public RegisterData(String userName, String password, String email){
        this.email = email;
        this.password = password;
        this.userName = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String checkData(){
        if(userName.isEmpty()||password.isEmpty()||email.isEmpty()){
            return "有信息为空";
        }
        if(password.contains(" ")||userName.contains(" ")){
            return "包含空格";
        }
        return "ok";
    }
}
