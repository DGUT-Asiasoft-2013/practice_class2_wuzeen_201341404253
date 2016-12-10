package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dgut.firstexam.R;
import com.dgut.firstexam.util.MD5;
import com.dgut.firstexam.fragment.InputCell.PictrueInputCellFragment;
import com.dgut.firstexam.fragment.InputCell.SimpleTextInputCellFragment;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

    SimpleTextInputCellFragment account;
    SimpleTextInputCellFragment password;
    SimpleTextInputCellFragment passwordRepeat;
    SimpleTextInputCellFragment name;
    SimpleTextInputCellFragment email;

    PictrueInputCellFragment pictrue;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_account);
        password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_password);
        passwordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_password2);
        pictrue = (PictrueInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_pictrue);
        name = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_name);
        email = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_email);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();

            }
        });
    }


    private void submit() {

        String accountS = account.getText();
        String passwordS = password.getText();
        String passwordRepeatS = passwordRepeat.getText();
        String emailS = email.getText();
        String nameS = name.getText();

        if (!passwordS.equals(passwordRepeatS)) {
            password.setEidtError("密码不一致");
            return;
        }
        passwordS= MD5.getMD5(passwordS);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("注册中");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", accountS)
                .addFormDataPart("passwordHash", passwordS)
                .addFormDataPart("email", emailS)
                .addFormDataPart("name", nameS);
        if (pictrue.getPngData() != null)
            multipartBuilder.addFormDataPart("avatar", "avatarName"
                    , RequestBody
                            .create(
                                    MediaType.parse("image/png")
                                    , pictrue.getPngData()));

        final Request request = new Request.Builder()
                .url("http://172.27.0.35:8080/membercenter/api/register")
                .post(multipartBuilder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败")
                                .setMessage("原因：" + e.getLocalizedMessage())
                                .setCancelable(true)
                                .setNegativeButton("好", null)
                                .show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("注册成功")
                                    .setMessage("注册信息：" + result)
                                    .setCancelable(true)
                                    .setNegativeButton("马上登陆", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .show();


                        }
                    });

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        account.setHintText("请输入账号");
        password.setHintText("请输入密码");
        password.setIsPassword(true);
        passwordRepeat.setHintText("请再输入一次密码");
        passwordRepeat.setIsPassword(true);
        pictrue.setHintText("更改图片");
        pictrue.setLableText("选择头像");
        email.setHintText("邮箱地址");
        name.setHintText("昵称");
    }

}
