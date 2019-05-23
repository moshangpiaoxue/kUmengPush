package com.mo.kumengpushlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.umeng.message.UTrack;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ author：mo
 * @ data：2019/5/23：11:57
 * @ 功能：通知点击/删除事件的接收广播（使用这种方式是因为当快速点击通知的时候会触发两次意图里的操作，
 * 只能用这种方式监听操作，然后加逻辑判断）
 * @ 使用 ：项目里继承此类，复写onClick（）和onDismiss（）方法，在里面写具体的逻辑
 * @ 注意 ：在复写的onClick（）方法里加入判断是否是第二次点击，
 * 比如，点击通知跳界面，在目标activity里加上标识，当activity创建后更改此标识，在onClick（）里判断此标识，就能知道这个点击操作是否已经执行过
 */
public class KNotificationBroadcast extends BroadcastReceiver {
    public static final String EXTRA_KEY_ACTION = "ACTION";
    public static final String EXTRA_KEY_MSG = "MSG";
    public static final int ACTION_CLICK = 10;
    public static final int ACTION_DISMISS = 11;
    public static final int EXTRA_ACTION_NOT_EXIST = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_KEY_MSG);
        int action = intent.getIntExtra(EXTRA_KEY_ACTION, EXTRA_ACTION_NOT_EXIST);
        try {
            UMessage msg = (UMessage) new UMessage(new JSONObject(message));
            switch (action) {
                case ACTION_DISMISS:
                    LogPush.Log("通知消失==" + message);
                    onDismiss(context, msg);
                    UTrack.getInstance(context).setClearPrevMessage(true);
                    UTrack.getInstance(context).trackMsgDismissed(msg);
                    break;
                case ACTION_CLICK:
                    LogPush.Log("点击通知==" + message);
                    onClick(context, msg);
                    UTrack.getInstance(context).setClearPrevMessage(true);
                    UTrack.getInstance(context).trackMsgClick(msg);
                    break;
                default:
                    break;
            }
            //
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息通知点击
     *
     * @param context 上下文
     * @param msg     友盟推送的消息
     */
    protected void onClick(Context context, UMessage msg) {

    }

    /**
     * 消息通知消失（滑动删除触发）
     *
     * @param context 上下文
     * @param msg     友盟推送的消息
     */
    protected void onDismiss(Context context, UMessage msg) {

    }


}
