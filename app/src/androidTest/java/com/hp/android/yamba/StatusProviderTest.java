package com.hp.android.yamba;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

public class StatusProviderTest extends ProviderTestCase2<StatusProvider> {

    public StatusProviderTest() { super(StatusProvider.class, "com.hp.android.yamba.provider"); }

    private MockContentResolver mContentResolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContentResolver = getMockContentResolver();
    }

    private static final int INSERT_COUNT = 3;
    public void testProviderInsert() {
        long now = System.currentTimeMillis();
        ContentValues[] values = new ContentValues[INSERT_COUNT];
        for (int i=0; i < INSERT_COUNT; i++) {
            ContentValues item = new ContentValues();
            item.put(StatusContract.Columns.CREATED_AT, now);
            item.put(StatusContract.Columns.USER, "Fake Student");
            item.put(StatusContract.Columns.MESSAGE, "Fake Message #"+i);

            values[i] = item;
        }

        mContentResolver.bulkInsert(StatusContract.CONTENT_URI, values);


        Cursor result = mContentResolver.query(StatusContract.CONTENT_URI, null, null, null, null);

        assertNotNull("Cursor shouldn't be null", result);
        assertTrue("Cursor shouldn't be empty", result.moveToFirst());
        assertEquals("Cursor should have correct item count", INSERT_COUNT, result.getCount());

        result.close();
    }
}
