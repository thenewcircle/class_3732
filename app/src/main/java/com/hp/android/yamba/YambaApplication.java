package com.hp.android.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String GCM_SENDER_ID = "836421038004";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(this, "Application Created");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        //Schedule the initial alarm
        rescheduleRefreshAlarm(this, preferences);

        GCMRegistrationTask task = new GCMRegistrationTask();
        task.execute();
    }

    private class GCMRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(YambaApplication.this);
            String registrationId;
            try {
                registrationId = gcm.register(GCM_SENDER_ID);
                LogUtil.d(this, "GCM Registration ID: "+registrationId);
            } catch (IOException e) {
                LogUtil.wtf(this, "Unable to register with GCM", e);
                return false;
            }

            //Send registration id to the application server
            AndroidHttpClient client = AndroidHttpClient.newInstance("YambaApplication");
            HttpGet request = new HttpGet("http://yamba-push-monitor.herokuapp.com/subscribe/" + registrationId);
            try {
                HttpResponse response = client.execute(request);
                BasicResponseHandler handler = new BasicResponseHandler();
                String result = handler.handleResponse(response);
                LogUtil.d(this, "Push Registration: "+result);
            } catch (IOException e) {
                LogUtil.wtf(this, "Unable to register with app server", e);
                return false;
            } finally {
                client.close();
            }

            return true;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final String refreshKey = getString(R.string.pref_key_refresh);
        if (TextUtils.equals(refreshKey, key)) {
            //Reschedule our refresh alarm
            rescheduleRefreshAlarm(this, sharedPreferences);
        }
    }

    public static void rescheduleRefreshAlarm(Context context, SharedPreferences preferences) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        final String refreshKey = context.getString(R.string.pref_key_refresh);
        long interval = Long.parseLong(preferences.getString(refreshKey, "0"));

        PendingIntent trigger = PendingIntent.getService(context, 0,
                new Intent(context, RefreshService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        if (interval == 0) {
            //Cancel the refresh alarm
            Log.d("YambaApplication", "Canceling refresh alarm");
            manager.cancel(trigger);
            return;
        }

        Log.d("YambaApplication", "Scheduling refresh alarm for "+interval+"ms");
        manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, interval, trigger);
    }
}
