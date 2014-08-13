package com.hp.android.yamba;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class StatusContract {
    public static final String PATH = "status";
    public static final String AUTHORITY = "com.hp.android.yamba.provider";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .path(PATH)
            .build();

    //We need these MIME values to return from getType()
    public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.hp.android.yamba";
    public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.hp.android.yamba";

    public static final String DEFAULT_SORT_ORDER = Columns.CREATED_AT + " DESC";

    //Implementing BaseColumns to get access to _ID
    public final class Columns implements BaseColumns {
        public static final String CREATED_AT = "created_at";
        public static final String USER = "user";
        public static final String MESSAGE = "message";
    }

    static String getCreateStatement() {
        return String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s TEXT, %s TEXT)",
                PATH, Columns._ID, Columns.CREATED_AT, Columns.USER, Columns.MESSAGE);
    }

    static String getUpgradeStatement(int oldVersion, int newVersion) {
        Log.d("StatusContract", "StatusContract upgraded from " + oldVersion + " to " + newVersion);
        return String.format("DROP TABLE IF EXISTS %s", PATH);
    }
}
