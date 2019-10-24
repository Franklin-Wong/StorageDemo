package com.integration.storagedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Map;

public class ShareReadActivity extends AppCompatActivity {
    private static final String TAG = "ShareReadActivity";
    private TextView tv_share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_read);
        tv_share = findViewById(R.id.tv_share);
        readSharedPreferences();

    }

    private void readSharedPreferences() {

        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        String desc = "共享参数中保存的信息如下：";
        Map<String, ?> map = preferences.getAll();
        for (Map.Entry<String, ?> param: map.entrySet()){
            Object value = param.getValue();
            String key = param.getKey();
            if (value instanceof String) {
                String v = preferences.getString(key, "");
                desc = String.format("%s\n %s 所对应的值为 ： %s", desc, key, v);
            } else if (value instanceof Integer) {
                int v = preferences.getInt(key, 0);
                desc = String.format("%s\n %s 所对应的值为 ：%d ",desc, key, v);
            }else if (value instanceof Float) {
                float v = preferences.getFloat(key, 0f);
                desc = String.format("%s\n %s 所对应的值为 ： %f", desc, key, v);
            }else if (value instanceof Long) {
                long v = preferences.getLong(key, 0);
                desc = String.format("%s\n %s 所对应的值为 ：%d ", desc, key, v);
            }else if (value instanceof Boolean) {
                boolean v = preferences.getBoolean(key, false);

                desc = String.format("%s\n %s 所对应的值为 ：%b ", desc, key, v);
            }
        }
        if (map.size() <= 0) {
            desc = "共享参数中保存的信息为空";
        }
        tv_share.setText(desc);
    }
}
