package com.integration.storagedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.integration.storagedemo.util.DateUtil;

public class ShareWriteActivity extends AppCompatActivity implements View.OnClickListener,  AdapterView.OnItemSelectedListener {
    private static final String TAG = "ShareWriteActivity";
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private SharedPreferences mPreferences;
    private boolean isMarried;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_write);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);

        findViewById(R.id.btn_save).setOnClickListener(this);
        initTypeSpinner();

        //初始化sp存储器
        mPreferences = getSharedPreferences("share", MODE_PRIVATE);


    }
    private String[] typeArray = {"未婚", "已婚"};
    private void initTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_select, typeArray);

        Spinner spinner = findViewById(R.id.sp_married);
        spinner.setSelection(0);
        spinner.setEnabled(true);
        spinner.setPrompt("请选择婚姻状态");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        //保存数据
        String age = et_age.getText().toString();
        String height = et_height.getText().toString();
        String name = et_name.getText().toString();
        String weight = et_weight.getText().toString();

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("name", name);
        editor.putInt("age", Integer.parseInt(age));
        editor.putFloat("height", Float.parseFloat(height));
        editor.putFloat("weight", Float.parseFloat(weight));
        editor.putBoolean("isMarried", isMarried);
        editor.putString("update_time", DateUtil.getNowTime());
//        editor.apply();
        editor.commit();

        showToast("数据已写入共享参数");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        isMarried = position != 0;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }


}
