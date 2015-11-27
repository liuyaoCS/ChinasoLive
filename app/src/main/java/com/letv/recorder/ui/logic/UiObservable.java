//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui.logic;

import java.util.Observable;

public class UiObservable extends Observable {
    public UiObservable() {
    }

    public void setChangeFlag() {
        this.setChanged();
    }

    public void notifyObserverPlus(Object data) {
        this.setChangeFlag();
        this.notifyObservers(data);
    }

    public void notifyObserverPlus() {
        this.notifyObserverPlus((Object) null);
    }
}
