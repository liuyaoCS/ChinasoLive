//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.letv.recorder.ui.RecorderDialog;
import com.letv.recorder.ui.RecorderMachineView;
import com.letv.recorder.util.ReUtils;
import java.util.ArrayList;

public class RecorderDialogBuilder {
    public static final String debug_ip = "debugIp";
    public static final String key_machine = "liveInfo";

    public RecorderDialogBuilder() {
    }

    public static Dialog showMobileNetworkWarningDialog(Context context, OnClickListener positiveListener, OnClickListener negetiveListener) {
        String content = "当前非wifi环境，将产生运营商流量费，是否继续?";
        return showCommentDialog(context, content, "确定", "取消", positiveListener, negetiveListener);
    }

    public static Dialog showCommentDialog(Context context, String content, String btnName, String btnCancelName, OnClickListener positiveListener, OnClickListener negitiveListener) {
        View view = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_common_dialog"), (ViewGroup)null);
        TextView textV = (TextView)view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_text"));
        textV.setText(content);
        TextView btn = (TextView)view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_positive"));
        btn.setText(btnName);
        btn.setOnClickListener(positiveListener);
        TextView btnCancel = (TextView)view.findViewById(ReUtils.getId(context, "letv_recorder_dialog_negtive"));
        if(negitiveListener != null) {
            btnCancel.setVisibility(0);
            btnCancel.setText(btnCancelName);
            btnCancel.setOnClickListener(negitiveListener);
        } else {
            btnCancel.setVisibility(8);
        }

        Dialog dialog = new Dialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));
        Window win = dialog.getWindow();
        LayoutParams lp = win.getAttributes();
        lp.width = -2;
        lp.height = -2;
        dialog.setContentView(view, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static void showDebugDialog(Context context, Bundle bundle) {
        View view = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_debug_view"), (ViewGroup)null);
        TextView debugIp = (TextView)view.findViewById(ReUtils.getId(context, "letv_debug_ip"));
        debugIp.setText(bundle.getString("debugIp"));
        Dialog dialog = new Dialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));
        Window win = dialog.getWindow();
        LayoutParams lp = win.getAttributes();
        lp.width = -2;
        lp.height = -2;
        dialog.setContentView(view);
        dialog.show();
    }

    public static RecorderDialog showMachineDialog(Context context, Bundle bundle) {
        RecorderMachineView view = new RecorderMachineView(context);
        if(bundle != null) {
            view.setRecorderInfo((ArrayList)bundle.get("liveInfo"));
        }

        RecorderDialog dialog = new RecorderDialog(context, ReUtils.getStyleId(context, "letvRecorderDialog"));
        Window win = dialog.getWindow();
        LayoutParams lp = win.getAttributes();
        lp.width = -2;
        lp.height = -2;
        dialog.setContentView(view);
        dialog.setObservable(view.getMachineObserable());
        dialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
        dialog.show();
        return dialog;
    }
}
