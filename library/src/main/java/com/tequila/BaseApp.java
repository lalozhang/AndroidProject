package com.tequila;

import android.app.Application;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2016/10/2.
 */
public class BaseApp extends Application {

    private static WeakReference<BaseApp> instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new WeakReference<BaseApp>(this);

    }

    public static BaseApp getContext() {
        return instance.get();
    }
}
