package com.integration.storagedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginForgetActivity";
    private EditText et_password_first;
    private EditText et_password_second;
    private EditText et_verifycode;
    private String mVerifyCode; // 验证码
    private String mPhone; // 手机号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);
        et_password_first = findViewById(R.id.et_password_first);
        et_password_second = findViewById(R.id.et_password_second);
        et_verifycode = findViewById(R.id.et_verifycode);
        findViewById(R.id.btn_verifycode).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        //从上一个页面获取手机号码，添加到本页面
        mPhone = getIntent().getStringExtra("phone");

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {

        //获取验证码
        if (v.getId() == R.id.btn_verifycode) {
            //判断手机号
            if (mPhone == null || mPhone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            //生成随机验证码
            mVerifyCode = String.format("%06d", (int)(Math.random() * 1000000 % 1000000));
            //显示在对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("确定", null);
            builder.setTitle("请记住验证码");
            builder.setMessage("手机号是：" + mPhone + "验证码是"+ mVerifyCode + "; 请您填写验证码");
            builder.create().show();

            //确定按钮
        } else if (v.getId() == R.id.btn_confirm) {
            //修改密码
            String password_first = et_password_first.getText().toString();
            String password_second = et_password_second.getText().toString();
            if (password_first.length() < 6 || password_second.length() < 6) {
                Toast.makeText(this, "请输入正确的新密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password_first.equals(password_second)) {
                Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                return;
            } else if (!et_verifycode.getText().toString().equals(mVerifyCode)) {
                Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //正常，登录完毕
                Intent intent = new Intent();
                intent.putExtra("new _password", password_first);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }
}
