package com.dgut.firstexam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 初始化界面 尝试请求网络
 */
public class BootActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
    }

    @Override
    protected void onResume() {
        super.onResume();

        OkHttpClient client = new OkHttpClient();
        Request request = Server.requestBuliderWithApi("hello")
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                BootActivity.this.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                BootActivity.this.onResponse(call, response);
            }
        });
    }

    public void onFailure(Call call, final IOException e) {
        BootActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BootActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResponse(Call call, final Response response) throws IOException {
        BootActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Toast.makeText(BootActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(BootActivity.this,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                Intent itnt = new Intent(BootActivity.this, LoginActivity.class);
                startActivity(itnt);
                finish();
            }
        });
    }

}
