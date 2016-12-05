package com.dgut.firstexam.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgut.firstexam.R;

/**
 * Created by Administrator on 2016/12/3.
 */

public class WelcomeFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }
}
