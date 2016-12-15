package com.dgut.firstexam.fragment.pages;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dgut.firstexam.R;
import com.dgut.firstexam.activity.ContentFeedActivity;
import com.dgut.firstexam.activity.NewArticleActivity;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.Article;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class FeedListFragment extends Fragment {
    View view;
    ListView listView;
    List<Article> articles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);
            listView = (ListView) view.findViewById(R.id.list);


            loadArticlePage();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), ContentFeedActivity.class);
                    intent.putExtra("content", articles.get(i).getTitle());
                    startActivity(intent);
                }
            });


        }
        return view;
    }


    ProgressDialog progressDialog;

    private void loadArticlePage() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("加载中");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Request request = Server.requestBuliderWithApi("feeds").get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                final String result = response.body().string();
                ObjectMapper mapper = new ObjectMapper();


                try {
                    JSONObject object = new JSONObject(result);
                    String content=object.getString("content");
                    articles = mapper.readValue(content, new TypeReference<List<Article>>() {});
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("加载成功")
                                    .setMessage(articles.size()+"")
                                    .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            listView.setAdapter(listAdapter);
                                        }
                                    })
                                    .show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return articles == null ? 0 : articles.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View container, ViewGroup parent) {
            View view = null;
            if (container == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
            } else {
                view = container;
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(articles.get(position).getTitle());

            return view;
        }
    };

}
