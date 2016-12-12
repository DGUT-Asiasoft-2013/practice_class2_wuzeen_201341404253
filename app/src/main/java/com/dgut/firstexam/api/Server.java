package com.dgut.firstexam.api;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/10.
 */

public class Server {
    static OkHttpClient client;
    public static String serverAddress = "http://172.27.0.10:8080/membercenter/";

    static {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public static OkHttpClient getSharedClient() {
        return client;
    }

    public static Request.Builder requestBuliderWithApi(String api) {
        return new Request.Builder().url(serverAddress + "api/" + api);
    }

}
