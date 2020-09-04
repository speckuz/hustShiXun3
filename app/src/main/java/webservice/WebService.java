package webservice;

import android.app.DownloadManager;
import android.content.SyncStatusObserver;
import android.util.Log;
import android.view.textclassifier.ConversationActions;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {

    public String uploadData(final String url, final String content) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType type = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody=RequestBody.Companion.create(content,type);
        Request request = new Request.Builder()
                .url(url)
                .post(stringBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println(result);
            return result;
        }
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

    public static String getByURL(String url,Map<String, Object> data) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL newURL = new URL(appendUrl(url,data));
        HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        //设置读取超时
        conn.setReadTimeout(5000);
        int code = conn.getResponseCode();
        //获得响应状态
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
