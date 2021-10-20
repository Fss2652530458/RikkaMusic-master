package com.rikkathewrold.rikkamusic.login.mvp.view;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyHttpUtil {
    public void myokhttp() {
        String TOKEN = "";
        //创建okhttp对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        //创建请求体
        String urlStr = "http://127.0.0.1:3000/login/cellphone?password=32107251zyr&phone=18987415349";
        Request request = new Request.Builder().addHeader("Authorization", TOKEN)
                .url(urlStr)
                .build();
        //创建响应体
        Response response = null;
        String responseStr = null;
        try {
            response = okHttpClient.newCall(request).execute();
            responseStr = response.body().string();
            Log.d("ri",response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
