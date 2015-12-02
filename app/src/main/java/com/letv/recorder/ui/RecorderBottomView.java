//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.letv.recorder.util.ReUtils;
import java.util.Formatter;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class RecorderBottomView extends RelativeLayout implements Observer {
    private static final String TAG = "RecorderBottomView";
    private Context context;
    public TextView peopleCount;
    private TextView recorderTime;
    private ImageView thumd;
    private StringBuilder mFormatBuilder = new StringBuilder();
    private Formatter mFormatter;
    private AlphaAnimation alphaAnimation;
    private Handler handler;
    private int time;
    Runnable timeRunnable;

    public RecorderBottomView(Context context) {
        super(context);
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
            }
        };
        this.time = 0;
        this.timeRunnable = new Runnable() {
            public void run() {
                RecorderBottomView.this.time = RecorderBottomView.this.time + 1;
                RecorderBottomView.this.recorderTime.setText(RecorderBottomView.this.stringForTime(RecorderBottomView.this.time));
                if(RecorderBottomView.this.time % 2 == 0) {
                    RecorderBottomView.this.thumd.setVisibility(0);
                } else {
                    RecorderBottomView.this.thumd.setVisibility(4);
                }

                RecorderBottomView.this.handler.postDelayed(this, 1000L);
            }
        };
        this.context = context;
        this.initView();
    }

    public RecorderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
            }
        };
        this.time = 0;
        this.timeRunnable = new Runnable() {
            public void run() {
                RecorderBottomView.this.time = RecorderBottomView.this.time + 1;
                RecorderBottomView.this.recorderTime.setText(RecorderBottomView.this.stringForTime(RecorderBottomView.this.time));
                if(RecorderBottomView.this.time % 2 == 0) {
                    RecorderBottomView.this.thumd.setVisibility(0);
                } else {
                    RecorderBottomView.this.thumd.setVisibility(4);
                }

                RecorderBottomView.this.handler.postDelayed(this, 1000L);
            }
        };
        this.context = context;
        this.initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(ReUtils.getLayoutId(this.context, "letv_recorder_bottom_float_layout"), this);
        this.peopleCount = (TextView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_bottom_people"));
        this.thumd = (ImageView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_bottom_thumd"));
        this.recorderTime = (TextView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_bottom_time"));
        this.recorderTime.setText(this.stringForTime(0));
    }

    public void update(Observable observable, Object data) {
        final Bundle bundle = (Bundle)data;
        int flag = bundle.getInt("flag");
        switch(flag) {
            case 0:
                Log.d("RecorderBottomView", "[observer recorder_start |recorderBottomView]");
                this.handler.post(this.timeRunnable);
                break;
            case 1:
                Log.d("RecorderBottomView", "[observer recorder_stop |recorderBottomView]");
                this.reset();
                break;
            case 11:
                this.handler.post(new Runnable() {
                    public void run() {
//                        String countPeople = bundle.getString("count");
//                        RecorderBottomView.this.peopleCount.setText(countPeople);
                    }
                });
        }

    }

    public void reset() {
        this.resetInternal();
        this.handler.removeCallbacks(this.timeRunnable);
    }

    private void resetInternal() {
        this.recorderTime.setText(this.stringForTime(0));
        this.thumd.setVisibility(0);
        this.time = 0;
    }

    private String stringForTime(int timeMs) {
        int seconds = timeMs % 60;
        int minutes = timeMs / 60 % 60;
        int hours = timeMs / 3600;
        this.mFormatBuilder.setLength(0);
        return hours > 0?this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString():this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }
}
