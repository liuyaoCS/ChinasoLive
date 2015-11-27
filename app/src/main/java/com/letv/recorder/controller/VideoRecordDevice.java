//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.VideoFeedingListener;
import com.letv.recorder.controller.ILetvRecorder;
import com.letv.recorder.controller.RecordDevices;
import com.letv.recorder.util.ScreenUtils;
import com.letv.recorder.util.StreamUtil;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoRecordDevice extends RecordDevices implements Observer {
    private static final String TAG = "CameraView2";
    private Camera mCamera = null;
    private SurfaceHolder mSurfaceHolder = null;
    private CameraParams cameraParams;
    private Context context;
    private StreamUtil streamUtil;
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    private ILetvRecorder letvRecorder;
    private VideoFeedingListener feedingListener;
    byte[] pdata = new byte[460800];
    private final int FREAME_RATE = 15;
    private final int PER_FREAME_TIME = 66666;
    private long last_pts = 0L;
    private long cur_pts = 0L;
    PreviewCallback cb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            VideoRecordDevice.this.feedingFrame(data);
        }
    };
    private boolean isRecorder = false;
    private byte[] buffer;
    private Runnable feedingCommand = new Runnable() {
        public void run() {
            Class var1 = VideoRecordDevice.class;
            synchronized(VideoRecordDevice.class) {
                VideoRecordDevice.this.feedingFrame(VideoRecordDevice.this.buffer);
            }
        }
    };

    public VideoRecordDevice(Context context, CameraParams cameraParams) {
        this.context = context;
        this.cameraParams = cameraParams;
    }

    public void setLetvRecorder(ILetvRecorder letvRecorder) {
        this.letvRecorder = letvRecorder;
    }

    public void setStreamUtils(StreamUtil streamUtil) {
        this.streamUtil = streamUtil;
    }

    public void setVideoFeedingListener(VideoFeedingListener feedingListener) {
        this.feedingListener = feedingListener;
    }

    public void bindingSurface(SurfaceHolder mSurfaceHolder) {
        this.mSurfaceHolder = mSurfaceHolder;
    }

    private boolean chooseVideoDevice() {
        Log.i("CameraView2", "going into initCamera");
        if(this.getCamera() != null) {
            try {
                Parameters e = this.getCamera().getParameters();
                this.buildDefaultParams(e);
                this.getCamera().setParameters(e);
                this.getCamera().setPreviewCallback(this.cb);
                this.getCamera().startPreview();
                return true;
            } catch (Exception var2) {
                var2.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private void buildDefaultParams(Parameters parameters) {
        this.setDisplayOrientation(parameters);
        this.setFlashMode(parameters);
        this.setWhiteBalance(parameters);
        this.setSceneMode(parameters);
        this.setFocusMode(parameters);
        parameters.setPreviewFormat(this.cameraParams.getPreviewFormat());
        this.setPreViewSize(parameters);
        Log.d("CameraView2", "==================================================");
        this.setFps(parameters);
        Log.d("CameraView2", "==================================================");
    }

    private void setSceneMode(Parameters parameters) {
        List supportedSceneModes = parameters.getSupportedSceneModes();
        if(supportedSceneModes != null) {
            Log.i("CameraView2", "===========================================");

            for(int i = 0; i < supportedSceneModes.size(); ++i) {
                Log.i("CameraView2", "[support SceneMode] " + (String)supportedSceneModes.get(i));
            }

            Log.i("CameraView2", "===========================================");
            if(supportedSceneModes.contains("auto")) {
                this.cameraParams.setSceneMode("auto");
                parameters.setSceneMode(this.cameraParams.getSceneMode());
            }
        }

    }

    private void setWhiteBalance(Parameters parameters) {
        List supportedWhiteBalance = parameters.getSupportedWhiteBalance();
        if(supportedWhiteBalance != null) {
            Log.i("CameraView2", "===========================================");

            for(int i = 0; i < supportedWhiteBalance.size(); ++i) {
                Log.i("CameraView2", "[support WhiteBalance] " + (String)supportedWhiteBalance.get(i));
            }

            Log.i("CameraView2", "===========================================");
            if(supportedWhiteBalance.contains("auto")) {
                this.cameraParams.setWhiteBalance("auto");
                parameters.setWhiteBalance(this.cameraParams.getWhiteBalance());
            }
        }

    }

    private void setFlashMode(Parameters parameters) {
        List supportedFlashModes = parameters.getSupportedFlashModes();
        if(supportedFlashModes != null) {
            Log.i("CameraView2", "===========================================");

            for(int i = 0; i < supportedFlashModes.size(); ++i) {
                Log.i("CameraView2", "[support flashMode] " + (String)supportedFlashModes.get(i));
            }

            Log.i("CameraView2", "===========================================");
        }

    }

    private void setDisplayOrientation(Parameters parameters) {
        if(!ScreenUtils.screenIsLanscape(this.context)) {
            parameters.set("orientation", "portrait");
            this.cameraParams.setPreviewOrientation(90);
        } else {
            parameters.set("orientation", "landscape");
            this.cameraParams.setPreviewOrientation(0);
        }

        this.getCamera().setDisplayOrientation(90);
    }

    private void setFocusMode(Parameters parameters) {
        List supportedFocusModes = parameters.getSupportedFocusModes();
        Log.i("CameraView2", "===========================================");

        for(int i = 0; i < supportedFocusModes.size(); ++i) {
            Log.i("CameraView2", "[support focusMode] " + (String)supportedFocusModes.get(i));
        }

        Log.i("CameraView2", "===========================================");
        if(supportedFocusModes.contains("continuous-video")) {
            this.cameraParams.setFocusMode("continuous-video");
            parameters.setFocusMode(this.cameraParams.getFocusMode());
        } else if(supportedFocusModes.contains("auto")) {
            this.cameraParams.setFocusMode("auto");
            parameters.setFocusMode(this.cameraParams.getFocusMode());
        }

    }

    private void setPreViewSize(Parameters parameters) {
        List previewSizes = this.getCamera().getParameters().getSupportedPreviewSizes();
        List previewFormats = this.getCamera().getParameters().getSupportedPreviewFormats();
        Size psize = null;
        Log.d("CameraView2", "==================================================");
        Log.d("CameraView2", "maxWidth:" + ((Size)previewSizes.get(0)).width + ",maxHeight:" + ((Size)previewSizes.get(0)).height);

        for(int pf = 0; pf < previewSizes.size(); ++pf) {
            psize = (Size)previewSizes.get(pf);
            Log.w("CameraView2initCamera", "PreviewSize,width: " + psize.width + " height" + psize.height);
        }

        Log.d("CameraView2", "==================================================");
        Integer var7 = null;

        for(int i = 0; i < previewFormats.size(); ++i) {
            var7 = (Integer)previewFormats.get(i);
            Log.w("CameraView2initCamera", "previewformates:" + var7);
        }

        parameters.setPreviewSize(640, 480);
    }

    private void setFps(Parameters parameters) {
        List range = parameters.getSupportedPreviewFpsRange();
        int minRange = ((int[])range.get(0))[0];
        int maxRange = ((int[])range.get(range.size() - 1))[1];
        Log.d("CameraView2", "range:" + range.size() + ",minRange:" + minRange + ",maxRange:" + maxRange);

        for(int j = 0; j < range.size(); ++j) {
            int[] r = (int[])range.get(j);
            Log.d("CameraView2", "=====");

            for(int k = 0; k < r.length; ++k) {
                Log.d("CameraView2", "fps range:" + r[k] + ",k" + k + ",j" + j);
            }
        }

        if(24000 <= maxRange && 24000 >= minRange) {
            this.cameraParams.setFps(new int[]{minRange, 24000});
        } else {
            this.cameraParams.setFps(new int[]{minRange, maxRange});
        }

    }

    private void feedingFrame(byte[] data) {
        if(this.letvRecorder != null && this.streamUtil != null) {
            byte[] tmp = this.streamUtil.dealVideoFrame(data);
            this.letvRecorder.feedingVideoFrame(tmp, (long)tmp.length, System.nanoTime() / 1000L);
        }

    }

    public boolean start() {
        try {
            this.setCamera(Camera.open(this.cameraParams.getCameraId()));
            if(this.mSurfaceHolder != null) {
                if(this.cameraParams.getCameraId() == 0) {
                    if(this.streamUtil != null) {
                        this.streamUtil.setRotateDirection(2);
                    }
                } else if(this.streamUtil != null) {
                    this.streamUtil.setRotateDirection(3);
                }

                this.getCamera().setPreviewDisplay(this.mSurfaceHolder);
                this.isRecorder = this.chooseVideoDevice();
                return this.isRecorder;
            } else {
                return true;
            }
        } catch (IOException var2) {
            var2.printStackTrace();
            this.isRecorder = false;
            return false;
        }
    }

    public void stop() {
        if(this.getCamera() != null) {
            this.getCamera().setPreviewCallback((PreviewCallback)null);
            this.getCamera().stopPreview();
            this.getCamera().release();
            this.setCamera((Camera)null);
            this.isRecorder = false;
        }

        this.pool.shutdown();
    }

    public boolean isRecording() {
        return this.isRecorder;
    }

    public void reset() {
    }

    public void setFlashFlag(boolean flag) {
        if(this.getCamera() != null) {
            Parameters params = this.getCamera().getParameters();
            List supportedFlashModes = params.getSupportedFlashModes();
            if(this.cameraParams != null && this.cameraParams.getCameraId() != 1) {
                if(flag) {
                    if(supportedFlashModes.contains("torch")) {
                        this.cameraParams.setFlashMode("torch");
                    }
                } else if(supportedFlashModes.contains("off")) {
                    this.cameraParams.setFlashMode("off");
                }

                try {
                    params.setFlashMode(this.cameraParams.getFlashMode());
                    this.getCamera().setParameters(params);
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }
        }

    }

    public void switchCamera(int cameraId) {
        this.stop();
        this.cameraParams.setCameraId(cameraId);
        this.start();
    }

    public void release() {
    }

    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle)data;
        int flag = bundle.getInt("flag");
        if(3 == flag) {
            this.setFlashFlag(false);
        } else if(2 == flag) {
            this.setFlashFlag(true);
        }

        if(6 == flag) {
            this.switchCamera(0);
        } else if(7 == flag) {
            this.switchCamera(1);
        }

    }

    public Camera getCamera() {
        return this.mCamera;
    }

    private void setCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }
}
