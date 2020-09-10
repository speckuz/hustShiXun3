package webService;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.myapplication.FriendInf;
import com.example.myapplication.Inf.Friend;
import com.example.myapplication.Inf.FriendDetailedInf;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {
    private static final String WEB_SET1 = "http://218.244.151.221:8080/login";
    private static final String WEB_SET = "http://218.244.151.221:8080/user/getUserByUserName";

    private static final OkHttpClient client = new OkHttpClient();
    private static FriendDetailedInf friendDetailedInf= null;
    private static String userName = null;

    public void test(String userName){
        Request request = new Request.Builder().url(WEB_SET+"?userName="+userName).build();
        //Request request = new Request.Builder().url("http://www   .baidu.com").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);


            }
        });
    }




    public void login(final String url, final String content) {
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(content,type);
        Request request = new Request.Builder()
                .url(url)
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
                    search(token);
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void search(String token){

        if(token == null){
            System.out.println("登录失败");
        }else{
            System.out.println("登录成功");
        }

        // Authorization
        Request request = new Request.Builder().url(WEB_SET+"?userName="+userName).header("Authorization",token).build();

        //request.header("Authorization");

        Call call = client.newCall(request);

        try {

            Response response = call.execute();
            String result = response.body().string();
            System.out.println("success");
            System.out.println(result);
            friendDetailedInf = searchFriendJson(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FriendDetailedInf searchFriend(String userName){
        String json = "{\"userName\":\"LuGaJiayi\", \"passwordHash\":\"laji\"}";
        this.userName = userName;
        login(WEB_SET1,json);


        System.out.println(friendDetailedInf == null);
        return friendDetailedInf;
    }

    public FriendDetailedInf searchFriendJson(String friend){
        try {
            JSONObject json = new JSONObject(friend);
            int statusCode = json.getInt("statusCode");
            if(statusCode == 200){
                JSONObject friendInfJson = json.getJSONObject("result");
                int userId = friendInfJson.getInt("userId");
                String userName = friendInfJson.getString("userName");
                String genderStr = friendInfJson.getString("gender");
                boolean gender = false;
                if(genderStr.equals("true")){
                    gender = true;
                }
                //thumbnail
                //birthday
                String location = friendInfJson.getString("location");

                JSONArray interestsJson = friendInfJson.getJSONArray("interests");
                ArrayList<String> interests = new ArrayList<String>();
                System.out.println(userId + genderStr + location);
                for(int i = 0; i < interestsJson.length();i++){
                    interests.add(interestsJson.getString(i));
                }
                for(int i = 0; i < interests.size();i++){
                    System.out.println(interests.get(i));
                }
                return new FriendDetailedInf(userId,userName,gender,null,null,location,interests);
            }else{
                String errMsg = json.getString("errMsg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void searchGroup(String userName){
        Request request = new Request.Builder().url(WEB_SET+"?userName="+userName).build();
        //Request request = new Request.Builder().url("http://www   .baidu.com").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("shibai");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);


            }
        });
    }

    private static String appendUrl(String url, Map<String, Object> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        for (String key : data.keySet()) {
            param.append(key + "=" + data.get(key).toString() + "&");
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (newUrl.indexOf("?") >= 0) {
            newUrl += "&" + paramStr;
        } else {
            newUrl += "?" + paramStr;
        }
        return newUrl;
    }




}
