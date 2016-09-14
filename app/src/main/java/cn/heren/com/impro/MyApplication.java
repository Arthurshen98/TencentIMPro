package cn.heren.com.impro;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.tencent.qalsdk.util.BaseApplication;

import java.util.LinkedList;
import java.util.List;

import cn.heren.com.impro.util.Foreground;


/**
 * Created by Administrator on 2016/8/28.
 */
public class MyApplication extends BaseApplication {
    public static Context mContext;
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
       Foreground.init(this);
        mContext = getApplicationContext();
    }

    @Override
    public Object getAppData(String s) {
        return null;
    }

    /**
     * 获取全局的context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }




}
