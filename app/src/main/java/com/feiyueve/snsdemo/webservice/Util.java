package com.feiyueve.snsdemo.webservice;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static final String ws = "ws://218.244.151.221:8080/link";//websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
