package com.dgut.firstexam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dgut.firstexam.R;

public class BootActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //改执行线程和创建handler的线程一致
                Intent intent=new Intent(BootActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);



    }
}
