package com.khyzhun.sasha.criminalintent;

import android.os.Build;

public class AndroidVersionHelper {
    public AndroidVersionHelper() {
    }

    static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    static boolean isGingerbreadOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
}