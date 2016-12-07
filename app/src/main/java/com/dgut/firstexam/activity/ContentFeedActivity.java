package com.dgut.firstexam.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dgut.firstexam.R;

public class ContentFeedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_feed);
        String content=getIntent().getStringExtra("content");

        TextView textView= (TextView) findViewById(R.id.content);
        textView.setText(content);

    }
}
