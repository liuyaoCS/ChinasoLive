package com.chinaso.cl;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.chinaso.cl.Utils.SPUtil;
import com.lecloud.common.cde.LeCloud;
import com.letv.recorder.util.Log;

import io.rong.imlib.RongIMClient;

/**
 * Created by rmss on 2015/11/18.
 */
public class ClApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        LeCloud.init(this);
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIMClient.init(this);
        }
        SPUtil.init(this);

    }
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
