package com.integration.storagedemo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.integration.storagedemo.util.ViewUtil;

public class LoginShareActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginShareActivity";
    private RadioGroup rg_login;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private EditText et_phone;
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private CheckBox ck_remember;
    //声明共享参数对象
    private SharedPreferences mShared;
    //页面跳转的的请求代码
    private int mRequestCode = 0;
    //用户类型
    private int mType = 0;
    //是否记住密码
    private boolean bRemember = false;
    //默认密码
    private String password = "111111";
    //验证码
    private String mVerifyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_share);
        rg_login = findViewById(R.id.rg_login);
        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);
        et_phone = findViewById(R.id.et_phone);
        tv_password = findViewById(R.id.tv_password);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        ck_remember = findViewById(R.id.ck_remember);

        rg_login.setOnCheckedChangeListener(new RadioListener());
        ck_remember.setOnCheckedChangeListener(new CheckListener());
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone));
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
        btn_forget.setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        initTypeSpinner();
        //从XML存储中获取共享参数对象
        mShared = getSharedPreferences("share_login", MODE_PRIVATE);
        //获取存储的账号与密码
        String phone = mShared.getString("phone", "");
        String password = mShared.getString("password", "");
        et_password.setText(password);
        et_phone.setText(phone);

    }

    private void initTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_select, typeArray);
        //
        adapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner spinner = findViewById(R.id.sp_type);
        spinner.setOnItemSelectedListener(new TypeSelectedListener());
        spinner.setPrompt("请选择用户类型");
        spinner.setEnabled(true);
        spinner.setAdapter(adapter);
        spinner.setSelection(mType);

    }

    private String[] typeArray = {"个人用户", "公司用户"};


    private class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_password) {
                tv_password.setText("密码");
                et_password.setHint("请您输入密码");
                btn_forget.setText("忘记密码");
                ck_remember.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rb_verifycode) {
                tv_password.setText("验证码 ：");
                et_password.setHint("请您输入验证码");
                btn_forget.setText("获取验证码");
                ck_remember.setVisibility(View.INVISIBLE);
            }

        }
    }

    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.ck_remember) {
                ck_remember.setChecked(isChecked);
            }

        }
    }

    private class HideTextWatcher implements TextWatcher {

        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;

        public HideTextWatcher(EditText editText) {
            mView = editText;
            mMaxLength = ViewUtil.getMaxLength(editText);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            //当输入文字完成，自动隐藏输入键盘
            if (s.length() == 11 && 11 == mMaxLength || s.length() == 6 && mMaxLength == 6) {
                ViewUtil.hideOneInputMethod(LoginShareActivity.this, mView);
            }

        }
    }

    private class TypeSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //确定选中的项目
            mType = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {

        //获取输入的手机号码
        String phone = et_phone.getText().toString();
        //登录
        if (v.getId() == R.id.btn_login) {
            //判断 手机号码错误
            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (rb_password.isChecked()) {
                //判断
                if (!et_password.getText().toString().equals(password)) {
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    loginSuccess();
                }

            } else if (rb_verifycode.isChecked()) {
                //判断验证码是否正确
                if (!et_password.getText().toString().equals(mVerifyCode)) {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                }else {
                    loginSuccess();
                }
            }

        } else if (v.getId() == R.id.btn_forget) {
            //忘记密码
            // 手机号码错误
            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            } else if (rb_password.isChecked()) {
                //忘记密码，进入修改密码界面
                Intent intent = new Intent(LoginShareActivity.this, LoginForgetActivity.class);
                intent.putExtra("phone", phone);
                startActivityForResult(intent, mRequestCode);
            } else if (rb_verifycode.isChecked()) {
                //接收验证码选项
                //生成6位随机验证码
                mVerifyCode = String.format("%06d", (int)(Math.random() * 1000000 % 1000000));

                //弹出对话框。提示用户验证码信息
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请您记住验证码");
                builder.setPositiveButton("好的", null);
                builder.setMessage("手机：" + phone + "的验证码：" + mVerifyCode + ",请您记住");
                builder.create().show();

            }
        }
    }

    private void loginSuccess() {
        //描述信息
        String desc = String.format("您登录的手机号是:%s, 账号类型是：%s。恭喜你通过登录验证，点击“确定”按钮返回上个页。",
                et_phone.getText().toString(), typeArray[mType]);

        //弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(desc);
        builder.setTitle("登录成功");
        builder.setNegativeButton("我再看看", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        //显示对话框
        builder.create().show();
        //是否保存
        if (bRemember) {
            //获取SP XML存储器， 设置模式为 私有
            SharedPreferences.Editor editor = mShared.edit();
            editor.putString("phone", et_phone.getText().toString());
            editor.putString("paassword", et_password.getText().toString());
            editor.apply();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //清空输入信息
        tv_password.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断请求码
        if (requestCode == mRequestCode && data != null) {
            password = data.getStringExtra("new _password");

        }
    }
}
