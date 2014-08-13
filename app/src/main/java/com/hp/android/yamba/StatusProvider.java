package com.hp.android.yamba;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StatusProvider extends ContentProvider {

    private static final int MATCH_DIR = 1;
    private static final int MATCH_ITEM = 2;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.PATH, MATCH_DIR);
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.PATH + "/#", MATCH_ITEM);
    }

    private StatusDbHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new StatusDbHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_DIR:
                return StatusContract.CONTENT_TYPE_DIR;
            case MATCH_ITEM:
                return StatusContract.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Bad Uri...Go away!");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MATCH_DIR:
                //We're set already
                break;
            case MATCH_ITEM:
                long id = ContentUris.parseId(uri);
                selection = StatusContract.Columns._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Bad Uri...Go away!");
        }

        Cursor cursor = db.query(StatusContract.PATH, projection, selection, selectionArgs,
                null, null, sortOrder);
        //Register for data change notifications
        ContentResolver resolver = getContext().getContentResolver();
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MATCH_DIR:
                long id = db.insertOrThrow(StatusContract.PATH, null, values);
                return ContentUris.withAppendedId(uri, id);
            case MATCH_ITEM:
                throw new IllegalArgumentException("Cannot insert into existing record!");
            default:
                throw new IllegalArgumentException("Bad Uri...Go away!");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
