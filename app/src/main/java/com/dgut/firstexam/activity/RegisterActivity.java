package com.dgut.firstexam.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dgut.firstexam.R;
import com.dgut.firstexam.fragment.PictrueInputCellFragment;
import com.dgut.firstexam.fragment.SimpleTextInputCellFragment;

public class RegisterActivity extends Activity {

    SimpleTextInputCellFragment account;
    SimpleTextInputCellFragment password;
    SimpleTextInputCellFragment passwordRepeat;
    PictrueInputCellFragment pictrue;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account= (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_account);
        password= (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_password);
        passwordRepeat= (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_password2);
        pictrue=(PictrueInputCellFragment) getFragmentManager().findFragmentById(R.id.fragment_pictrue);




        register= (Button) findViewById(R.id.register);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setEidtError("提示形式1");
                passwordRepeat.setLayoutError("提示形式2");
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
    }
}
