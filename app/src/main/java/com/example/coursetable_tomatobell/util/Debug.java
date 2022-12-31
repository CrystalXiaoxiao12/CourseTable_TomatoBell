package com.example.coursetable_tomatobell.util;

import android.util.Log;

public class Debug {
    static final String TAG="xie";
    public static void debug(int flag){
        Log.d(TAG, "debug: "+flag);
    }
    public static void debug(String  str){
        Log.d(TAG, "debug: "+str);
    }
}
