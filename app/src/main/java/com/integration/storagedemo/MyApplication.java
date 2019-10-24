package com.integration.storagedemo;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wongerfeng on 2019/9/26.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    //声明一个静态全局变量实例
    private static MyApplication sApp;

    private HashMap<String, String> mInfoMap = new HashMap<>();

    private static MyApplication getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //应用打开时，给静态实例赋值
        sApp = this;
        Log.i(TAG, "onCreate: ");


    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.i(TAG, "onTerminate: ");
    }

    private Map<Long, Bitmap> mIconMap = new HashMap<>();
}
