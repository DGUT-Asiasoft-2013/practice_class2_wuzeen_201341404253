package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.Article;
import com.dgut.firstexam.api.entity.User;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;
import com.dgut.firstexam.util.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewArticleActivity extends Activity {

    Button send;
    SimpleTextInputCellFragment title, content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);
        send = (Button) findViewById(R.id.send);
        title = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_title);
        content = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_content);


    }

    @Override
    protected void onResume() {
        super.onResume();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sibmit();

            }
        });

        title.setHintText("标题");
        content.setHintText("写下你的想法吧~");
        content.setLinesAndLength(100, 1000);
    }

    ProgressDialog progressDialog;

    private void sibmit() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("发表中");
        progressDialog.setCancelable(false);
        progressDialog.show();

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title.getText())
                .addFormDataPart("text", content.getText());

        Request request = Server.requestBuliderWithApi("article")
                .post(multipartBuilder.build())
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewArticleActivity.this, "失败了", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                final String result = response.body().string();
                if (!result.equals("")) {
                    ObjectMapper mapper = new ObjectMapper();
                    final Article article = mapper.readValue(result, Article.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(NewArticleActivity.this)
                                    .setTitle("发表成功")
                                    .setMessage("Article：" + article.getAuthorName())

                                    .setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
                                        }
                                    })
                                    .show();
                        }
                    });

                } else {
                    onFailure(call, null);
                }

            }
        });

    }
}
