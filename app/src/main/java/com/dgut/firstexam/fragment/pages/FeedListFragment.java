package com.dgut.firstexam.fragment.pages;

import android.app.Fragment;
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

import java.util.Random;


public class FeedListFragment extends Fragment {
    View view;
    ListView listView;
    String data[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);
            listView = (ListView) view.findViewById(R.id.list);
            listView.setAdapter(listAdapter);
            //10到30个长度
            data = new String[10 + new Random().nextInt(100) % 20];

            for (int i = 0; i < data.length; i++) {
                data[i] = "随机数" + new Random().nextInt(20) + "第" + i;
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(getActivity(), ContentFeedActivity.class);
                    intent.putExtra("content",data[i]);
                    startActivity(intent);
                }
            });


        }
        return view;
    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null ? 0 : data.length;
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
            textView.setText(data[position]);

            return view;
        }
    };


}
