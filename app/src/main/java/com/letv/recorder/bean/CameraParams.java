//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.bean;

import java.io.Serializable;

public class CameraParams implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int default_preview_width = 640;
    public static final int default_preview_height = 480;
    public static final int min_fps = 15000;
    public static final int defaut_fps = 24000;
    private int width = 640;
    private int height = 480;
    private int cameraId = 0;
    private String flashMode = "off";
    private String whiteBalance = "auto";
    private String sceneMode = "auto";
    private int previewFormat = 17;
    private int pictureFormat = 256;
    private String focusMode = "continuous-video";
    private int previewOrientation = 0;
    private int[] fps = new int[]{15000, 24000};

    public CameraParams() {
    }

    public int getCameraId() {
        return this.cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getFlashMode() {
        return this.flashMode;
    }

    public void setFlashMode(String flashMode) {
        this.flashMode = flashMode;
    }

    public String getWhiteBalance() {
        return this.whiteBalance;
    }

    public void setWhiteBalance(String whiteBalance) {
        this.whiteBalance = whiteBalance;
    }

    public String getSceneMode() {
        return this.sceneMode;
    }

    public void setSceneMode(String sceneMode) {
        this.sceneMode = sceneMode;
    }

    public int getPreviewFormat() {
        return this.previewFormat;
    }

    public String getFocusMode() {
        return this.focusMode;
    }

    public void setFocusMode(String focusMode) {
        this.focusMode = focusMode;
    }

    public int getPreviewOrientation() {
        return this.previewOrientation;
    }

    public void setPreviewOrientation(int previewOrientation) {
        this.previewOrientation = previewOrientation;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getFps() {
        return this.fps;
    }

    public void setFps(int[] fps) {
        this.fps = fps;
    }
}
