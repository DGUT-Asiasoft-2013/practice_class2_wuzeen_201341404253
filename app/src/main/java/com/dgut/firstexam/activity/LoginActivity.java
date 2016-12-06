package com.dgut.firstexam.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends FragmentActivity {

    SimpleTextInputCellFragment account;
    SimpleTextInputCellFragment password;
    Button login, register;
    TextView recover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_account);
        password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_password);

        login = (Button) findViewById(R.id.username_sign_in_button);
        recover = (TextView) findViewById(R.id.password_recover);

        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
        recover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PasswordRecoverActivity.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        account.setHintText("请输入账号");
        password.setHintText("请输入密码");
    }
}

