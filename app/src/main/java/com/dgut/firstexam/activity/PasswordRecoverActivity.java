package com.dgut.firstexam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.dgut.firstexam.R;
import com.dgut.firstexam.api.Server;
import com.dgut.firstexam.fragment.PasswordRecoverStep1Fragment;
import com.dgut.firstexam.fragment.PasswordRecoverStep2Fragment;
import com.dgut.firstexam.util.MD5;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class PasswordRecoverActivity extends Activity {
    PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
    PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();
    String email;
    String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);

        // 设置第一步的Fragment
        setStep1Fragment();
    }

    private void setStep1Fragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();

        step1.setNextListener(new PasswordRecoverStep1Fragment.goStep2Listener() {
            @Override
            public void goStep2() {

                setStep2Fragment();
            }
        });


    }



    private void setStep2Fragment() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_right,
                        R.animator.slide_out_left,
                        R.animator.slide_in_left,
                        R.animator.slide_out_right)
                .replace(R.id.container, step2)
                .addToBackStack(null)
                .commit();
        email = step1.getEmail();
        step2.setPasswordRecoverListener(new PasswordRecoverStep2Fragment.PasswordRecoverListener() {
            @Override
            public void passwordRecover() {

                newPassword = step2.getNewPassword();
                if (newPassword != null) {

                    submit();
                }
            }
        });
    }



    ProgressDialog progressDialog;
    private void submit() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("修改中");
        progressDialog.setCancelable(false);
        progressDialog.show();
        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .addFormDataPart("email", email)
                .addFormDataPart("passwordHash", MD5.getMD5(newPassword));//密码加密
        final Request request = Server.requestBuliderWithApi("passwordRecover")
                .post(multipartBuilder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PasswordRecoverActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                final String result=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( Boolean.valueOf(result)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PasswordRecoverActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        }
                        else{
                            Toast.makeText(PasswordRecoverActivity.this,"邮箱不存在！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
