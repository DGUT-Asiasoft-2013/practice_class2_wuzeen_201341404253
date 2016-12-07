package com.dgut.firstexam.fragment.widgets;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgut.firstexam.R;
import com.dgut.firstexam.activity.NewArticleActivity;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MainTabbarFragment extends Fragment {

    View btnNew, tabFeeds, tabNotes, tabSearch, tabMe;
    View[] tabs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tabbar, null);

        btnNew = view.findViewById(R.id.btn_new);
        tabFeeds = view.findViewById(R.id.tab_feeds);
        tabNotes = view.findViewById(R.id.tab_notes);
        tabSearch = view.findViewById(R.id.tab_search);
        tabMe = view.findViewById(R.id.tab_me);


        tabs = new View[]{
                tabFeeds, tabNotes, tabSearch, tabMe
        };

        for (final View tab : tabs) {
            tab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onTabClicked(tab);
                }
            });
        }

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), NewArticleActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
            }
        });

        return view;
    }

    public static interface OnTabSelectedListener {
        void onTabSelected(int index);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    OnTabSelectedListener onTabSelectedListener;

    public void setSelectTab(int index){
        onTabClicked(tabs[index]);
    }

    void onTabClicked(View tab) {
        int selectedIndex = -1;
        for (int i = 0; i < tabs.length; i++) {
            View otherTab = tabs[i];
            if (otherTab == tab) {
                otherTab.setSelected(true);
                selectedIndex = i;

            } else
                otherTab.setSelected(false);
        }
        if (onTabSelectedListener != null && selectedIndex >= 0) {
            onTabSelectedListener.onTabSelected(selectedIndex);
        }

    }
}
