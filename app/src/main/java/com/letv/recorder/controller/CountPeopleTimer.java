//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.controller;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.letv.recorder.callback.RequestCallback;
import com.letv.recorder.request.RecorderRequest;
import com.letv.recorder.ui.logic.UiObservable;

public class CountPeopleTimer {
    private static final String TAG = "CameraView2";
    private static final int DELAY_MILLIS = 30000;
    Handler handler = new Handler();
    UiObservable countObservable = new UiObservable();
    RecorderRequest recorderRequest = new RecorderRequest();
    Runnable mStartCountRunnable = new Runnable() {
        public void run() {
            CountPeopleTimer.this.recorderRequest.onlineCountRequest(RecorderRequest.activityId, new RequestCallback() {
                public void onSucess(Object object) {
                    String countPeople = (String)object;
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 11);
                    bundle.putString("count", countPeople);
                    CountPeopleTimer.this.countObservable.notifyObserverPlus(bundle);
                    Log.d("CameraView2", "[count] people:" + countPeople);
                    CountPeopleTimer.this.handler.postDelayed(CountPeopleTimer.this.mStartCountRunnable, 30000L);
                }

                public void onFailed(int statusCode, String errorMsg) {
                    Log.w("CameraView2", "[count] code:" + statusCode + "," + errorMsg);
                }
            });
        }
    };

    public UiObservable getCountObservable() {
        return this.countObservable;
    }

    public CountPeopleTimer() {
    }

    public void startCountPeople() {
        this.handler.post(this.mStartCountRunnable);
    }

    public void stopCountPeople() {
        this.handler.removeCallbacks(this.mStartCountRunnable);
    }
}
