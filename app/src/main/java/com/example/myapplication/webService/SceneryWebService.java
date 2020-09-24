package com.example.myapplication.webService;

import com.example.myapplication.inf.Comment;
import com.example.myapplication.inf.FriendDetailedInf;
import com.example.myapplication.inf.Group;
import com.example.myapplication.inf.Moment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SceneryWebService {
    private static final  String WEB_SET_LIKE_MOMENT = "http://218.244.151.221:8080//scenery/thumbUp?sceneryId=";
    private static final  String WEB_SET_DISLIKE_MOMENT = "http://218.244.151.221:8080//scenery/deThumbUp?sceneryId=";

    private static final  String WEB_SET_GET_FRIEND_SCENERY = "http://218.244.151.221:8080//scenery/getFriendScenery?page=";
    private static final  String WEB_SET_GET_WORLD_SCENERY = "http://218.244.151.221:8080//scenery/get?page=";
    private static final  String WEB_SET_DELETE_SCENERY ="http://218.244.151.221:8080//scenery/delete?sceneryId=";
    private static final  String WEB_SET_DELETE_COMMENT ="http://218.244.151.221:8080//scenery/deleteComment?sceneryId=";
    private static final  String WEB_SET_COMMENT ="http://218.244.151.221:8080/scenery/comment?sceneryId=";
    private static final  String WEB_SET_POST_SCENERY ="http://218.244.151.221:8080//scenery/post";
    private static final String WEB_SET1 = "http://218.244.151.221:8080/login";
    private static final String WEB_SET_BATCH_GET_USER_NAME = "http://218.244.151.221:8080//user/batchGetUserName";
    private static final String WEB_SET_GET_ONE_FRIEND_MOMENT = "http://218.244.151.221:8080/scenery/getUserScenery?page=";

    private String token;
    private static final OkHttpClient client = new OkHttpClient();

    public SceneryWebService(){
        login();
    }




    //模拟登录获取token
    public void login() {
        String user = "{\"userName\":\"LuGaJiayi\", \"passwordHash\":\"laji\"}";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(user,type);
        Request request = new Request.Builder()
                .url(WEB_SET1)
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

    public String batchGetUserName(int userId){
        String json = "[" + userId + "]";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_BATCH_GET_USER_NAME)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                JSONObject friendInf = jsonObject.getJSONArray("result").getJSONObject(0);
                return friendInf.getString("nickname");
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //发动态
    public void postScenery(String text,ArrayList<String> pictures){
        String json = "{\"text\":\""+text+"\", \"pictures\":[]}";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_POST_SCENERY)
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
    //点赞
    public void likeMoment(String sceneryId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_LIKE_MOMENT+sceneryId).get().build();
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
    //取消点赞
    public void dislikeMoment(String sceneryId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_DISLIKE_MOMENT+sceneryId).get().build();
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
    //评论
    public void comment (String sceneryId,String commentText, int commentToIndex){
        String json = "{\"commentText\":\""+commentText+"\", \"commentToIndex\":"+commentToIndex+"}";
        System.out.println(json + sceneryId);
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_COMMENT+sceneryId)
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
    //删除动态
    public void deleteScenery(String sceneryId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_DELETE_SCENERY+sceneryId).get().build();
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

    //删除评论
    public void deleteComment(String sceneryId,int commentIndex){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_DELETE_COMMENT+sceneryId+"&commentIndex="+commentIndex).get().build();
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



    //获取好友的动态信息
    public ArrayList<Moment> getFriendScenery(String page,int searchUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GET_FRIEND_SCENERY+page).build();
        Call call = client.newCall(request);
        ArrayList<Moment> moments = new ArrayList<Moment>();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                if(statusCode == 200){
                    JSONArray inf = jsonObject.getJSONArray("result");
                    for(int i = 0; i < inf.length();i++){
                        JSONObject jsonMoment = inf.getJSONObject(i);
                        String id = jsonMoment.getString("id");
                        int userId = jsonMoment.getInt("userId");
                        String postTime = jsonMoment.getString("postTime");
                        String text = jsonMoment.getString("text");
                        int nextCommentIndex = jsonMoment.getInt("nextCommentIndex");
                        JSONArray pictures = jsonMoment.getJSONArray("pictures");
                        JSONArray thumbUps = jsonMoment.getJSONArray("thumbUps");
                        JSONArray comments = jsonMoment.getJSONArray("comments");
                        boolean like = false;
                        for(int j = 0;j < pictures.length();j++){

                        }
                        ArrayList<Integer> thumbUpsList = new ArrayList<Integer>();
                        ArrayList<String> thumbUpsName = new ArrayList<String>();
                        for(int j = 0;j < thumbUps.length();j++){
                            thumbUpsList.add(thumbUps.getInt(j));
                            thumbUpsName.add(batchGetUserName(thumbUps.getInt(j)));
                            if(thumbUps.getInt(j) == searchUserId){
                                like = true;
                            }
                        }
                        ArrayList<Comment> commentList= new ArrayList<Comment>();
                        for(int j = 0;j < comments.length();j++){
                            JSONObject comment = comments.getJSONObject(j);
                            int commentIndex = comment.getInt("commentIndex");
                            int commentToIndex = comment.getInt("commentToIndex");
                            int commentUserId = comment.getInt("commentUserId");
                            String commentText = comment.getString("commentText");
                            String commentToUserName = new String();
                            if(commentToIndex != 0)
                                for(int k = 0;k < commentList.size();k++){
                                    if(commentList.get(k).getCommentIndex() == commentToIndex){
                                        commentToUserName = batchGetUserName(commentList.get(k).getCommentUserId());
                                    }
                                }
                            commentList.add(new Comment(commentIndex,batchGetUserName(commentUserId),commentToUserName,commentToIndex,commentUserId,commentText));
                        }
                        moments.add(new Moment(batchGetUserName(userId),id,userId,text,postTime,like,nextCommentIndex,thumbUpsList,thumbUpsName,commentList));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return moments;
    }

    public ArrayList<Moment> getOneFriendScenery(String page,int searchUserId,int searchFriendUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GET_ONE_FRIEND_MOMENT+page+"&userId="+searchFriendUserId).build();
        System.out.println(request.url());
        Call call = client.newCall(request);
        ArrayList<Moment> moments = new ArrayList<Moment>();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                if(statusCode == 200){
                    JSONArray inf = jsonObject.getJSONArray("result");
                    for(int i = 0; i < inf.length();i++){
                        JSONObject jsonMoment = inf.getJSONObject(i);
                        String id = jsonMoment.getString("id");
                        int userId = jsonMoment.getInt("userId");
                        String postTime = jsonMoment.getString("postTime");
                        String text = jsonMoment.getString("text");
                        int nextCommentIndex = jsonMoment.getInt("nextCommentIndex");
                        JSONArray pictures = jsonMoment.getJSONArray("pictures");
                        JSONArray thumbUps = jsonMoment.getJSONArray("thumbUps");
                        JSONArray comments = jsonMoment.getJSONArray("comments");
                        boolean like = false;
                        for(int j = 0;j < pictures.length();j++){

                        }
                        ArrayList<Integer> thumbUpsList = new ArrayList<Integer>();
                        ArrayList<String> thumbUpsName = new ArrayList<String>();
                        for(int j = 0;j < thumbUps.length();j++){
                            thumbUpsList.add(thumbUps.getInt(j));
                            thumbUpsName.add(batchGetUserName(thumbUps.getInt(j)));
                            if(thumbUps.getInt(j) == searchUserId){
                                like = true;
                            }
                        }
                        ArrayList<Comment> commentList= new ArrayList<Comment>();
                        for(int j = 0;j < comments.length();j++){
                            JSONObject comment = comments.getJSONObject(j);
                            int commentIndex = comment.getInt("commentIndex");
                            int commentToIndex = comment.getInt("commentToIndex");
                            int commentUserId = comment.getInt("commentUserId");
                            String commentText = comment.getString("commentText");
                            String commentToUserName = new String();
                            if(commentToIndex != 0)
                                for(int k = 0;k < commentList.size();k++){
                                    if(commentList.get(k).getCommentIndex() == commentToIndex){
                                        commentToUserName = batchGetUserName(commentList.get(k).getCommentUserId());
                                    }
                                }
                            commentList.add(new Comment(commentIndex,batchGetUserName(commentUserId),commentToUserName,commentToIndex,commentUserId,commentText));
                        }
                        moments.add(new Moment(batchGetUserName(userId),id,userId,text,postTime,like,nextCommentIndex,thumbUpsList,thumbUpsName,commentList));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return moments;
    }

    //获取广场的动态信息
    public ArrayList<Moment> getWorldScenery(String page,int searchUserId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GET_WORLD_SCENERY+page).build();
        Call call = client.newCall(request);
        ArrayList<Moment> moments = new ArrayList<Moment>();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                if(statusCode == 200){
                    JSONArray inf = jsonObject.getJSONArray("result");
                    for(int i = 0; i < inf.length();i++){
                        JSONObject jsonMoment = inf.getJSONObject(i);
                        String id = jsonMoment.getString("id");
                        int userId = jsonMoment.getInt("userId");
                        String postTime = jsonMoment.getString("postTime");
                        String text = jsonMoment.getString("text");
                        int nextCommentIndex = jsonMoment.getInt("nextCommentIndex");
                        JSONArray pictures = jsonMoment.getJSONArray("pictures");
                        JSONArray thumbUps = jsonMoment.getJSONArray("thumbUps");
                        JSONArray comments = jsonMoment.getJSONArray("comments");
                        boolean like = false;
                        for(int j = 0;j < pictures.length();j++){

                        }
                        ArrayList<Integer> thumbUpsList = new ArrayList<Integer>();
                        ArrayList<String> thumbUpsName = new ArrayList<String>();
                        for(int j = 0;j < thumbUps.length();j++){
                            thumbUpsList.add(thumbUps.getInt(j));
                            thumbUpsName.add(batchGetUserName(thumbUps.getInt(j)));
                            if(thumbUps.getInt(j) == searchUserId){
                                like = true;
                            }
                        }
                        ArrayList<Comment> commentList= new ArrayList<Comment>();
                        for(int j = 0;j < comments.length();j++){
                            JSONObject comment = comments.getJSONObject(j);
                            int commentIndex = comment.getInt("commentIndex");
                            int commentToIndex = comment.getInt("commentToIndex");
                            int commentUserId = comment.getInt("commentUserId");
                            String commentText = comment.getString("commentText");
                            String commentToUserName = new String();
                            if(commentToIndex != 0)
                                for(int k = 0;k < commentList.size();k++){
                                    if(commentList.get(k).getCommentIndex() == commentToIndex){
                                        commentToUserName = batchGetUserName(commentList.get(k).getCommentUserId());
                                    }
                                }
                            commentList.add(new Comment(commentIndex,batchGetUserName(commentUserId),commentToUserName,commentToIndex,commentUserId,commentText));
                        }
                        moments.add(new Moment(batchGetUserName(userId),id,userId,text,postTime,like,nextCommentIndex,thumbUpsList,thumbUpsName,commentList));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return moments;
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
