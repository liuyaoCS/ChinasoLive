//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import android.content.Context;

public class ReUtils {
    public ReUtils() {
    }

    private static int getReId(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    public static int getLayoutId(Context context, String name) {
        return getReId(context, name, "layout");
    }

    public static int getDrawableId(Context context, String name) {
        return getReId(context, name, "drawable");
    }

    public static int getStringId(Context context, String name) {
        return getReId(context, name, "string");
    }

    public static int getStyleId(Context context, String name) {
        return getReId(context, name, "style");
    }

    public static int getColorId(Context context, String name) {
        return getReId(context, name, "color");
    }

    public static int getArrayId(Context context, String name) {
        return getReId(context, name, "array");
    }

    public static int getId(Context context, String name) {
        return getReId(context, name, "id");
    }

    public static int getDimen(Context context, String name) {
        return getReId(context, name, "dimen");
    }
}
