//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.bean;

import java.io.Serializable;

public class AudioParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private int channelConfig = 12;
    private int audioFormat = 2;
    private int nChannels = 2;
    private int sampleRateInHz = '걄';

    public AudioParams() {
    }

    public int getChannelConfig() {
        return this.channelConfig;
    }

    public void setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
    }

    public int getAudioFormat() {
        return this.audioFormat;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getnChannels() {
        return this.nChannels;
    }

    public void setnChannels(int nChannels) {
        this.nChannels = nChannels;
    }

    public int getSampleRateInHz() {
        return this.sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }
}
