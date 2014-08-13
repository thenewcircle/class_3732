package com.hp.android.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatusDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "timeline.db";
    private static final int DB_VERSION = 1;

    public StatusDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StatusContract.getCreateStatement());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(StatusContract.getUpgradeStatement(oldVersion, newVersion));
        onCreate(db);
    }
}
