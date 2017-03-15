package com.pm.testresourceprovider;

import android.app.Application;

import com.pm.skinlibrary.skin.SkinManager;

/**
 * Created by puming on 2017/3/11.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
