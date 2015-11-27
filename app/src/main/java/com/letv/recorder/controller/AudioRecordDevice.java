//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.media.AudioRecord;
import android.util.Log;
import com.letv.recorder.bean.AudioParams;
import com.letv.recorder.controller.ILetvRecorder;
import com.letv.recorder.controller.RecordDevices;

public class AudioRecordDevice extends RecordDevices {
    private static final String TAG = "CameraView2";
    private AudioRecord audioRecord;
    private boolean isRunning;
    private Thread thread;
    private int bufferLenght;
    private ILetvRecorder letvRecorder;
    private AudioParams audioParams;
    Runnable audioRunnable = new Runnable() {
        public void run() {
            AudioRecordDevice.this.audioRecord.startRecording();

            while(AudioRecordDevice.this.isRunning) {
                byte[] tmp = new byte[AudioRecordDevice.this.bufferLenght];
                int size = AudioRecordDevice.this.audioRecord.read(tmp, 0, AudioRecordDevice.this.bufferLenght);
                if(size <= 0) {
                    Log.i("CameraView2", "[audioRecord] audio ignore, no data to read.");
                    break;
                }

                long timestamp = System.nanoTime() / 1000L;
                if(AudioRecordDevice.this.letvRecorder != null) {
                    AudioRecordDevice.this.letvRecorder.feedingAudioFrame(tmp, (long)size, timestamp);
                }
            }

            if(AudioRecordDevice.this.letvRecorder != null) {
                AudioRecordDevice.this.letvRecorder.setStartTime(0L);
            }

            AudioRecordDevice.this.audioRecord.stop();
            Log.d("CameraView2", "[===audio] stop finish");
        }
    };

    public void setLetvRecorder(ILetvRecorder letvRecorder) {
        this.letvRecorder = letvRecorder;
    }

    public AudioRecordDevice(AudioParams audioParams) {
        this.audioParams = audioParams;
        this.choosAudioDevice();
    }

    private void choosAudioDevice() {
        int[] sampleRates = new int[]{'걄', 22050, 11025};
        int[] var5 = sampleRates;
        int var4 = sampleRates.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            int sampleRateInHz = var5[var3];
            this.audioParams.setSampleRateInHz(sampleRateInHz);
            this.audioParams.setChannelConfig(12);
            this.audioParams.setAudioFormat(2);
            byte bSamples = 8;
            if(this.audioParams.getAudioFormat() == 2) {
                bSamples = 16;
            }

            if(this.audioParams.getChannelConfig() == 16) {
                this.audioParams.setnChannels(1);
            }

            int bufferSizeInBytes = AudioRecord.getMinBufferSize(this.audioParams.getSampleRateInHz(), this.audioParams.getChannelConfig(), this.audioParams.getAudioFormat());
            AudioRecord audioRecorder = new AudioRecord(1, this.audioParams.getSampleRateInHz(), this.audioParams.getChannelConfig(), this.audioParams.getAudioFormat(), bufferSizeInBytes);
            if(audioRecorder.getState() == 1) {
                this.audioRecord = audioRecorder;
                this.bufferLenght = 2048;
                Log.i("CameraView2", String.format("[audioRecord] mic open rate=%dHZ, channels=%d, bits=%d,buffer:%d, state=%d", new Object[]{Integer.valueOf(sampleRateInHz), Integer.valueOf(this.audioParams.getnChannels()), Integer.valueOf(bSamples), Integer.valueOf(bufferSizeInBytes), Integer.valueOf(audioRecorder.getState())}));
                break;
            }

            Log.e("CameraView2", "initialize the mic failed.");
        }

    }

    public boolean start() {
        if(this.audioRecord != null) {
            this.isRunning = true;
            this.thread = new Thread(this.audioRunnable, "[aThread]");
            this.thread.start();
            return true;
        } else {
            Log.e("CameraView2", "[audioRecord] AudioRecord is null !!");
            return false;
        }
    }

    public void stop() {
        this.isRunning = false;
        Log.d("CameraView2", "[======audio stop]");
        if(this.thread != null) {
            try {
                this.thread.join();
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

    }

    public boolean isRecording() {
        return this.isRunning;
    }

    public void reset() {
    }

    public void release() {
        if(this.audioRecord != null) {
            this.audioRecord.release();
        }

    }
}
