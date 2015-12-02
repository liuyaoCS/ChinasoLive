package com.recorder.net.model;

import java.io.Serializable;

/**
 * Created by rmss on 2015/11/18.
 */
public class LiveVideoListInfo implements Serializable{
    String uid;
    String letvid;
    String rongid;
    String status;
    String timeStamp;
    String name;	  //用户名
    String avatar;	  //头像
    String cover;     //视频封面
    String coverTitle;//视频封面名称

    public String getRongid() {
        return rongid;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCover() {
        return cover;
    }

    public String getCoverTitle() {
        return coverTitle;
    }

    public String getUid() {
        return uid;
    }

    public String getLetvid() {
        return letvid;
    }

    public String getRonid() {
        return rongid;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }


}
