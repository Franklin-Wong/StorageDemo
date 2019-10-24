package com.integration.storagedemo.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by Wongerfeng on 2019/10/11.
 */
public class ViewUtil {

    public static int getMaxLength(EditText editText) {
        int length = 0;
        //反射调用隐藏方法
        //获取管理器数组
        InputFilter[] filters = editText.getFilters();
        try {
            for (InputFilter filter :
                filters) {
                Class<? extends InputFilter> aClass = filter.getClass();
                if (aClass.getName().equals("android.text.InputFilter$LengthFilter")) {
                    //获取类的成员变量
                    Field[] fields = aClass.getDeclaredFields();
                    //遍历成员变量
                    for (Field filed :
                            fields) {
                        if (filed.getName().equals("mMax")) {
                            filed.setAccessible(true);
                            length = (int) filed.get(filter);

                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     *
     * @param activity
     * @param view
     */
    public static void hideOneInputMethod(Activity activity, EditText view) {
        //从系统获取输入键盘管理器
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //关闭管理器
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
