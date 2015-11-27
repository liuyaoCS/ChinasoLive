//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
    public ScreenUtils() {
    }

    public static int getWight(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getHeight(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static boolean isInRight(Context mContext, int xWeight) {
        return xWeight > getHeight(mContext) * 3 / 4;
    }

    public static boolean isInLeft(Context mContext, int xWeight) {
        return xWeight < getWight(mContext) * 1 / 4;
    }

    public static boolean screenIsLanscape(Context mContext) {
        boolean ret = false;
        switch(mContext.getResources().getConfiguration().orientation) {
            case 1:
                ret = false;
                break;
            case 2:
                ret = true;
        }

        return ret;
    }

    public static int getOrientation(Context mContext) {
        return mContext.getResources().getConfiguration().orientation;
    }

    public static int getImageWidth16_10(int heightPx) {
        return (int)((double)heightPx * 1.6D);
    }

    public static int getImageHeight16_10(int widthPx) {
        return (int)((double)widthPx / 1.6D);
    }

    public static int getImageHeight16_9(int widthPx) {
        return widthPx * 9 / 16;
    }

    public static int getImageHeight7_2(int widthPx) {
        return widthPx * 2 / 7;
    }
}
