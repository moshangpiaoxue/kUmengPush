package com.mo.kumengpushlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * @ author：mo
 * @ data：2019/5/23:9:25
 * @ 功能：友盟推送工具类
 */
public class PushUtil {
    @SuppressLint("StaticFieldLeak")
    private static PushAgent mPushAgent;
    /**
     * 设备Token
     */
    private static String mDeviceToken;
    /**
     * 通知id
     */
    public static int notificationCount = 1;
    /**
     * 通知点击消失事件监听广播
     */
    private static Class<KNotificationBroadcast> notificationBroadcastClass = null;
    /**
    * 通知栏小图标（后期把通知栏封了）
    */
    private static int icon;


    /**
     * 初始化友盟配置（所有友盟的功能都要执行此操作，如：分享、推送等）
     *
     * @param context       上下文
     * @param appKey        应用申请的Appkey
     * @param channel       渠道名称 （"Umeng"）
     * @param type          设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；
     *                      传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
     * @param messageSecret Push推送业务的secret 填充Umeng Message Secret对应信息
     */
    public static void init(Context context, String appKey, String channel, int type, String messageSecret) {
        UMConfigure.init(context, appKey, TextUtils.isEmpty(channel) ? "Umeng" : channel, type == UMConfigure.DEVICE_TYPE_BOX ? UMConfigure.DEVICE_TYPE_BOX : UMConfigure.DEVICE_TYPE_PHONE, messageSecret);
    }

    /**
     * 设置推送配置
     *
     * @param context                    上下文
     * @param notificationBroadcastClass 通知点击/消失事件监听的广播，（必须继承NotificationBroadcast.class）
     */
    public static void setting(Context context, Class<?> notificationBroadcastClass) {
        try {
            PushUtil.notificationBroadcastClass = (Class<KNotificationBroadcast>) notificationBroadcastClass;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //获取消息推送代理实例
        mPushAgent = PushAgent.getInstance(context);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                mDeviceToken = deviceToken;

                LogPush.Log("注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogPush.Log("注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        //获取DeviceToken  递归
        if (TextUtils.isEmpty(mDeviceToken)) {
            mDeviceToken= mPushAgent.getRegistrationId();
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    int count = 0;
//                    if (null ==  MyApplication.mydeviceToken && count < 10) {
//                        String temp = mPushAgent.getRegistrationId();
//                        if (temp == null || temp.isEmpty()) {
//                            count++;
//                        } else {
//                            MyApplication.mydeviceToken = temp;
//                            count = 0;
//                        }
//                    } else {
//                        count = 0;
//                        cancel();
//                    }
//                }
//            }, 0, 800);
        }
        mPushAgent.setNotificaitonOnForeground(true);
        //设置接收通知服务
        mPushAgent.setPushIntentServiceClass(KPushIntentService.class);
    }

    public static PushAgent getmPushAgent() {
        return mPushAgent;
    }
    public static int getIcon() {
        return icon == 0 ? R.mipmap.aa : icon;
    }

    public static void setIcon(int icon) {
        PushUtil.icon = icon;
    }

    public static String getmDeviceToken() {
        return TextUtils.isEmpty(mDeviceToken) ? mPushAgent.getRegistrationId() : mDeviceToken;
    }

    public static Class<KNotificationBroadcast> getBroadcast() {
        return notificationBroadcastClass == null ? KNotificationBroadcast.class : notificationBroadcastClass;
    }

}
