//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.Log;
import com.letv.recorder.util.ReUtils;
import java.util.Observable;
import java.util.Observer;

public class RecorderTopFloatView extends RelativeLayout implements Observer {
    private static final String TAG = "CameraView2";
    private Context context;
    private ImageView leftArrayBtn;
    private TextView topTitle;
    private ImageView chCamBtn;
    private ImageView micBtn;
    private ImageView flashBtn;
    private boolean useMic = true;
    private boolean useBackCam = true;
    private boolean useFlash = false;
    private UiObservable topSubject = new UiObservable();

    public boolean isUserMic() {
        return this.useMic;
    }

    public void setUserMic(boolean userMic) {
        this.useMic = userMic;
    }

    public boolean isUseBackCam() {
        return this.useBackCam;
    }

    public void setUseBackCam(boolean useBackCam) {
        this.useBackCam = useBackCam;
    }

    public boolean isUseFlash() {
        return this.useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    public UiObservable getTopSubject() {
        return this.topSubject;
    }

    public RecorderTopFloatView(Context context) {
        super(context);
        this.context = context;
        this.initView();
    }

    public RecorderTopFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initView();
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(ReUtils.getLayoutId(this.context, "letv_recorder_top_float_layout"), this);
        this.leftArrayBtn = (ImageView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_left_arraw"));
        this.leftArrayBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 8);
                RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
            }
        });
        this.topTitle = (TextView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_top_title"));
        this.chCamBtn = (ImageView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_top_change_cam"));
        this.chCamBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if(RecorderTopFloatView.this.useBackCam) {
                    bundle.putInt("flag", 7);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.useBackCam = false;
                    RecorderTopFloatView.this.flashBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_flash_gray"));
                    RecorderTopFloatView.this.useFlash = false;
                } else {
                    RecorderTopFloatView.this.flashBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_flash_white"));
                    bundle.putInt("flag", 6);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.useBackCam = true;
                }

            }
        });
        this.micBtn = (ImageView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_top_mic"));
        this.micBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if(RecorderTopFloatView.this.useMic) {
                    bundle.putInt("flag", 5);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.micBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_mic_blue"));
                    RecorderTopFloatView.this.useMic = false;
                } else {
                    bundle.putInt("flag", 4);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.micBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_mic_white"));
                    RecorderTopFloatView.this.useMic = true;
                }

            }
        });
        this.flashBtn = (ImageView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_flash"));
        this.flashBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if(RecorderTopFloatView.this.useFlash) {
                    bundle.putInt("flag", 3);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.flashBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_flash_white"));
                    RecorderTopFloatView.this.useFlash = false;
                } else if(RecorderTopFloatView.this.useBackCam) {
                    bundle.putInt("flag", 2);
                    RecorderTopFloatView.this.getTopSubject().notifyObserverPlus(bundle);
                    RecorderTopFloatView.this.flashBtn.setImageResource(ReUtils.getDrawableId(RecorderTopFloatView.this.context, "letv_recorder_flash_blue"));
                    RecorderTopFloatView.this.useFlash = true;
                } else {
                    Toast.makeText(RecorderTopFloatView.this.context, "该摄像头不支持闪光灯", 0).show();
                }

            }
        });
    }

    public void updateTitle(String title) {
        this.topTitle.setText(title);
    }

    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle)data;
        int flag = bundle.getInt("flag");
        if(flag == 0) {
            Log.d("CameraView2", "[oberver] recorder_start |recorderTopFlatView");
            this.setVisibility(8);
        } else if(1 == flag) {
            Log.d("CameraView2", "[oberver] recorder_stop |recorderTopFlatView");
            this.setVisibility(0);
        }

    }
}
