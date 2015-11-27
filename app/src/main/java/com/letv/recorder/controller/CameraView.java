//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class CameraView extends SurfaceView implements Callback {
    private static final String TAG = "CameraView2";
    private SurfaceHolder mSurfaceHolder = null;

    public CameraView(Context context) {
        super(context);
        this.viewInit();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.viewInit();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.viewInit();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mSurfaceHolder = holder;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mSurfaceHolder = null;
    }

    private void viewInit() {
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(3);
    }

    public SurfaceHolder getCurentSurfaceHolder() {
        return this.mSurfaceHolder;
    }
}
