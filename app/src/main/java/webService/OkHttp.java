package webService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttp {
    private static final String WEB_SET = "http://218.244.151.221:8080//user/getUserByUserName";

    public void test(String userName){
        OkHttpClient client = new OkHttpClient();
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



    public void searchFriend(String userName){
        OkHttpClient client = new OkHttpClient();
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

    public void searchGroup(String userName){
        OkHttpClient client = new OkHttpClient();
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
