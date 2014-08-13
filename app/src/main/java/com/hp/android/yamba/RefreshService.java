package com.hp.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;

public class RefreshService extends IntentService {

    public static final int MAX_POSTS = 20;

    public RefreshService() {
        super("RefreshService");
    }

    private YambaClient mYambaClient;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mYambaClient = YambaUtil.getClient(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            List<YambaClient.Status> statuses;
            try {
                LogUtil.d(this, "Getting latest timeline...");
                statuses = mYambaClient.getTimeline(MAX_POSTS);
                LogUtil.d(this, "Refresh complete. Received "+statuses.size()+" items");
            } catch (YambaClientException e) {
                LogUtil.wtf(this, "Error Refreshing Timeline", e);
                return;
            }

            for (YambaClient.Status item : statuses) {
                LogUtil.d(this, item.getMessage());
            }

            Notification note = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.notification_refresh_title))
                    .setContentText(getString(R.string.notification_refresh_text))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setTicker(getString(R.string.notification_refresh_title))
                    .setAutoCancel(true)
                    .build();
            mNotificationManager.notify(YambaUtil.NOTE_REFRESH_ID, note);
        }
    }
}
