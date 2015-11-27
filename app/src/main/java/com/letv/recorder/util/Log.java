//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

public class Log {
    private static boolean logToggle = true;

    public Log() {
    }

    public static void logFilter() {
    }

    public static void d(String tag, String msg) {
        if(isLogToggle()) {
            android.util.Log.d(tag, msg);
        }

    }

    public static void i(String tag, String msg) {
        if(isLogToggle()) {
            android.util.Log.i(tag, msg);
        }

    }

    public static void w(String tag, String msg) {
        if(isLogToggle()) {
            android.util.Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if(isLogToggle()) {
            android.util.Log.e(tag, msg);
        }

    }

    public static void v(String tag, String msg) {
        if(isLogToggle()) {
            android.util.Log.v(tag, msg);
        }

    }

    public static boolean isLogToggle() {
        return logToggle;
    }

    public static void setLogToggle(boolean logToggle) {
        logToggle = logToggle;
    }
}
