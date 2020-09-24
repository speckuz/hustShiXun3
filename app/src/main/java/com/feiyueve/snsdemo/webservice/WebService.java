package com.feiyueve.snsdemo.webservice;

import android.app.DownloadManager;
import android.content.SyncStatusObserver;
import android.util.Log;
import android.view.textclassifier.ConversationActions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.RegisterActivity;
import com.feiyueve.snsdemo.dao.PersonalInf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {

    public static String uploadData(final String url, String token,final String content) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(content,type);
        Request request;
        if(token==null){
            request = new Request.Builder()
                    .url(url)
                    .post(stringBody)
                    .build();
        }else {
            request = new Request.Builder()
                    .addHeader("Authorization",token)
                    .url(url)
                    .post(stringBody)
                    .build();
        }
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            return result;
        }
    }

    public static String getFriendInf(String[] friendId,String token){
        String string = JSON.toJSONString(friendId);
        String result = "";
        try {
            result = WebService.uploadData("http://218.244.151.221:8080/user/batchGetUserName", token, string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("3333333333333333333333"+result);
        return result;
    }

    public static String getFriendList(String token){
        String result = "";
        try {
            result = WebService.getByURL("http://218.244.151.221:8080/friend/getAll", null, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("22222222222222222222"+result);
        return result;
    }

    private static String appendUrl(String url, Map<String, Object> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        if(data!=null) {
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
        }
        return newUrl;
    }

    public static String checkLoginStatus(final String userName, final String token) {
        String result = "";
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("userName", userName);
        try {
            result = WebService.getByURL("http://218.244.151.221:8080/user/getUserByUserName", temp, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1111111111111111"+result);
        if(!result.contains("200"))
            return "errMsg";
        return result;
    }

    public static String postByURL(String url,Map<String, Object> data,String token) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL newURL = new URL(appendUrl(url,data));
        HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
        conn.setRequestMethod("POST");
        if(token!=null){
            conn.setRequestProperty("Authorization",token);
        }
        conn.connect();
        int resultCode = conn.getResponseCode();
        if (HttpURLConnection.HTTP_OK == resultCode) {
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
        }
        conn.disconnect();
        return (sb.toString());
    }

    public static String getByURL(String url,Map<String, Object> data,String token) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL newURL = new URL(appendUrl(url,data));
        HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
        conn.setRequestMethod("GET");
        if(token!=null){
            conn.setRequestProperty("Authorization",token);
        }
        conn.connect();
        int resultCode = conn.getResponseCode();
        if (HttpURLConnection.HTTP_OK == resultCode) {
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
        }
        conn.disconnect();
        return (sb.toString());
    }

    private String parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject object = new JSONObject(Boolean.parseBoolean(jsonData));
            String name = object.getString("result");
            //日志
            Log.d("name", "结果是：" + name);
            return name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
