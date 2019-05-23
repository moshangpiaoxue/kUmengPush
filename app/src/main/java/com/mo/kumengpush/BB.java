package com.mo.kumengpush;

import android.content.Context;
import android.content.Intent;

import com.mo.kumengpushlib.KNotificationBroadcast;
import com.umeng.message.entity.UMessage;

/**
 * @ author：mo
 * @ data：2019/5/23:10:35
 * @ 功能：
 */
public class BB extends KNotificationBroadcast {
    @Override
    protected void onClick(Context context, UMessage msg) {
        super.onClick(context, msg);
        if (TestActivity.tag) {
            Intent intentAct = new Intent(context, TestActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentAct);
        }
    }
}
