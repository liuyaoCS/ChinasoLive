//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.bean;

import com.letv.recorder.bean.LivesInfo;
import java.io.Serializable;
import java.util.ArrayList;

public class RecorderInfo implements Serializable {
    public String activityId;
    public String activityName;
    public String liveNum;
    public String playerPageUrl;
    public String description;
    public String coverImgUrl;
    public ArrayList<LivesInfo> livesInfos = new ArrayList();

    public RecorderInfo() {
    }
}
