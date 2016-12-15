package com.dgut.firstexam.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.Article;
import com.dgut.firstexam.util.DateToString;
import com.dgut.firstexam.view.AvatarView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity {

    TextView titleText, textText, authorNameText, dateText;
    AvatarView avatarView;
    Button goToComment, like;
    Article article;
    public static int author_id;//静态变量 供上传评论使用
    boolean isLiked;
    int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_feed);
        article = (Article) getIntent().getSerializableExtra("article");
        author_id = article.getId();
        titleText = (TextView) findViewById(R.id.title);
        textText = (TextView) findViewById(R.id.content);
        authorNameText = (TextView) findViewById(R.id.author);
        avatarView = (AvatarView) findViewById(R.id.avatar);
        goToComment = (Button) findViewById(R.id.comment);
        like = (Button) findViewById(R.id.like);
        dateText = (TextView) findViewById(R.id.createdate);

        getLikesCount();
        checkLiked();
        goToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedContentActivity.this, NewCommentActivity.class);
                intent.putExtra("author_id", author_id);
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLike();
            }
        });
        avatarView.load(article.getAuthor());
    }


    private void getLikesCount() {
        like.setText("加载中");
        like.setEnabled(false);
        Request request = Server.requestBuliderWithApi("article/" + article.getId() + "/likes")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        like.setText("数据异常");
                        like.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 count = Integer.valueOf(response.body().string());

                if (count>=0) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            like.setText("点赞(" +count+")");
                            like.setEnabled(true);
                        }
                    });

                } else {
                    onFailure(call, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        titleText.setText(article.getTitle());
        textText.setText(article.getText());
        authorNameText.setText(article.getAuthor().getName());
        dateText.setText(DateToString.getStringDate(article.getCreateDate()));

    }

    public void saveLike() {
        like.setText("加载中");
        like.setEnabled(false);
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("likes", String.valueOf(!isLiked));

        Request request = Server.requestBuliderWithApi("article/" + article.getId() + "/likes")
                .post(multipartBuilder.build())
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        like.setEnabled(true);
                        like.setText("数据异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                count = Integer.valueOf(response.body().string());

                if (count>=0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            like.setText("点赞(" + count + ")");
                            like.setEnabled(true);
                            isLiked=!isLiked;
                        }
                    });

                } else {
                    onFailure(call, null);
                }
            }
        });
    }

    private void checkLiked() {
        like.setText("加载中");
        like.setEnabled(false);
        Request request = Server.requestBuliderWithApi("article/" + article.getId() + "/isLikes")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        like.setText("数据异常");
                        like.setBackgroundColor(Color.parseColor("#aaaaaa"));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isLiked = Boolean.valueOf(response.body().string());

                if (isLiked) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            like.setEnabled(true);
                            like.setBackgroundColor(Color.parseColor("#FFFF0004"));

                        }
                    });

                } else {
                    onFailure(call, null);
                }
            }
        });

    }
}
