package com.hp.android.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(this, "Application Created");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        //Schedule the initial alarm
        rescheduleRefreshAlarm(this, preferences);
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
