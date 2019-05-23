package com.mo.kumengpushlib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

/**
 * @ author：mo
 * @ data：2019/5/23：11:29
 * @ 功能：友盟推送消息接收Service
 */
public class KPushIntentService extends UmengMessageService {
    /**
     * 接收消息
     */
    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            LogPush.Log(message);
            showNotification(context, msg);
        } catch (Exception e) {
            LogPush.Log(e.getMessage());
        }
    }

    /**
     * 开启通知栏
     */
    public void showNotification(Context context, UMessage msg) {
        LogPush.Log("开始推送");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //通道id
        String channelId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = "1";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            //是否在桌面icon右上角展示小红点 
            channel.enableLights(true);
            //小红点颜色   
            channel.setLightColor(Color.RED);
            //是否在久按桌面图标时显示此渠道的通知 
            channel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(false);
        Notification mNotification = builder.build();
        //notification通知栏图标
        mNotification.icon = PushUtil.getIcon();
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.tickerText = msg.ticker;
        //自定义布局
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_view1);
        contentView.setTextViewText(R.id.notification_title, msg.title);
        contentView.setTextViewText(R.id.notification_text, msg.text);
        mNotification.contentView = contentView;
        // 永久在通知栏里
//        mNotification.flags = Notification.FLAG_NO_CLEAR;
        //点完消失
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        //使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法，但是必须定义contentIntent
        mNotification.contentIntent = getClickPendingIntent(context, msg);
        mNotification.deleteIntent = getDismissPendingIntent(context, msg);
        mNotificationManager.notify(PushUtil.notificationCount++, mNotification);
    }

    /**
     * 点击触发意图
     */
    public PendingIntent getClickPendingIntent(Context context, UMessage msg) {
        Intent clickIntent = new Intent();
        clickIntent.setClass(context, PushUtil.getBroadcast());
        clickIntent.putExtra(KNotificationBroadcast.EXTRA_KEY_MSG, msg.getRaw().toString());
        clickIntent.putExtra(KNotificationBroadcast.EXTRA_KEY_ACTION, KNotificationBroadcast.ACTION_CLICK);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, PushUtil.notificationCount++,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return clickPendingIntent;
    }

    /**
     * 消失触发意图（滑动删除触发）
     */
    public PendingIntent getDismissPendingIntent(Context context, UMessage msg) {
        Intent deleteIntent = new Intent();
        deleteIntent.setClass(context, PushUtil.getBroadcast());
        deleteIntent.putExtra(KNotificationBroadcast.EXTRA_KEY_MSG, msg.getRaw().toString());
        deleteIntent.putExtra(KNotificationBroadcast.EXTRA_KEY_ACTION, KNotificationBroadcast.ACTION_DISMISS);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, PushUtil.notificationCount++,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return deletePendingIntent;
    }
}
