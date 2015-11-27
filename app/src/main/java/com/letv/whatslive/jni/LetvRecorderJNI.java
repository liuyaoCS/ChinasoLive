//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.whatslive.jni;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import com.letv.live.camera.Interface.RecordCallbackListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public final class LetvRecorderJNI {
    private static final String TAG = "FFRecorder";
    private RecordCallbackListener mRecordCallbackListener;
    private String mPushUrl;

    static {
        System.loadLibrary("fdk-aac");
        System.loadLibrary("ffmpeg_neon");
        System.loadLibrary("recorder");
        System.loadLibrary("transmitter");
    }

    public LetvRecorderJNI() {
        this.setup();
    }

    public void postNativeError(String event, int parent_code, int child_code) {
        System.out.println("sourceData:postNativeError");
        Log.d("FFRecorder", "Received native event : " + event + " parent code : " + parent_code + " child_code : " + child_code);
        this.saveFile("postNativeError", "Received native event : " + event + " parent code : " + parent_code + " child_code : " + child_code + " pushurl:" + this.mPushUrl + "\n");
        switch(parent_code) {
            case 100:
                (new Timer()).schedule(new TimerTask() {
                    public void run() {
                        LetvRecorderJNI.this.saveFile("getCurrentMaxRwTime", LetvRecorderJNI.this.getCurrentMaxRwTime() + " pushurl:" + LetvRecorderJNI.this.mPushUrl + "\n");
                    }
                }, 1L, 120000L);
            case 0:
            case 1:
            case 102:
            default:
        }
    }

    public void setListener(RecordCallbackListener recordCallbackListener) {
        this.mRecordCallbackListener = recordCallbackListener;
    }

    public void setPushUrl(String pushUrl) {
        this.mPushUrl = pushUrl;
    }

    public native void setup();

    public native void release();

    public native int setVideoOptions(int var1, int var2, int var3, long var4);

    public native int setAudioOptions(int var1, int var2, long var3, long var5);

    public native int open(String var1, Boolean var2);

    public native int close();

    public native int supplyVideoFrame(byte[] var1, long var2, long var4);

    public native int supplyAudioSamples(byte[] var1, long var2, long var4);

    public native int setPushStreamTimeout(int var1);

    public native int getCurrentMaxRwTime();

    private void saveFile(String type, String content) {
        File pathFile = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsLIVE/Log/");
        if(!pathFile.exists()) {
            pathFile.mkdirs();
        }

        File f = new File(pathFile, DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()) + ".log");
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(f, true);
            bw = new BufferedWriter(fw, 2048);
            String e = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()) + "," + type + "\n" + content;
            bw.write(e);
        } catch (IOException var24) {
            System.out.println("写入文件出错");
        } finally {
            if(bw != null) {
                try {
                    bw.flush();
                } catch (IOException var23) {
                    var23.printStackTrace();
                }

                try {
                    bw.close();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }
            }

            if(fw != null) {
                try {
                    fw.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }
            }

        }

    }

    public static enum AudioSampleFormat {
        AudioSampleFormatU8,
        AudioSampleFormatS16,
        AudioSampleFormatS32,
        AudioSampleFormatFLT,
        AudioSampleFormatDBL,
        AudioSampleFormatMax;

        private AudioSampleFormat() {
        }
    }

    public static enum VideoFrameFormat {
        VideoFrameFormatYUV420P,
        VideoFrameFormatYUV420V,
        VideoFrameFormatYUV420F,
        VideoFrameFormatNV12,
        VideoFrameFormatNV21,
        VideoFrameFormatRGB24,
        VideoFrameFormatBGR24,
        VideoFrameFormatARGB,
        VideoFrameFormatRGBA,
        VideoFrameFormatABGR,
        VideoFrameFormatBGRA,
        VideoFrameFormatRGB565LE,
        VideoFrameFormatRGB565BE,
        VideoFrameFormatBGR565LE,
        VideoFrameFormatBGR565BE,
        VideoFrameFormatMax;

        private VideoFrameFormat() {
        }
    }
}
