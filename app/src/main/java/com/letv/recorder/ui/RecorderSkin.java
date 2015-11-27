//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.callback.LetvRecorderCallback;
import com.letv.recorder.callback.PublishListener;
import com.letv.recorder.controller.CountPeopleTimer;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.RecorderBottomView;
import com.letv.recorder.ui.RecorderDialog;
import com.letv.recorder.ui.RecorderDialogBuilder;
import com.letv.recorder.ui.RecorderTopFloatView;
import com.letv.recorder.ui.RecorderView;
import com.letv.recorder.util.Log;
import com.letv.recorder.util.NetworkUtils;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RecorderSkin implements Callback {
    protected static final String TAG = "CameraView2";
    private RecorderView rv;
    private RecorderTopFloatView topFloatView;
    private RecorderBottomView bottomView;
    private RecorderDialog machineDialog;
    private static LetvPublisher publisher;
    private Handler handler = new Handler();
    private Context context;
    private CountPeopleTimer mCountPeopleTimer;
    private Observer machineObserver = new Observer() {
        public void update(Observable observable, Object data) {
            Bundle bundle = (Bundle)data;
            int flag = bundle.getInt("flag");
            switch(flag) {
                case 9:
                    Log.d("CameraView2", "[observer] selected_angle 选择推流");
                    int numFlag = bundle.getInt("numFlag");
                    RecorderSkin.this.selectMachine(numFlag);
                default:
            }
        }
    };
    private RecorderSkin.NetworkChangeReceiver networkChangeReceiver = new RecorderSkin.NetworkChangeReceiver(/*(RecorderSkin.NetworkChangeReceiver)null*/);
    private SurfaceView cameraView;

    public RecorderSkin() {
        publisher=LetvPublisher.getInstance();
    }

    public void build(Context context, RecorderView rv) {
        this.rv = rv;
        this.context = context;
        this.setCameraView(new SurfaceView(context));
        this.initCameraPreView2(this.getCameraView());
        this.initStartCountTimer();
        this.initObserver();
        this.startCountPeople();
    }

    private void initStartCountTimer() {
        this.mCountPeopleTimer = new CountPeopleTimer();
    }

    private void startCountPeople() {
        if(this.mCountPeopleTimer != null) {
            this.mCountPeopleTimer.startCountPeople();
        }

    }

    private void stopCountPeople() {
        if(this.mCountPeopleTimer != null) {
            this.mCountPeopleTimer.stopCountPeople();
        }

    }

    public void BindingPublisher(LetvPublisher letvpublisher) {
        publisher = letvpublisher;
        this.setPublisherListerner();
    }

    private void setPublisherListerner() {
        if(publisher != null) {
            publisher.setPublishListener(new PublishListener() {
                public void onPublish(int code, String msg) {
                    Log.d("CameraView2", "[callback] code:" + code);
                    switch(code) {
                        case 100:
                            RecorderSkin.this.handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(RecorderSkin.this.context, "连接网络地址失败", Toast.LENGTH_SHORT).show();
                                    RecorderSkin.this.stopPublishWithUI();
                                }
                            });
                        case 101:
                        default:
                    }
                }
            });
        }

    }

    private void initCameraPreView2(SurfaceView cameraView) {
        this.setBottomView(new RecorderBottomView(this.context));
        this.setTopFloatView(new RecorderTopFloatView(this.context));
        this.rv.attachTopFloatView(this.getTopFloatView());
        this.rv.attachBomttomView(this.getBottomView());
        this.rv.attachSurfaceView(cameraView);
        cameraView.getHolder().addCallback(this);
    }

    private void initObserver() {
        this.rv.getStartSubject().addObserver(new Observer() {
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle)data;
                int flag = bundle.getInt("flag");
                switch(flag) {
                    case 0:
                        Log.d("CameraView2", "[observer] recorder_start 开始推流");
                        if(RecorderSkin.publisher != null) {
                            RecorderSkin.publisher.publish();
                        }
                        break;
                    case 1:
                        Log.d("CameraView2", "[observer] recorder_stop 停止推流");
                        if(RecorderSkin.publisher != null) {
                            RecorderSkin.publisher.stopPublish();
                        }
                        break;
                    case 10:
                        RecorderSkin.this.handleMachine();
                }

            }
        });
        this.rv.getStartSubject().addObserver(this.getTopFloatView());
        this.getTopFloatView().getTopSubject().addObserver(Publisher.getVideoRecordDevice());
        this.getTopFloatView().getTopSubject().addObserver(new Observer() {
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle)data;
                int flag = bundle.getInt("flag");
                if(8 == flag) {
                    Log.d("CameraView2", "[observer] top_float_back 返回按钮");
                    if(RecorderSkin.this.context instanceof Activity) {
                        ((Activity)RecorderSkin.this.context).finish();
                    }
                }

            }
        });
        this.mCountPeopleTimer.getCountObservable().addObserver(this.bottomView);
    }

    private void handleMachine() {
        Log.d("CameraView2", "[observer] angle_request 机位请求");
        if(publisher != null) {
            publisher.handleMachine(new LetvRecorderCallback() {
                public void onSucess(final Object data1) {
                   final ArrayList<LivesInfo> data=(ArrayList<LivesInfo>)data1;
                    int num = data.size();
                    switch(num) {
                        case 0:
                            RecorderSkin.this.handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(RecorderSkin.this.context, "当前无机位", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case 1:
                            RecorderSkin.this.handler.post(new Runnable() {
                                public void run() {
                                    RecorderSkin.this.selectMachine(0);
                                }
                            });
                            break;
                        default:
                            RecorderSkin.this.handler.post(new Runnable() {
                                public void run() {
                                    if(RecorderSkin.this.machineDialog == null || !RecorderSkin.this.machineDialog.isShowing()) {
                                        Bundle dialogBundle = new Bundle();
                                        dialogBundle.putSerializable("liveInfo", data);
                                        RecorderSkin.this.machineDialog = RecorderDialogBuilder.showMachineDialog(RecorderSkin.this.context, dialogBundle);
                                        RecorderSkin.this.machineDialog.getObservable().addObserver(RecorderSkin.this.machineObserver);
                                    }
                                }
                            });
                    }

                }

                public void onFailed(final int code, final String msg) {
                    RecorderSkin.this.handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(RecorderSkin.this.context, "接口请求失败,错误码:" + code + "," + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });
        }

    }

    public void onResume() {
        this.registerNetworkChange();
    }

    public void onPause() {
        this.unregisterNetworkChange();
        this.stopPublishWithUI();
    }

    public void onDestroy() {
        this.stopCountPeople();
    }

    private void stopPublishWithUI() {
        if(this.rv != null) {
            this.rv.stopAuto();
        }

    }

    protected RecorderDialog getMachineDialog() {
        return this.machineDialog;
    }

    protected void setMachineDialog(RecorderDialog machineDialog) {
        this.machineDialog = machineDialog;
    }

    protected RecorderBottomView getBottomView() {
        return this.bottomView;
    }

    protected void setBottomView(RecorderBottomView bottomView) {
        this.bottomView = bottomView;
    }

    protected RecorderTopFloatView getTopFloatView() {
        return this.topFloatView;
    }

    protected void setTopFloatView(RecorderTopFloatView topFloatView) {
        this.topFloatView = topFloatView;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if(Publisher.getVideoRecordDevice() != null) {
            Publisher.getVideoRecordDevice().bindingSurface(arg0);
            Publisher.getVideoRecordDevice().start();
        }

    }

    public void surfaceCreated(SurfaceHolder arg0) {
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        if(Publisher.getVideoRecordDevice() != null) {
            Publisher.getVideoRecordDevice().stop();
        }

    }

    private void selectMachine(int numFlag) {
        if(publisher.selectMachine(numFlag)) {
            this.rv.startRecorder();
            if(this.machineDialog != null && this.machineDialog.isShowing()) {
                this.machineDialog.dismiss();
            }
        } else {
            Toast.makeText(this.context, "该机位正在直播", Toast.LENGTH_SHORT).show();
        }

    }

    private void registerNetworkChange() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.setPriority(1000);
        this.context.registerReceiver(this.networkChangeReceiver, filter);
    }

    private void unregisterNetworkChange() {
        this.context.unregisterReceiver(this.networkChangeReceiver);
    }

    public SurfaceView getCameraView() {
        return this.cameraView;
    }

    public void setCameraView(SurfaceView cameraView) {
        this.cameraView = cameraView;
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        private Dialog mobileNetdialog;
        private Dialog noNetworkDialog;

        private NetworkChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d("CameraView2", "[netChange]=========== status:" + LetvPublisher.getRecorder().isRecording() + ",type:" + NetworkUtils.getNetType(context));
            if(!NetworkUtils.isWifiNetType(context) && LetvPublisher.getRecorder() != null && LetvPublisher.getRecorder().isRecording()) {
                if(NetworkUtils.getNetType(context) != null) {
                    this.mobileNetdialog = RecorderDialogBuilder.showMobileNetworkWarningDialog(context, new OnClickListener() {
                        public void onClick(View v) {
                            NetworkChangeReceiver.this.mobileNetdialog.dismiss();
                        }
                    }, new OnClickListener() {
                        public void onClick(View v) {
                            NetworkChangeReceiver.this.mobileNetdialog.dismiss();
                        }
                    });
                } else {
                    Log.d("CameraView2", "[netchange]================check");
                    if(this.noNetworkDialog == null) {
                        this.noNetworkDialog = RecorderDialogBuilder.showCommentDialog(context, "当前网络中断,请检查网络设置", "我知道了", "", new OnClickListener() {
                            public void onClick(View v) {
                                NetworkChangeReceiver.this.noNetworkDialog.dismiss();
                                NetworkChangeReceiver.this.noNetworkDialog = null;
                            }
                        }, (OnClickListener)null);
                    }
                }
            }

        }
    }
}
