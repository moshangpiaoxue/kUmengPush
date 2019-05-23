package com.mo.kumengpushlib;

/**
 * @ author：mo
 * @ data：2019/5/23:9:51
 * @ 功能：
 */
public class LogPush {
    private static boolean isDebug = true;

    public static void isDebug(boolean isDebug) {
        LogPush.isDebug = isDebug;
    }

    public static void Log(String str) {
        if (isDebug) {
            android.util.Log.i("友盟推送", str);
        }
    }
}
