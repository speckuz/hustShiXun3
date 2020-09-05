package webService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttp {
    private static final String WEB_SET = "http://218.244.151.221:8080/user/exists";

    public void test(String userName){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(WEB_SET+"?userName="+userName).build();
        //Request request = new Request.Builder().url("http://www.baidu.com").build();
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



}
