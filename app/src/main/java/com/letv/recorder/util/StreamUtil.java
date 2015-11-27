//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import com.letv.recorder.util.ScriptC_blur;
import com.letv.recorder.util.ScriptC_rotate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StreamUtil {
    private static final String TAG = "StreamUtil";
    private RenderScript rs;
    private ScriptC_blur blur_script;
    private ScriptC_rotate rotate_script;
    private Allocation allocationRGB;
    private Allocation allocationBlur;
    private Allocation rows;
    private Allocation columns;
    private StreamUtil.Size srcImageSize = null;
    private StreamUtil.Size tagImageSize = null;
    private Bitmap mBlurBitmap;
    private Bitmap mRgbBitmap;
    private int[] mRgbPixels;
    private byte[] mOutputBuffer;
    private View mOutputView;
    private boolean isNeedBlur = false;
    private boolean isNeedCrop = true;
    private byte[] mCroppedBuffer = null;

    public void setOutputView(View ov) {
        this.mOutputView = ov;
        this.mOutputView.setBackgroundDrawable(new BitmapDrawable(this.mBlurBitmap));
    }

    public StreamUtil(Context context, int width, int height, View ov) {
        this.mOutputView = ov;
        this.srcImageSize = new StreamUtil.Size(width, height);
        this.calTargetImageSize();
        this.rs = RenderScript.create(context);
        this.blur_script = new ScriptC_blur(this.rs);
        this.rotate_script = new ScriptC_rotate(this.rs);
        this.mRgbBitmap = null;
        this.mBlurBitmap = Bitmap.createBitmap(this.tagImageSize.width, this.tagImageSize.height, Config.ARGB_8888);
        this.mOutputBuffer = new byte[this.tagImageSize.width * this.tagImageSize.height * 4];
        this.mRgbPixels = new int[this.tagImageSize.width * this.tagImageSize.height];
        if(this.mOutputView != null) {
            this.mOutputView.setBackgroundDrawable(new BitmapDrawable(this.mBlurBitmap));
        }

        this.allocationBlur = Allocation.createFromBitmap(this.rs, this.mBlurBitmap);
        int[] row_indices = new int[this.tagImageSize.height];

        for(int column_indices = 0; column_indices < this.tagImageSize.height; row_indices[column_indices] = column_indices++) {
            ;
        }

        this.rows = Allocation.createSized(this.rs, Element.U32(this.rs), this.tagImageSize.height, 1);
        this.rows.copyFrom(row_indices);
        int[] var8 = new int[this.tagImageSize.width];

        for(int i = 0; i < this.tagImageSize.width; var8[i] = i++) {
            ;
        }

        this.columns = Allocation.createSized(this.rs, Element.U32(this.rs), this.tagImageSize.width, 1);
        this.columns.copyFrom(var8);
        this.rotate_script.set_gOut(this.allocationBlur);
        this.rotate_script.set_width(this.tagImageSize.width);
        this.rotate_script.set_height(this.tagImageSize.height);
        this.rotate_script.set_direction(2);
        this.blur_script.set_gIn(this.allocationBlur);
        this.blur_script.set_width((long)this.tagImageSize.width);
        this.blur_script.set_height((long)this.tagImageSize.height);
        this.blur_script.set_radius(32L);
    }

    private void calTargetImageSize() {
        if(this.srcImageSize != null) {
            if(this.srcImageSize.width * 9 < this.srcImageSize.height * 16) {
                this.tagImageSize = new StreamUtil.Size(this.srcImageSize.width * 9 / 16, this.srcImageSize.width);
                this.mCroppedBuffer = new byte[this.tagImageSize.height * this.tagImageSize.width * 3 / 2];
            } else if(this.srcImageSize.width * 9 > this.srcImageSize.height * 16) {
                this.tagImageSize = new StreamUtil.Size(this.srcImageSize.height, this.srcImageSize.height * 16 / 9);
                this.mCroppedBuffer = new byte[this.tagImageSize.height * this.tagImageSize.width * 3 / 2];
            } else {
                this.tagImageSize = new StreamUtil.Size(this.srcImageSize.height, this.srcImageSize.width);
                this.isNeedCrop = false;
            }

        }
    }

    public int getCroppedVideoWidth() {
        return this.tagImageSize.width;
    }

    public int getCroppedVideoHeight() {
        return this.tagImageSize.height;
    }

    public void setRotateDirection(int dir) {
        if(this.rotate_script != null && dir >= 0 && dir <= 3) {
            this.rotate_script.set_direction(dir);
        }

    }

    public void setBlurRadius(int r) {
        if(this.blur_script != null && r > 0) {
            this.blur_script.set_radius((long)r);
        }

    }

    public void setBlurState(boolean enableBlur) {
        this.isNeedBlur = enableBlur;
    }

    public Bitmap getScreenShot() {
        return this.mBlurBitmap;
    }

    public byte[] dealVideoFrame(byte[] data) {
        if(!this.isNeedCrop) {
            this.mCroppedBuffer = data;
        } else {
            this.cropYUVImage(data);
        }

        this.decodeNV21(this.mRgbPixels, this.mCroppedBuffer, this.tagImageSize.height, this.tagImageSize.width);
        this.mRgbBitmap = Bitmap.createBitmap(this.mRgbPixels, 0, this.tagImageSize.height, this.tagImageSize.height, this.tagImageSize.width, Config.ARGB_8888);
        this.allocationRGB = Allocation.createFromBitmap(this.rs, this.mRgbBitmap);
        this.rotate_script.set_gIn(this.allocationRGB);
        this.rotate_script.forEach_filter(this.allocationBlur);
        this.allocationBlur.syncAll(128);
        if(this.isNeedBlur) {
            this.blur_script.forEach_blur_h(this.rows);
            this.blur_script.forEach_blur_v(this.columns);
            this.allocationBlur.syncAll(128);
        }

        this.allocationBlur.copyTo(this.mOutputBuffer);
        this.mOutputView.postInvalidate();
        return this.mOutputBuffer;
    }

    private void cropYUVImage(byte[] src) {
        if(this.srcImageSize.width * 9 < this.srcImageSize.height * 16) {
            int startRow = (this.srcImageSize.height - this.tagImageSize.width) / 2;
            System.arraycopy(src, startRow * this.srcImageSize.width, this.mCroppedBuffer, 0, this.tagImageSize.height * this.tagImageSize.width);
            System.arraycopy(src, (this.srcImageSize.height + startRow / 2) * this.srcImageSize.width, this.mCroppedBuffer, this.tagImageSize.height * this.tagImageSize.width, this.tagImageSize.height * this.tagImageSize.width / 2);
        } else {
            int var10000 = this.srcImageSize.width;
            var10000 = this.srcImageSize.height;
        }

    }

    private void decodeNV21(int[] rgb, byte[] yuv420sp, int width, int height) {
        int frameSize = width * height;
        int j = 0;

        for(int yp = 0; j < height; ++j) {
            int uvp = frameSize + (j >> 1) * width;
            int u = 0;
            int v = 0;

            for(int i = 0; i < width; ++yp) {
                int y = (255 & yuv420sp[yp]) - 16;
                if(y < 0) {
                    y = 0;
                }

                if((i & 1) == 0) {
                    v = (255 & yuv420sp[uvp++]) - 128;
                    u = (255 & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = y1192 + 1634 * v;
                int g = y1192 - 833 * v - 400 * u;
                int b = y1192 + 2066 * u;
                if(r < 0) {
                    r = 0;
                } else if(r > 262143) {
                    r = 262143;
                }

                if(g < 0) {
                    g = 0;
                } else if(g > 262143) {
                    g = 262143;
                }

                if(b < 0) {
                    b = 0;
                } else if(b > 262143) {
                    b = 262143;
                }

                rgb[yp] = -16777216 | r << 6 & 16711680 | g >> 2 & '\uff00' | b >> 10 & 255;
                ++i;
            }
        }

    }

    private void saveBitmap(Bitmap bm, String name) {
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/tmp/", name);
        if(f.exists()) {
            f.delete();
        }

        try {
            Log.d("StreamUtil", "Saving bitmap " + name);
            FileOutputStream e = new FileOutputStream(f);
            bm.compress(CompressFormat.PNG, 90, e);
            e.flush();
            e.close();
            Log.d("StreamUtil", "Bitmap " + name + " saved.");
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    private void saveRawData(byte[] buffer, int len, String name) {
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/tmp/", name);
        if(f.exists()) {
            f.delete();
        }

        try {
            Log.d("StreamUtil", "Saving raw data to " + name);
            FileOutputStream e = new FileOutputStream(f);
            e.write(buffer);
            e.flush();
            e.close();
            Log.d("StreamUtil", "File " + name + " saved.");
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    private class Size {
        public int width;
        public int height;

        public Size(int w, int h) {
            this.width = w;
            this.height = h;
        }
    }
}
