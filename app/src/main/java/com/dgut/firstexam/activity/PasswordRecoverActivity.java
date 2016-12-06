package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.PasswordRecoverStep1Fragment;
import com.dgut.firstexam.fragment.PasswordRecoverStep2Fragment;

public class PasswordRecoverActivity extends Activity {
    PasswordRecoverStep1Fragment step1;
    PasswordRecoverStep2Fragment step2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);

        // 设置第一步的Fragment
        setStep1Fragment();
    }

    private void setStep1Fragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (step1 == null)
            step1 = new PasswordRecoverStep1Fragment();
        transaction.replace(R.id.container, step1);
        transaction.commit();
        step1.setNextListener(new PasswordRecoverStep1Fragment.goStep2Listener() {
            @Override
            public void goStep2() {
                setStep2Fragment();
            }
        });
    }

    private void setStep2Fragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (step2 == null)
            step2 = new PasswordRecoverStep2Fragment();
        transaction.setCustomAnimations(
                +R.animator.slide_in_right,
                +R.animator.slide_out_left,
                +R.animator.slide_in_left,
                +R.animator.slide_out_right);
        transaction.replace(R.id.container, step2);
        transaction.addToBackStack(null);

        transaction.commit();

        step2.setGoLoginListener(new PasswordRecoverStep2Fragment.GoLoginListener() {
            @Override
            public void backToLogin() {
                finish();
            }
        });
    }
}
