package com.hp.android.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmPushReceiver extends BroadcastReceiver {
    public GcmPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        // Filter messages based on message type. It is likely that GCM will be extended in the future
        // with new message types, so just ignore message types you're not interested in, or that you
        // don't recognize.
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            LogUtil.wtf(this, "GCM Error");
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            LogUtil.wtf(this, "GCM Deleted");
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            LogUtil.d(this, "GCM Tickle Received");
            //Trigger the refresh service
            Intent refreshIntent = new Intent(context, RefreshService.class);
            context.startService(refreshIntent);
        }
    }
}
