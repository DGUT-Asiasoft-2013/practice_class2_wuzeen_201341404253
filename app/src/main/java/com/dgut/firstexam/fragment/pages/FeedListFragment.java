package com.dgut.firstexam.fragment.pages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dgut.firstexam.R;


public class FeedListFragment extends Fragment {
    View view;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);
            listView = (ListView) view.findViewById(R.id.list);
            listView.setAdapter(listAdapter);
        }
        return view;
    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 100;
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
            textView.setText("this is row " + position);

            return view;
        }
    };


}
