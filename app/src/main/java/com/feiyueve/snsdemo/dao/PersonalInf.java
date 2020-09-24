package com.feiyueve.snsdemo.dao;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.webservice.WebService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalInf implements Serializable {
    private String username;
    private String userId;
    private String nickname;
    private String password;
    private String gender;
    private String birthday;
    private List<String> interests;
    private String location;
    private String signal;
    private String signature;
    private String token;
    private String thumbnail;

    public PersonalInf(String userName) {
        this.username = userName;
    }

    public PersonalInf() {

    }

    public void setPersonalInf(JSONObject jsonObject){
        username = jsonObject.getString("userName");
        userId = jsonObject.getString("userId");
        nickname = jsonObject.getString("nickname");
        password = jsonObject.getString("passwordHash");
        gender = jsonObject.getString("gender");
        if(jsonObject.get("birthday")!=null) {
            birthday = jsonObject.getString("birthday").split("T")[0];
        }
        location = jsonObject.getString("location");
        signal = jsonObject.getString("signal");
        signature = jsonObject.getString("signature");
        JSONArray jsonArray= jsonObject.getJSONArray("interests");
        if(jsonArray!=null)
            interests = JSONArray.parseArray(jsonArray.toJSONString(),String.class);
        if(jsonObject.getString("thumbnail")!=null)
            thumbnail = jsonObject.getString("thumbnail");
    }

    public JSONObject personalInfToJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",username);
        jsonObject.put("nickname",nickname);
        jsonObject.put("passwordHash",password);
        jsonObject.put("gender",gender);
        jsonObject.put("birthday",birthday);
        jsonObject.put("location",location);
        jsonObject.put("signal",signal);
        jsonObject.put("signature",signature);
        if(thumbnail!=null)
            jsonObject.put("thumbnail",thumbnail);
        if(interests!=null)
            jsonObject.put("interests",interests);
        return jsonObject;
    }

    public static void updatePersonalInf(final JSONObject userJSON, final String token){
        try {
            final String content = String.valueOf(userJSON);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = "";
                    try {
                        result = new WebService().uploadData("http://218.244.151.221:8080/user/update",token,content);
                        System.out.println(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
        }
    }

    public static PersonalInf getPersonalInf(final String userName, final String token){
        final PersonalInf personalInf = new PersonalInf();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = "";
                    Map<String,Object> temp = new HashMap<String, Object>();
                    temp.put("userName",userName);
                    try {
                        result = WebService.getByURL("http://218.244.151.221:8080/user/getUserByUserName",temp,token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(result.contains("200")) {
                        System.out.println(result);
                        JSONObject jsonObjectResult = ((JSONObject) JSON.parse(result)).getJSONObject("result");
                        personalInf.setPersonalInf(jsonObjectResult);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personalInf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
