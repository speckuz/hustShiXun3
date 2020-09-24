package com.example.myapplication.webService;

import com.example.myapplication.inf.FriendDetailedInf;
import com.example.myapplication.inf.Group;

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

public class GroupWebService {
    private static final String WEB_SET1 = "http://218.244.151.221:8080/login";

    private static final  String WEB_SET_GROUP_SEARCH = "http://218.244.151.221:8080/group/search?groupId=";
    private static final  String WEB_SET_GROUP_SEARCH_MEMBER_ALIAS = "http://218.244.151.221:8080/group/memberAlias?groupId=";
    private static final  String WEB_SET_GROUP_ADD = "http://218.244.151.221:8080/group/add?groupId=";
    private static final  String WEB_SET_GROUP_DELETE = "http://218.244.151.221:8080/group/delete?groupId=";
    private static final  String WEB_SET_GROUP_ESCAPE = "http://218.244.151.221:8080/group/escape?groupId=";
    private static final  String WEB_SET_GROUP_REMOVE_BAD_GUY = "http://218.244.151.221:8080/group/remove?badGuyId=";
    private static final  String WEB_SET_GROUP_CREATE = "http://218.244.151.221:8080/group/create";
    private static final  String WEB_SET_GROUP_PERMISSION = "http://218.244.151.221:8080/group/permission?groupId=103&requestId=1";
    private static final  String WEB_SET_GROUP_REFUSE = "http://218.244.151.221:8080/group/refuse?groupId=";
    private static final  String WEB_SET_GROUP_UPDATE_NAME = "http://218.244.151.221:8080/group/updateName?groupId=";
    private static final  String WEB_SET_GROUP_GET_GROUP_ALIAS= "http://218.244.151.221:8080/group/groupAlias?groupId=";
    private static final  String WEB_SET_GROUP_GET_GROUP_USER_IN= "http://218.244.151.221:8080/group/userIn";

    private static final OkHttpClient client = new OkHttpClient();
    private static FriendDetailedInf friendDetailedInf= null;
    private static String userName = null;
    private String token;



    public GroupWebService(){
        String json = "{\"userName\":\"LuGaJiayi\", \"passwordHash\":\"laji\"}";
        login(WEB_SET1,json);
    }
    //获取某人的全部群聊信息
    public ArrayList<Group> getGroup(){
        ArrayList<Group> groups = new ArrayList<Group>();
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_GET_GROUP_USER_IN).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject groupJson = jsonArray.getJSONObject(i);
                    int id = groupJson.getInt("id");
                    String viniGroupName = groupJson.getString("viniGroupName");
                    int founderId = groupJson.getInt("founderId");
                    Group group = new Group(id,viniGroupName,0,null,null,null,0,null,null);
                    groups.add(group);
                }
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return groups;
    }
    //允许某人加群
    public void permissionGroup(int groupId,int requestId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_PERMISSION+groupId+"&requestId="+requestId).get().build();
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
    //拒绝某人加群
    public void refuseGroup(int groupId,int requestId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_REFUSE+groupId+"&requestId="+requestId).get().build();
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

    //群主踢人
    public void removeBadGuy(int badGuyId,int groupId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_REMOVE_BAD_GUY+badGuyId+"groupId="+groupId).get().build();
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
    //群主修改群名
    public void updateName(int groupId,String groupNewName){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_UPDATE_NAME+groupId+"&groupNewName="+groupNewName).get().build();
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
    //退出群聊
    public void escapeGroup(int groupId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_ESCAPE+groupId).get().build();
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

    //解散群组
    public void deleteGroup(int groupId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_DELETE+groupId).get().build();
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
    //创建群组
    public int createGroup(String groupName){
        String json = "{\"viniGroupName\":\""+groupName+"\"}";
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(json,type);
        Request request = new Request.Builder()
                .header("Authorization",token)
                .url(WEB_SET_GROUP_CREATE)
                .post(stringBody)
                .build();
        Call call = client.newCall(request);
        int id = -1;
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                id = jsonObject1.getInt("id");
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
    //添加群组
    public void addGroup(int groupId){
        Request request = new Request.Builder().header("Authorization",token).url(WEB_SET_GROUP_ADD+groupId).get().build();
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

    //获取群聊备注
    public String getGroupAlias(String groupId){
        Request request = new Request.Builder().url(WEB_SET_GROUP_GET_GROUP_ALIAS+groupId).header("Authorization",token).get().build();
        Call call = client.newCall(request);
        String groupAlias = new String();
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(request.url());
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            int statusCode = jsonObject.getInt("statusCode");
            if(statusCode == 200){
                groupAlias = jsonObject.getString("result");
            }else{

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return groupAlias;
    }
    public ArrayList<String> searchMemberAlias(String  groupId,ArrayList<Integer> members){
        Request request = new Request.Builder().url(WEB_SET_GROUP_SEARCH_MEMBER_ALIAS +groupId).header("Authorization",token).get().build();
        Call call = client.newCall(request);
        ArrayList<String> memberAlias = new ArrayList<String>();
        try{
            Response response = call.execute();
            String result = response.body().string();
            System.out.println(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                if(statusCode == 200){
                    JSONObject inf = jsonObject.getJSONObject("result");
                    for(int i = 0; i < members.size();i++){
                        memberAlias.add(inf.getString(members.get(i)+""));
                    }
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }
        return memberAlias;
    }

    //搜素群组
    public Group searchGroup(String  groupId){
        Request request1 = new Request.Builder().url(WEB_SET_GROUP_SEARCH+groupId).header("Authorization",token).get().build();

        Call call1 = client.newCall(request1);
        Group group = null;
        try{
            Response response = call1.execute();
            String result = response.body().string();
            System.out.println("123");
            System.out.println(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int statusCode = jsonObject.getInt("statusCode");
                if(statusCode == 200){
                    JSONObject inf = jsonObject.getJSONObject("result");
                    int id = inf.getInt("id");
                    String viniGroupName = inf.getString("viniGroupName");
                    int founderId = inf.getInt("founderId");
                    JSONArray membersJson = inf.getJSONArray("members");
                    ArrayList<Integer> members = new ArrayList<Integer>();
                    String identity = inf.getString("identity");
                    for(int i = 0; i < membersJson.length() ;i++){
                        if(membersJson.getInt(i) != founderId)
                            members.add(membersJson.getInt(i));
                    }
                    group = new Group(id,viniGroupName,membersJson.length() - 1,members,searchMemberAlias(groupId,members),identity,founderId,getGroupAlias(groupId),null);
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return group;
    }


    //模拟登陆
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
                    this.token = inf.getString("token");
                    System.out.println(userId);
                    System.out.println(token);
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
