package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dgut.firstexam.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        Request request = new Request.Builder()
                .url("http://172.27.0.35:8080/membercenter/api/hello")
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                BootActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BootActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                BootActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							Toast.makeText(BootActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
                        Intent itnt = new Intent(BootActivity.this, LoginActivity.class);
                        startActivity(itnt);
                        finish();
					}
				});
            }

        });



    }
}
