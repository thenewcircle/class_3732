package com.hp.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class StatusUpdateService extends IntentService {

    public static final int NOTE_ID = 1337;
    public static final String EXTRA_STATUS = "com.hp.android.yamba.EXTRA_STATUS";

    public StatusUpdateService() {
        super("StatusUpdateService");
    }

    private YambaClient mYambaClient;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mYambaClient = new YambaClient("student", "password");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /** This is called on a background thread */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String status = intent.getStringExtra(EXTRA_STATUS);

            Notification note = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.notification_post_title))
                    .setContentText(getString(R.string.notification_post_text))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setTicker(getString(R.string.notification_post_title))
                    .setOngoing(true)
                    .build();
            mNotificationManager.notify(NOTE_ID, note);

            try {
                mYambaClient.postStatus(status);

                mNotificationManager.cancel(NOTE_ID);
                LogUtil.d(this, "Status Complete");
            } catch (YambaClientException e) {
                Intent callback = new Intent(this, StatusActivity.class);
                callback.putExtra(EXTRA_STATUS, status);

                PendingIntent trigger = PendingIntent.getActivity(this, 0, callback,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                note = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.notification_post_error_title))
                        .setContentText(getString(R.string.notification_post_error_text))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setTicker(getString(R.string.notification_post_error_title))
                        .setContentIntent(trigger)
                        .setAutoCancel(true)
                        .build();

                mNotificationManager.notify(NOTE_ID, note);
                LogUtil.wtf(this, "Error Posting Status", e);
            }
        }
    }
}
