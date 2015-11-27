//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import com.letv.recorder.ui.logic.UiObservable;

public class RecorderDialog extends Dialog {
    private Bundle bundle;
    private UiObservable observable;

    public RecorderDialog(Context context, int theme) {
        super(context, theme);
    }

    protected RecorderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public RecorderDialog(Context context) {
        super(context);
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public UiObservable getObservable() {
        return this.observable;
    }

    public void setObservable(UiObservable observable) {
        this.observable = observable;
    }
}
