
package com.dgut.firstexam.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.dgut.firstexam.R;
import com.dgut.firstexam.activity.FeedContentActivity;
import com.dgut.firstexam.activity.LoginActivity;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.Article;
import com.dgut.firstexam.api.entity.Comment;
import com.dgut.firstexam.api.entity.Page;
import com.dgut.firstexam.util.DateToString;
import com.dgut.firstexam.util.MD5;
import com.dgut.firstexam.view.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedListFragment extends Fragment {

    View view;
    ListView listView;
    View btnLoadMore;
    TextView textLoadMore;
    SearchView searchView;
    List<Article> data;
    Page<Article> articlePage;
    int page = 0;
    Activity activity;
    int NOT_MORE_PAGE = -1;
    String keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            activity = getActivity();
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);
            btnLoadMore = inflater.inflate(R.layout.list_foot, null);
            textLoadMore = (TextView) btnLoadMore.findViewById(R.id.loadmore);
            searchView = (SearchView) view.findViewById(R.id.search);
            listView = (ListView) view.findViewById(R.id.list);
            listView.addFooterView(btnLoadMore);
            listView.setAdapter(listAdapter);

            searchView.setSubmitButtonEnabled(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClicked(position);
                }
            });

            btnLoadMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    searchArticle();

                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    page=0;
                    if (s.equals(""))
                        keyword = null;
                    else
                        keyword = s;
                    searchArticle();
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    searchView.clearFocus(); // 不获取焦点

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        return view;
    }

    BaseAdapter listAdapter = new BaseAdapter() {

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.fragment_page_feed_list_item, null);
            } else {
                view = convertView;
            }

            TextView textContent = (TextView) view.findViewById(R.id.content);
            TextView textTitle = (TextView) view.findViewById(R.id.title);
            TextView textAuthorName = (TextView) view.findViewById(R.id.author);
            TextView textDate = (TextView) view.findViewById(R.id.createdate);
            AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

            Article article = data.get(position);

            textContent.setText(article.getText());
            textTitle.setText(article.getTitle());
            textAuthorName.setText(article.getAuthor().getName());
            avatar.load(article.getAuthor());


            textDate.setText(DateToString.getStringDate(article.getCreateDate()));

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

    void onItemClicked(int position) {
        Article article = data.get(position);

        Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
        itnt.putExtra("article", article);
        startActivity(itnt);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchArticle();
    }

    Request request;

    public void searchArticle() {
        textLoadMore.setEnabled(false);
        textLoadMore.setText("加载中");

        if (keyword != null) {
            request = Server.requestBuliderWithApi("article/search/" + keyword + "?page=" + page++)
                    .get()
                    .build();
        } else {
            request = Server.requestBuliderWithApi("feeds/" + page++)
                    .get()
                    .build();
        }
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
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
                ObjectMapper mapper = new ObjectMapper();
                articlePage = mapper.readValue(result, new TypeReference<Page<Article>>() {
                });

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textLoadMore.setEnabled(true);
                        if (articlePage.getTotalPages() > page) {
                            textLoadMore.setText("加载更多");
                            data.addAll(articlePage.getContent());
                            listAdapter.notifyDataSetChanged();
                        } else if (page == 0) {
                            data = articlePage.getContent();
                            listAdapter.notifyDataSetInvalidated();
                        } else if (articlePage.getTotalPages() == 0) {
                            page = NOT_MORE_PAGE;
                            textLoadMore.setText("没有新文章了");
                        } else {
                            page = NOT_MORE_PAGE;
                            data = articlePage.getContent();
                            listAdapter.notifyDataSetInvalidated();
                            textLoadMore.setText("没有新文章了");
                        }

                    }
                });
            }
        });
    }
}