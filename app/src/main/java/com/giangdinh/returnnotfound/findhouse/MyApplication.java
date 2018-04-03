package com.giangdinh.returnnotfound.findhouse;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
    }
}
