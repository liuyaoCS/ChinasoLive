package recorder.net.model;

import java.io.Serializable;

/**
 * Created by chinaso on 2015/11/26.
 */
public class CoverInfo implements Serializable{
    String info;
    boolean result;

    public String getInfo() {
        return info;
    }

    public boolean isResult() {
        return result;
    }
}
