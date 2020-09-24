package com.example.myapplication.webService;

import com.example.myapplication.inf.Friend;
import com.example.myapplication.inf.FriendDetailedInf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendInfWebService {
    private static final String WEB_SET_GET_USER = "http://218.244.151.221:8080/user/getUserByUserName";
    private static final String WEB_SET_IS_FRIEND = "http://218.244.151.221:8080/friend/isFriend?userId=";
    private static final String WEB_SET_LOGIN = "http://218.244.151.221:8080/login";
    private static final String WEB_SET_REFUSE = "http://218.244.151.221:8080//friend/refuse?friendUserId=";
    private static final String WEB_SET_FRIEND_RECOMMEND = "http://218.244.151.221:8080/friend/recommend";
    private static final String WEB_SET_FRIEND_CONFIRM = "http://218.244.151.221:8080/friend/confirm";
    private static final String WEB_SET_FRIEND_DELETE = "http://218.244.151.221:8080/friend/delete?friendUserId=";
    private static final String WEB_SET_FRIEND_ADD = "http://218.244.151.221:8080/friend/add";
    private static final String WEB_SET_FRIEND_CHANGE = "http://218.244.151.221:8080/friend/change";
    private static final String WEB_SET_FRIEND_GET_ALL = "http://218.244.151.221:8080/friend/getAll";
    private static final String WEB_SET_FRIEND_BATCH_GET_USER_NAME = "http://218.244.151.221:8080/user/batchGetUserName";


    private static final OkHttpClient client = new OkHttpClient();
    private String token;
    public FriendInfWebService(){
        login();
    }

    //获取好友列表（易）
    public ArrayList<Friend> getAllFriend(){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_FRIEND_GET_ALL).get().build();
        Call call = client.newCall(request);
        ArrayList<Friend> friends = new ArrayList<Friend>();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                JSONArray friendJson = jsonObject.getJSONArray("result");
                for(int i = 0; i < friendJson.length();i++){
                    JSONObject jsonObject1= friendJson.getJSONObject(i);

                    int id = jsonObject1.getInt("userId");
                    String alias = jsonObject1.getString("alias");

                    Friend friend = new Friend(id,alias,"");
                    friends.add(friend);
                }
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friends;
    }
    //添加好友
    public void addFriend(int userId,String alias,String groupName){
        String json = "{\"userId\":\""+userId+"\", \"commentToIndex\":"+alias+"\", \"groupName\":"+groupName+"\"}";
        System.out.println(json);
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_FRIEND_ADD)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){

            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    //删除好友
    public void deleteFriend(int friendUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_FRIEND_DELETE+friendUserId).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){

            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    //同意好友请求
    public void confirmFriend(int friendUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_FRIEND_CONFIRM+friendUserId).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){

            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    //拒绝好友请求
    public void refuseFriend(int friendUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_REFUSE+friendUserId).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){

            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    //获取推荐的好友列表
    public ArrayList<FriendDetailedInf> getRecommendFriendList(){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_FRIEND_RECOMMEND).get().build();
        Call call = client.newCall(request);
        ArrayList<FriendDetailedInf> friendDetailedInf = new ArrayList<FriendDetailedInf>();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                JSONArray friendRecommendList = jsonObject.getJSONArray("result");
                for(int i = 0;i < friendRecommendList.length();i++){
                    JSONObject friendJson = friendRecommendList.getJSONObject(i);
                    friendDetailedInf.add(searchFriendJson(friendJson));
                }
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friendDetailedInf;
    }
    public FriendDetailedInf searchFriendJson(JSONObject friendInfJson){
        try{
            int userId = friendInfJson.getInt("userId");
            String userName = friendInfJson.getString("userName");

            boolean gender = false;
            if(friendInfJson.has("gender")){
                String genderStr = friendInfJson.getString("gender");
                if(genderStr.equals("true")){
                    gender = true;
                }
            }

            String alias = friendInfJson.getString("nickname");
            //thumbnail
            //birthday
            //String thumbnail = friendInfJson.getString("thumbnail");
            String location = null;
            String birthday = null;
            String sign = null;
            if(friendInfJson.has("location"))
                location = friendInfJson.getString("location");
            if(friendInfJson.has("birthday"))
                birthday = friendInfJson.getString("birthday");
            if(friendInfJson.has("signature"))
                sign = friendInfJson.getString("signature");
            JSONArray interestsJson = friendInfJson.getJSONArray("interests");
            ArrayList<String> interests = new ArrayList<String>();
            System.out.println(userId + location);
            for(int i = 0; i < interestsJson.length();i++){
                interests.add(interestsJson.getString(i));
            }
            for(int i = 0; i < interests.size();i++){
                System.out.println(interests.get(i));
            }
            System.out.println(isFriend(userId));
            return new FriendDetailedInf(userId,userName,alias,gender,null,sign,birthday,location,interests,isFriend(userId));
        }catch (Exception e){

        }
        return null;

    }

    public boolean isFriend(int userId){
        Request request = new Request.Builder()
                .url(WEB_SET_IS_FRIEND+userId)
                .header("Authorization",token)
                .get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            System.out.println(result);
            if(statusCode == 200){
                if(jsonObject.getString("result").equals("true")){
                    return true;
                }else{
                    return false;
                }
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    //修改好友信息
    public void change(int userId,String alias,String groupName){
        String json = "{\"userId\":\""+userId+"\", \"commentToIndex\":"+alias+"\", \"groupName\":"+groupName+"\"}";
        System.out.println(json);
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_FRIEND_CHANGE)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){

            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public FriendDetailedInf search(int userId){
        FriendDetailedInf friendDetailedInf = null;
        String user = "["+userId+"]";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(user,type);
        Request request = new Request.Builder()
                .url(WEB_SET_FRIEND_BATCH_GET_USER_NAME)
                .header("Authorization",token)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            friendDetailedInf = searchFriendJson(jsonArray.getJSONObject(0));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friendDetailedInf;
    }


    public FriendDetailedInf search(String userName){

        FriendDetailedInf friendDetailedInf = null;
        if(token == null){
            System.out.println("登录失败");
        }else{
            System.out.println("登录成功");
        }

        // Authorization
        Request request = new Request.Builder().url(WEB_SET_GET_USER+"?userName="+userName).header("Authorization",token).build();

        //request.header("Authorization");

        Call call = client.newCall(request);

        try {

            Response response = call.execute();
            String result = response.body().string();
            System.out.println("success");
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            friendDetailedInf = searchFriendJson(jsonObject.getJSONObject("result"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friendDetailedInf;
    }

    //模拟登录获取token
    public void login() {
        String user = "{\"userName\":\"LuGaJiayi\", \"passwordHash\":\"laji\"}";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(user,type);
        Request request = new Request.Builder()
                .url(WEB_SET_LOGIN)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println("success");
            System.out.println(result);
            try {
                JSONObject json = new JSONObject(result);
                int statusCode = json.getInt("statusCode");
                if(statusCode == 200){
                    JSONObject inf = json.getJSONObject("result");
                    int userId = inf.getInt("userId");
                    String token = inf.getString("token");
                    System.out.println(userId);
                    System.out.println(token);
                    this.token = token;
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
