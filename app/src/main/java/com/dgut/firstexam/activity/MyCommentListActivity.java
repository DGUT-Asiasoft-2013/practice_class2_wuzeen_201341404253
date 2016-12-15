package com.dgut.firstexam.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.Comment;
import com.dgut.firstexam.api.entity.Page;
import com.dgut.firstexam.util.DateToString;
import com.dgut.firstexam.view.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MyCommentListActivity extends Activity {
    View btnLoadMore;
    TextView textLoadMore;
    ListView listView;
    List<Comment> data;
    Page<Comment> commentPage;
    int pageSize=10;
    int page = 0;
    int NOT_MORE_PAGE = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment_list);
        
        listView= (ListView) findViewById(R.id.list);

        btnLoadMore = getLayoutInflater().inflate(R.layout.list_foot, null);
        textLoadMore = (TextView) btnLoadMore.findViewById(R.id.loadmore);
        listView.addFooterView(btnLoadMore);
        listView.setAdapter(listAdapter);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page!=NOT_MORE_PAGE) {

                    getMyCommentList();
                }
            }
        });

    }


    BaseAdapter listAdapter = new BaseAdapter() {

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.activity_my_comment_list_item, null);
            } else {
                view = convertView;
            }

            TextView textContent = (TextView) view.findViewById(R.id.content);
            TextView textTitle = (TextView) view.findViewById(R.id.title);
            TextView textAuthorName = (TextView) view.findViewById(R.id.author);
            TextView textDate = (TextView) view.findViewById(R.id.createdate);
            AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

            Comment comment = data.get(position);

            textContent.setText(comment.getContent());
            textTitle.setText(comment.getArticle().getText());
            textAuthorName.setText(comment.getAuthor().getName());
            avatar.load(comment.getAuthor());


            textDate.setText(DateToString.getStringDate(comment.getCreateDate()));

            return view;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getMyCommentList();

    }

    private void getMyCommentList() {
        textLoadMore.setEnabled(false);
        textLoadMore.setText("加载中");


          Request request = Server.requestBuliderWithApi("comment/toOther/" + page++)
                    .get()
                    .build();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textLoadMore.setEnabled(true);
                        textLoadMore.setText("网络异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                final ObjectMapper mapper = new ObjectMapper();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textLoadMore.setEnabled(true);
                        textLoadMore.setText("数据解析中");
                        try {
                            commentPage = mapper.readValue(result, new TypeReference<Page<Comment>>() {
                            });



                            if (data==null||(data.size()<=pageSize&&page==1))
                            {   textLoadMore.setText("加载更多");
                                data = commentPage.getContent();
                                listAdapter.notifyDataSetInvalidated();
                            }
                            else if(commentPage.getTotalPages()!=page){
                                textLoadMore.setText("加载更多");
                                data.addAll(commentPage.getContent());
                                listAdapter.notifyDataSetChanged();
                            }
                            else {
                                page=NOT_MORE_PAGE;
                                textLoadMore.setText("没有新内容");
                                data.addAll(commentPage.getContent());
                                listAdapter.notifyDataSetChanged();
                            }



                        } catch (IOException e) {
                            textLoadMore.setText("数据解析失败"+e.getLocalizedMessage());
                        }






                    }
                });
            }
        });





    }
}
