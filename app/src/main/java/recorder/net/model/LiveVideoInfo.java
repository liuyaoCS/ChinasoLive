package recorder.net.model;

import java.io.Serializable;

/**
 * Created by rmss on 2015/11/18.
 */
public class LiveVideoInfo implements Serializable{
    String uid;
    String letvid;
    String rongid;

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

    String status;
    String timeStamp;
}
