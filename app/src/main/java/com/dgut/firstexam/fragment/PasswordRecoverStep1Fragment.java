package com.dgut.firstexam.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.InputCell.BaseInputCelllFragment;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;

/**
 * Created by Administrator on 2016/12/3.
 */

public class PasswordRecoverStep1Fragment extends Fragment {

    SimpleTextInputCellFragment email;
    Button next;
    View view;
    private goStep2Listener mGoStep2Listenerk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_password_recover_step1, container, false);
            email = (SimpleTextInputCellFragment) getChildFragmentManager().findFragmentById(R.id.fragment_email);
            next = (Button) view.findViewById(R.id.next);

        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        email.setHintText("请输入邮箱地址");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoStep2Listenerk.goStep2();
            }
        });

    }

    //加载监听接口
    public static interface goStep2Listener {
        public void goStep2();
    }
    public void setNextListener(goStep2Listener listener) {
        mGoStep2Listenerk = listener;
    }
}
