package com.hp.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
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
            } catch (YambaClientException e) {
                LogUtil.wtf(this, "Error Refreshing Timeline", e);
                return;
            }

            final ContentResolver resolver = getContentResolver();
            final ContentValues[] values = new ContentValues[statuses.size()];
            for (int i=0; i < values.length; i++) {
                final YambaClient.Status item = statuses.get(i);
                final ContentValues statusValues = new ContentValues();
                statusValues.put(StatusContract.Columns._ID, item.getId());
                statusValues.put(StatusContract.Columns.CREATED_AT, item.getCreatedAt().getTime());
                statusValues.put(StatusContract.Columns.USER, item.getUser());
                statusValues.put(StatusContract.Columns.MESSAGE, item.getMessage());

                values[i] = statusValues;
            }
            int affected = resolver.bulkInsert(StatusContract.CONTENT_URI, values);
            LogUtil.d(this, "Refresh complete. Received "+affected+" items");
            if (affected > 0) {
                Notification note = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.notification_refresh_title))
                        .setContentText(getResources().getQuantityString(R.plurals.notification_refresh_text, affected, affected))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setTicker(getString(R.string.notification_refresh_title))
                        .setAutoCancel(true)
                        .build();
                mNotificationManager.notify(YambaUtil.NOTE_REFRESH_ID, note);
            }
        }
    }
}
