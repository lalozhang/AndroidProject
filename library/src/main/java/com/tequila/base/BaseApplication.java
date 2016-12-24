package com.tequila.base;

import android.app.Application;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2016/10/2.
 */
public class BaseApplication extends Application {

    private static WeakReference<BaseApplication> instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new WeakReference<BaseApplication>(this);

    }

    public static BaseApplication getContext() {
        return instance.get();
    }
}
