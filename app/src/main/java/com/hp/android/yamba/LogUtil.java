package com.hp.android.yamba;

import android.util.Log;

public class LogUtil {

    private static String getTag(Object object) {
        return object.getClass().getSimpleName();
    }

    public static void d(Object object, String message) {
        Log.d(getTag(object), message);
    }

    public static void wtf(Object object, String message) {
        Log.wtf(getTag(object), message);
    }

    public static void wtf(Object object, String message, Throwable e) {
        Log.wtf(getTag(object), message, e);
    }
}
