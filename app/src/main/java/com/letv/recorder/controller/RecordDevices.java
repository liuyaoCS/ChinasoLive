//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

public abstract class RecordDevices {
  public RecordDevices() {
  }

  public abstract boolean start();

  public abstract void stop();

  public abstract boolean isRecording();

  public abstract void reset();

  public abstract void release();
}
