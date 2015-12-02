//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.content.Context;
import android.view.SurfaceView;
import com.letv.recorder.bean.AudioParams;
import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.PublishListener;
import com.letv.recorder.callback.VideoFeedingListener;
import com.letv.recorder.controller.AudioRecordDevice;
import com.letv.recorder.controller.LetvRecorderJNIWrapper;
import com.letv.recorder.controller.VideoRecordDevice;
import com.letv.recorder.util.Log;
import com.letv.recorder.util.StreamUtil;
import com.letv.whatslive.jni.LetvRecorderJNI.AudioSampleFormat;
import com.letv.whatslive.jni.LetvRecorderJNI.VideoFrameFormat;

public class Publisher {
    private static Publisher instance = new Publisher();
    private static final String TAG = "CameraView2";
    private static final int video_bitrate = 600000;
    private static final int audio_bitrate = 128000;
    private int errorCode = 0;
    private SurfaceView cameraView;
    private static AudioRecordDevice audioRecordDevice;
    private static VideoRecordDevice videoRecordDevice;
    private static LetvRecorderJNIWrapper letR;
    private CameraParams cameraParams;
    private AudioParams audioParams;
    private String url = "";
    protected Context context;
    protected StreamUtil streamUtil;
    private PublishListener publishListener;
    private VideoFeedingListener feedingListener = new VideoFeedingListener() {
        public void onFeeding(int code, String msg) {
            Log.d("CameraView2", "[feeding] code:" + code);
        }
    };

    public Publisher() {
    }

    public static Publisher getInstance() {
        return instance;
    }

    public void stopPublish() {
        if(getAudioRecordDevice() != null) {
            getAudioRecordDevice().stop();
        }

        if(getRecorder() != null) {
            Log.d("CameraView2", "[==== com.recorder close]");
            getRecorder().close();
            Log.d("CameraView2", "[==== com.recorder close finish]");
            getRecorder().release();
            Log.d("CameraView2", "[==== com.recorder finish]");
        }

    }

    public int publish() {
        (new Thread(new Runnable() {
            public void run() {
                Publisher.letR.useAudio(true);
                if(Publisher.letR.open(Publisher.this.getUrl()) < 0) {
                    if(Publisher.this.publishListener != null) {
                        Publisher.this.publishListener.onPublish(100, (String)null);
                    }
                } else {
                    Publisher.audioRecordDevice.start();
                    if(Publisher.this.publishListener != null) {
                        Publisher.this.publishListener.onPublish(101, (String)null);
                    }
                }

            }
        }, "[pThread]")).start();
        return 0;
    }

    public void setPublishListener(PublishListener publishListener) {
        this.publishListener = publishListener;
    }

    public void initPublisher(Context context) {
        this.context = context;
        getRecorder();
        this.setCameraParams(new CameraParams());
        this.videoRecordDevice=new VideoRecordDevice(this.getContext(), this.getCameraParams());
       // setVideoRecordDevice(new VideoRecordDevice(this.getContext(), this.getCameraParams()));
        this.setAudioParams(new AudioParams());
        this.audioRecordDevice=new AudioRecordDevice(this.getAudioParams());
        //setAudioRecordDevice(new AudioRecordDevice(this.getAudioParams()));
        this.letR=new LetvRecorderJNIWrapper();
        //setRecorder(new LetvRecorderJNIWrapper());
        getVideoRecordDevice().setLetvRecorder(getRecorder());
        getAudioRecordDevice().setLetvRecorder(getRecorder());
        getVideoRecordDevice().setVideoFeedingListener(this.feedingListener);
    }

    private void initRecorderParams(SurfaceView cv) {
        this.streamUtil = new StreamUtil(this.context, this.getCameraParams().getWidth(), this.getCameraParams().getHeight(), cv);
        getVideoRecordDevice().setStreamUtils(this.streamUtil);
        getRecorder().setAudioParams(AudioSampleFormat.AudioSampleFormatS16.ordinal(), this.getAudioParams().getnChannels(), (long)this.getAudioParams().getSampleRateInHz(), 128000L);
        getRecorder().setVideoParams(VideoFrameFormat.VideoFrameFormatRGBA.ordinal(), this.streamUtil.getCroppedVideoWidth(), this.streamUtil.getCroppedVideoHeight(), 600000L);
    }

    public static AudioRecordDevice getAudioRecordDevice() {
        return audioRecordDevice;
    }

    public static VideoRecordDevice getVideoRecordDevice() {
        return videoRecordDevice;
    }

    public static void setVideoRecordDevice(VideoRecordDevice videoRecordDevice) {
        videoRecordDevice = videoRecordDevice;
    }

    public static void setAudioRecordDevice(AudioRecordDevice audioRecordDevice) {
        audioRecordDevice = audioRecordDevice;
    }

    public SurfaceView getCameraView() {
        return this.cameraView;
    }

    public void setCameraView(SurfaceView cameraView) {
        this.cameraView = cameraView;
        this.initRecorderParams(cameraView);
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static LetvRecorderJNIWrapper getRecorder() {
        return letR;
    }

    public static void setRecorder(LetvRecorderJNIWrapper letR) {
        letR = letR;
    }

    public CameraParams getCameraParams() {
        return this.cameraParams;
    }

    public void setCameraParams(CameraParams cameraParams) {
        this.cameraParams = cameraParams;
    }

    public AudioParams getAudioParams() {
        return this.audioParams;
    }

    public void setAudioParams(AudioParams audioParams) {
        this.audioParams = audioParams;
    }
}
