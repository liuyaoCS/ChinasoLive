package com.recorder.net.model;

import java.io.Serializable;

/**
 * Created by rmss on 2015/11/19.
 */
public class StopVideoInfo implements Serializable{
    public String activityId;
    public String result;
    public  String getActivityId(){
        return  activityId;
    }
    public String getResult(){
        return  result;
    }

}
