package com.dgut.firstexam.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;

public class NewArticleActivity extends Activity {

    Button send;
    SimpleTextInputCellFragment title,content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);
        send= (Button) findViewById(R.id.send);
        title= (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_title);
        content= (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_content);


    }

    @Override
    protected void onResume() {
        super.onResume();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.slide_out_bottom); }
        });

        title.setHintText("标题");
        content.setHintText("写下你的想法吧~");
        content.setLines(20);
    }
}
