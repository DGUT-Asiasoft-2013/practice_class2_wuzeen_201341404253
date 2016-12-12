package com.dgut.firstexam.fragment.pages;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.activity.LoginActivity;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.api.entity.User;
import com.dgut.firstexam.view.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyProFileFragment extends Fragment {
    View view;
    TextView me;
    ProgressBar progressBar;
    AvatarView avatarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_my_profile, null);
            me = (TextView) view.findViewById(R.id.me);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            avatarView= (AvatarView) view.findViewById(R.id.avatar);
            me.setVisibility(View.INVISIBLE);
        }
        getCurrentUser();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getCurrentUser() {


        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuliderWithApi("me")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                MyProFileFragment.this.onFailure(call, e);

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                MyProFileFragment.this.onResponse(call, response);
            }
        });

    }

    public void onFailure(Call call, IOException e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                me.setText("网络连接错误");
            }
        });

    }

    public void onResponse(Call call, Response response) throws IOException {

        final String result = response.body().string();
        if (!result.equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            final User user = mapper.readValue(result, User.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    me.setVisibility(View.VISIBLE);
                    me.setText("昵称：" + user.getName());
                    avatarView.load(user);
                }
            });
        } else {
            onFailure(call, null);
        }

    }


}
