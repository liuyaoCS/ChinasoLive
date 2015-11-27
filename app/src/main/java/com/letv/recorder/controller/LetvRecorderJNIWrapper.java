//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import com.letv.recorder.controller.ILetvRecorder;
import com.letv.whatslive.jni.LetvRecorderJNI;

public class LetvRecorderJNIWrapper implements ILetvRecorder {
    private LetvRecorderJNI recorder = new LetvRecorderJNI();
    private boolean useAudio = true;
    private static long recorderStartTime;
    private boolean isRecording = false;
    private boolean firstEncode = true;
    private final int FREAME_RATE = 15;
    private final int PER_FREAME_TIME = 66666;
    private long last_pts = 0L;
    private long cur_pts = 0L;

    public LetvRecorderJNIWrapper() {
    }

    public int open(String url) {
        int ret = -1;
        if(this.recorder != null) {
            ret = this.recorder.open(url, Boolean.valueOf(this.useAudio));
        }

        this.setRecording(true);
        return ret;
    }

    public void useAudio(boolean hasAudio) {
        this.useAudio = hasAudio;
    }

    public void close() {
        if(this.recorder != null && this.isRecording()) {
            this.recorder.close();
        }

        this.setRecording(false);
        this.firstEncode = true;
    }

    public void release() {
        if(this.recorder != null) {
            this.recorder.release();
        }

        recorderStartTime = 0L;
    }

    public int feedingVideoFrame(byte[] pData, long numBytes, long timestamp) {
        if(this.recorder != null) {
            if(this.getStartTime() < 0L) {
                return -1;
            }

            if(this.getStartTime() > 0L) {
                return this.recorder.supplyVideoFrame(pData, numBytes, timestamp - this.getStartTime());
            }
        }

        return -1;
    }

    public int feedingAudioFrame(byte[] samples, long numSamples, long timestamp) {
        if(this.recorder != null) {
            if(this.getStartTime() == 0L) {
                this.setStartTime(System.nanoTime() / 1000L);
            }

            return this.recorder.supplyAudioSamples(samples, numSamples, timestamp - this.getStartTime());
        } else {
            return -1;
        }
    }

    public void setVideoParams(int fmt, int width, int height, long bitrate) {
        if(this.recorder != null) {
            this.recorder.setVideoOptions(fmt, width, height, bitrate);
        }

    }

    public void setAudioParams(int fmt, int channels, long samplerate, long bitrate) {
        if(this.recorder != null) {
            this.recorder.setAudioOptions(fmt, channels, samplerate, bitrate);
        }

    }

    public void setStartTime(long timestamp) {
        recorderStartTime = timestamp;
    }

    public long getStartTime() {
        return recorderStartTime;
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    private void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }
}
