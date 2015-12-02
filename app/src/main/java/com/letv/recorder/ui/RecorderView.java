package com.letv.recorder.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.Log;
import com.letv.recorder.util.NetworkUtils;
import com.letv.recorder.util.ReUtils;
import com.letv.recorder.util.ScreenUtils;
import java.util.Observer;

import com.recorder.upload.FlushFlowActivity;

public class RecorderView extends RelativeLayout implements Callback {
    private static final String TAG = "CameraView2";
    private Context context;
    private FrameLayout topContainer;
    private FrameLayout bottomContainer;
    private RecorderView.CaptureBtn startBtn;
    private boolean isRecording;
    private SurfaceView surfaceView;
    private RelativeLayout surfaceContainer;
    private TextView peopleCountTextView;

    public RecorderView(Context context) {
        super(context);
        this.context = context;
        this.initView();
    }

    public RecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initView();
    }
    public TextView getPeopleCountView(){
        return this.peopleCountTextView;
    }
    private void initView() {
        LayoutInflater.from(this.context).inflate(ReUtils.getLayoutId(this.context, "letv_recorder_main_layout"), this);
        this.topContainer = (FrameLayout)this.findViewById(ReUtils.getId(this.context, "letv_recorder_top_container"));
        this.bottomContainer = (FrameLayout)this.findViewById(ReUtils.getId(this.context, "letv_recorder_bottom_container"));
        this.surfaceContainer = (RelativeLayout)this.findViewById(ReUtils.getId(this.context, "letv_recorder_surface_container"));
        this.attachStartBtn();
        this.isRecording = false;
    }

    public void attachSurfaceView(SurfaceView surfaceView) {
        if(this.surfaceView != null) {
            this.surfaceContainer.removeView(this.surfaceView);
            this.surfaceView.getHolder().removeCallback(this);
        }

        this.surfaceView = surfaceView;
        this.surfaceView.getHolder().addCallback(this);
        this.surfaceContainer.addView(surfaceView);
    }

    public void attachTopFloatView(View topView) {
        this.topContainer.removeAllViews();
        this.topContainer.addView(topView);
    }

    public void attachBomttomView(View bottomView) {
        this.bottomContainer.removeAllViews();
        if(bottomView instanceof Observer) {
            this.startBtn.getStartSubject().deleteObserver((Observer)bottomView);
        }

        LayoutParams lp = (LayoutParams)this.startBtn.getLayoutParams();
        android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(ScreenUtils.getWight(this.context) - lp.width - lp.rightMargin, -1);
        this.bottomContainer.addView(bottomView, params);
        if(bottomView instanceof Observer) {
            this.startBtn.getStartSubject().addObserver((Observer)bottomView);
        }
        if(bottomView instanceof RecorderBottomView){
            this.peopleCountTextView=((RecorderBottomView) bottomView).peopleCount;
            Log.i("ly","found peopleCoutView");
        }else{
            Log.e("ly","not found peopleCoutView");
        }

    }

    public void attachCenterView(View centerView) {
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(13);
        this.addView(centerView, params);
        centerView.setVisibility(GONE);
    }

    public void attachStartBtn() {
        this.startBtn = new RecorderView.CaptureBtn(this.context);
        this.startBtn.setImageResource(ReUtils.getDrawableId(this.context, "letv_recorder_open"));
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(12);
        params.addRule(11);
        params.setMargins(0, 0, dip2px(this.context, 20.0F), dip2px(this.context, 5.0F));
        this.addView(this.startBtn, params);
        android.view.ViewGroup.LayoutParams layoutParams = this.startBtn.getLayoutParams();
        layoutParams.height = dip2px(this.context, 56.0F);
        layoutParams.width = dip2px(this.context, 56.0F);
        this.startBtn.setLayoutParams(layoutParams);
    }

    public void stopAuto() {
//        this.isRecording = true;
//        this.startBtn.onClick((View) null);
    }

    public UiObservable getStartSubject() {
        return this.startBtn.getStartSubject();
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }

    public void startRecorder() {
        this.startBtn.startRecorder();
    }

    public void stopRecorder() {
        this.startBtn.stopRecorder();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private class CaptureBtn extends ImageView implements OnClickListener {
        private UiObservable startSubject = new UiObservable();
        private Dialog mobileNetworkDialog;

        public CaptureBtn(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.initView();
        }

        public CaptureBtn(Context context) {
            super(context);
            this.initView();
        }

        private void initView() {
            this.setOnClickListener(this);
        }

        public void onClick(View v) {
            if(!RecorderView.this.isRecording) {
                this.selectAngle();
            } else {

                if(v!=null){
                    this.stopRecorder();
                    ((FlushFlowActivity)context).finish();
                }
            }

        }

        private void selectAngle() {
            final Bundle bundle = new Bundle();
            if(NetworkUtils.isWifiNetType(RecorderView.this.context)) {
                bundle.putInt("flag", 10);
                this.getStartSubject().notifyObserverPlus(bundle);
            } else if(NetworkUtils.getNetType(RecorderView.this.context) != null) {
                this.mobileNetworkDialog = RecorderDialogBuilder.showMobileNetworkWarningDialog(RecorderView.this.context, new OnClickListener() {
                    public void onClick(View v) {
                        if(CaptureBtn.this.mobileNetworkDialog != null) {
                            CaptureBtn.this.mobileNetworkDialog.dismiss();
                            bundle.putInt("flag", 10);
                            CaptureBtn.this.getStartSubject().notifyObserverPlus(bundle);
                        }

                    }
                }, new OnClickListener() {
                    public void onClick(View v) {
                        if(CaptureBtn.this.mobileNetworkDialog != null) {
                            CaptureBtn.this.mobileNetworkDialog.dismiss();
                        }

                    }
                });
            } else {
                Toast.makeText(RecorderView.this.context, "当前网络中断,请检查网络设置", Toast.LENGTH_SHORT).show();
            }

        }

        public void startRecorder() {
            RecorderView.this.startBtn.setImageResource(ReUtils.getDrawableId(RecorderView.this.context, "letv_recorder_stop"));
            Bundle bundle = new Bundle();
            bundle.putInt("flag", 0);
            this.getStartSubject().notifyObserverPlus(bundle);
            RecorderView.this.isRecording = true;
        }

        public void stopRecorder() {
            RecorderView.this.startBtn.setImageResource(ReUtils.getDrawableId(RecorderView.this.context, "letv_recorder_open"));
            Bundle bundle = new Bundle();
            bundle.putInt("flag", 1);
            this.getStartSubject().notifyObserverPlus(bundle);
            RecorderView.this.isRecording = false;

        }

        public UiObservable getStartSubject() {
            return this.startSubject;
        }
    }
}
