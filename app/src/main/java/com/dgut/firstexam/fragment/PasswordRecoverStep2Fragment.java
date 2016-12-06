package com.dgut.firstexam.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.InputCell.BaseInputCelllFragment;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;

/**
 * Created by Administrator on 2016/12/3.
 */

public class PasswordRecoverStep2Fragment extends Fragment {

    SimpleTextInputCellFragment password, passwordRepeat;
    Button recover;
    private GoLoginListener goLoginListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_recover_step2, container, false);
        password = (SimpleTextInputCellFragment) getChildFragmentManager().findFragmentById(R.id.fragment_password);
        passwordRepeat = (SimpleTextInputCellFragment) getChildFragmentManager().findFragmentById(R.id.fragment_password2);
        recover= (Button) view.findViewById(R.id.recover);
        return view;


    }

    public static interface GoLoginListener {
        void backToLogin();
    }

    public void setGoLoginListener(GoLoginListener goLoginListener) {
        this.goLoginListener = goLoginListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        password.setHintText("请输入新密码");
        passwordRepeat.setHintText("再输入一次密码");

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLoginListener.backToLogin();
            }
        });

    }
}
