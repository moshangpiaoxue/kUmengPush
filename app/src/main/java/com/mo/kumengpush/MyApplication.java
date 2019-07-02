package com.mo.kumengpush;

import android.app.Application;

import com.mo.kumengpushlib.PushUtil;

/**
 * @ author：mo
 * @ data：2019/5/23:10:28
 * @ 功能：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PushUtil.init(this, "5ce6063b3fc195b245000b70", "Umeng", 1, "406c9c0ce0a350e990a69fdde131f6ba");
        PushUtil.pushSetting(this, R.mipmap.aa,BB.class,null);
    }
}
