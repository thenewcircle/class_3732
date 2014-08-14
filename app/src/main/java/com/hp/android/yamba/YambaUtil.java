package com.hp.android.yamba;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.marakana.android.yamba.clientlib.YambaClient;

public class YambaUtil {

    public static final int NOTE_POST_ID = 1337;
    public static final int NOTE_REFRESH_ID = 1338;

    public static YambaClient getClient(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String username = prefs.getString(context.getString(R.string.pref_key_username), "");
        String password = prefs.getString(context.getString(R.string.pref_key_password), "");

        return new YambaClient(username, password);
    }

    public static CharSequence getCreatedAtString(long createdAt) {
        return DateUtils.getRelativeTimeSpanString(createdAt,
                System.currentTimeMillis(), 0);
    }
}
