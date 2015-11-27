//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

public interface ILetvRecorder {
  int open(String var1);

  void useAudio(boolean var1);

  void close();

  void release();

  int feedingVideoFrame(byte[] var1, long var2, long var4);

  int feedingAudioFrame(byte[] var1, long var2, long var4);

  void setVideoParams(int var1, int var2, int var3, long var4);

  void setAudioParams(int var1, int var2, long var3, long var5);

  void setStartTime(long var1);

  long getStartTime();
}
