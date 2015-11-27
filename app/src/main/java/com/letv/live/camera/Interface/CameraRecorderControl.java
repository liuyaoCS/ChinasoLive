//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.live.camera.Interface;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.letv.live.camera.Interface.RecordCallbackListener;

public interface CameraRecorderControl {
  void initParameters(Context var1, int var2);

  void startVideoRecording();

  void stopVideoRecording();

  void release();

  void setCurrentAngle(int var1);

  Bitmap getCurrentFrameThumbnail();

  int switchCamera();

  boolean isSupportedFlashMode();

  boolean isFrontCamera();

  void setFlashMode(boolean var1);

  float getScaleRatio();

  int getVideoWidth();

  void switchPreviewSize(Context var1, int var2);

  View getCameraSurfaceView();

  void setRecordListener(RecordCallbackListener var1);
}
