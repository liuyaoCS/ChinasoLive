//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.content.Context;
import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.bean.RecorderInfo;
import com.letv.recorder.callback.LetvRecorderCallback;
import com.letv.recorder.callback.RequestCallback;
import com.letv.recorder.controller.CountPeopleTimer;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.request.RecorderRequest;
import com.letv.recorder.util.Log;
import java.util.ArrayList;

public class LetvPublisher extends Publisher {
    private static LetvPublisher letvPublisher;
    protected static final String TAG = "CameraView2";
    private int errorCode = 0;
    private RecorderRequest recorderRequest;
    private CountPeopleTimer mCountPeopleTimer;
    private RecorderInfo mRecorderInfo;

    public LetvPublisher() {
    }

    public static LetvPublisher getInstance() {
        if(letvPublisher==null){
            letvPublisher=new LetvPublisher();
        }
        return letvPublisher;
    }

    public static void init(String activityId, String userId, String secretKey) {
        RecorderRequest.init(userId, activityId, secretKey);
    }

    public void initPublisher(Context context) {
        this.recorderRequest = new RecorderRequest();
        this.getRecorderInfo();
        super.initPublisher(context);
    }

    private void getRecorderInfoRetry(final LetvRecorderCallback<ArrayList<LivesInfo>> callback) {
        this.recorderRequest.liveRequest3(RecorderRequest.activityId, new RequestCallback() {
            public void onSucess(Object object) {
                LetvPublisher.this.mRecorderInfo = (RecorderInfo)object;
                if(LetvPublisher.this.mRecorderInfo != null) {
                    LetvPublisher.this.getMachineInfo(callback);
                }

            }

            public void onFailed(int statusCode, String errorMsg) {
                if(callback != null) {
                    callback.onFailed(statusCode, errorMsg);
                }

            }
        });
    }

    public void handleMachine(LetvRecorderCallback<ArrayList<LivesInfo>> callback) {
        if(this.mRecorderInfo != null) {
            this.getMachineInfo(callback);
        } else {
            this.getRecorderInfoRetry(callback);
        }

    }

    public boolean selectMachine(int num) {
        LivesInfo livesInfo = (LivesInfo)this.mRecorderInfo.livesInfos.get(num);
        return this.startRecorderInternal(livesInfo);
    }

    private boolean startRecorderInternal(LivesInfo livesInfo) {
        if(livesInfo.status == 0) {
            this.setUrl(livesInfo.pushUrl);
            return true;
        } else {
            return false;
        }
    }

    private void getMachineInfo(final LetvRecorderCallback<ArrayList<LivesInfo>> callback) {
        this.recorderRequest.machineStateRequest(RecorderRequest.activityId, new RequestCallback() {
            public void onSucess(Object object) {
                RecorderInfo parseData = (RecorderInfo)object;
                ArrayList livesInfos = parseData.livesInfos;

                for(int i = 0; i < livesInfos.size(); ++i) {
                    LivesInfo livesInfo = (LivesInfo)parseData.livesInfos.get(i);
                    ((LivesInfo)LetvPublisher.this.mRecorderInfo.livesInfos.get(i)).status = livesInfo.status;
                }

                if(callback != null) {
                    callback.onSucess(livesInfos);
                }

            }

            public void onFailed(int statusCode, String errorMsg) {
                if(callback != null) {
                    callback.onFailed(statusCode, errorMsg);
                }

            }
        });
    }

    private void getRecorderInfo() {
        this.recorderRequest.liveRequest3(RecorderRequest.activityId, new RequestCallback() {
            public void onSucess(Object object) {
                LetvPublisher.this.mRecorderInfo = (RecorderInfo)object;
            }

            public void onFailed(int statusCode, String errorMsg) {
                Log.w("CameraView2", "接口请求失败,错误码:" + statusCode + ",errorMsg");
                LetvPublisher.this.errorCode = statusCode;
            }
        });
    }
}
