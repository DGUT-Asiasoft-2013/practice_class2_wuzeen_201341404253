package com.dgut.firstexam.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.util.MD5;
import com.dgut.firstexam.api.entity.User;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录界面 实现账号密码的POST请求 得到返回的用户信息
 */
public class LoginActivity extends FragmentActivity {

    SimpleTextInputCellFragment account;
    SimpleTextInputCellFragment password;
    Button login, register;
    TextView recover;
    ProgressDialog progressDialog;
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
                loginHttpRequest();
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

    private void loginHttpRequest() {
        if (!isInputCorrect()) {
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = Server.getSharedClient();

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", account.getText())
                .addFormDataPart("passwordHash", MD5.getMD5(password.getText()));//密码加密

        Request request = Server.requestBuliderWithApi("login")
                .post(multipartBuilder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
               LoginActivity.this.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                LoginActivity.this.onResponse(call,response);
            }
        });
    }

    public void onFailure(Call call, final IOException e) {
        progressDialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(LoginActivity.this,e==null?"用户密码不正确":e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResponse(Call call, final Response response) throws IOException {
        progressDialog.dismiss();
        final String result = response.body().string();
        if (!result .equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            final User user = mapper.readValue(result, User.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登录成功")
                            .setMessage("用户名：" + user.getAccount())

                            .setNegativeButton("进入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .show();
                }
            });

        }else {
            onFailure(call,null);
        }

    }



    private boolean isInputCorrect() {
        if (account.getText().equals("")) {
            account.setLayoutError("用户名不能为空");
            password.getText();//清除上一次密码为空的提示
            return false;
        }
        if (password.getText().equals("")) {
            password.setLayoutError("密码不能为空");
            return false;
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        account.setHintText("请输入账号");
        password.setHintText("请输入密码");
        password.setIsPassword(true);
    }
}

