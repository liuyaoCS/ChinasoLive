//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.callback;

public interface LetvRecorderCallback<T> {
  void onFailed(int var1, String var2);

  void onSucess(T var1);
}
