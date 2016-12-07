package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.pages.FeedListFragment;
import com.dgut.firstexam.fragment.pages.MyProFileFragment;
import com.dgut.firstexam.fragment.pages.NoteListFragment;
import com.dgut.firstexam.fragment.pages.SearchPageFragment;
import com.dgut.firstexam.fragment.widgets.MainTabbarFragment;

public class MainActivity extends Activity {

    MainTabbarFragment tabbarFragment;
    FeedListFragment contentFeedList = new FeedListFragment();
    NoteListFragment contentNoteList = new NoteListFragment();
    SearchPageFragment contentSearchPage = new SearchPageFragment();
    MyProFileFragment contentMyProfile = new MyProFileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabbarFragment = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.tabbar);
        tabbarFragment.setOnTabSelectedListener(new MainTabbarFragment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                changeContentPageFragment(index);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        tabbarFragment.setSelectTab(0);
    }

    FragmentTransaction transaction;

    private void changeContentPageFragment(int index) {
        Fragment newFrag = null;
        switch (index) {
            case 0: newFrag = contentFeedList; break;
            case 1: newFrag = contentNoteList; break;
            case 2: newFrag = contentSearchPage; break;
            case 3: newFrag = contentMyProfile; break;

            default:break;
        }

        if(newFrag==null) return;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        +R.animator.slide_in_right,
                        +R.animator.slide_out_left,
                        +R.animator.slide_in_left,
                        +R.animator.slide_out_right)
                .replace(R.id.page_content, newFrag)
                .commit();
    }

}
