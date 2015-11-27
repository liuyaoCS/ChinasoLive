//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.bean.RecorderInfo;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.Log;
import com.letv.recorder.util.ReUtils;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RecorderAngleView extends RelativeLayout implements Observer {
    private static final String TAG = "CameraView2";
    private Context context;
    private RecorderAngleView.AngleBtn angle1 = new RecorderAngleView.AngleBtn(1);
    private RecorderAngleView.AngleBtn angle2 = new RecorderAngleView.AngleBtn(2);
    private RecorderAngleView.AngleBtn angle3 = new RecorderAngleView.AngleBtn(3);
    private RecorderAngleView.AngleBtn angle4 = new RecorderAngleView.AngleBtn(4);
    private ArrayList<RecorderAngleView.AngleBtn> angles;

    public RecorderAngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public RecorderAngleView(Context context) {
        super(context);
        this.initView(context);
    }

    public void addObserver(Observer oberver) {
        this.angle1.addObserver(oberver);
        this.angle2.addObserver(oberver);
        this.angle3.addObserver(oberver);
        this.angle4.addObserver(oberver);
    }

    public void setVisibility(int visibility) {
        if(8 == visibility) {
            this.reset();
        }

        super.setVisibility(visibility);
    }

    public void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_angle_layout"), this);
        this.angles = new ArrayList();
        this.angle1.angle = this.findViewById(ReUtils.getId(context, "letv_recorder_angle_1"));
        this.angle1.angleI = (ImageView)this.findViewById(ReUtils.getId(context, "letv_recorder_angle_1_i"));
        this.angle1.setOnClickListenner();
        this.angle1.setEnable(false);
        this.angles.add(this.angle1);
        this.angle2.angle = this.findViewById(ReUtils.getId(context, "letv_recorder_angle_2"));
        this.angle2.angleI = (ImageView)this.findViewById(ReUtils.getId(context, "letv_recorder_angle_2_i"));
        this.angle2.setOnClickListenner();
        this.angle2.setEnable(false);
        this.angles.add(this.angle2);
        this.angle3.angle = this.findViewById(ReUtils.getId(context, "letv_recorder_angle_3"));
        this.angle3.angleI = (ImageView)this.findViewById(ReUtils.getId(context, "letv_recorder_angle_3_i"));
        this.angle3.setOnClickListenner();
        this.angle3.setEnable(false);
        this.angles.add(this.angle3);
        this.angle4.angle = this.findViewById(ReUtils.getId(context, "letv_recorder_angle_4"));
        this.angle4.angleI = (ImageView)this.findViewById(ReUtils.getId(context, "letv_recorder_angle_4_i"));
        this.angle4.setOnClickListenner();
        this.angle4.setEnable(false);
        this.angles.add(this.angle4);
    }

    public void updataAngleStatus(RecorderInfo mRecorderInfo) {
        ArrayList livesInfos = mRecorderInfo.livesInfos;

        for(int i = 0; i < livesInfos.size(); ++i) {
            LivesInfo livesInfo = (LivesInfo)livesInfos.get(i);
            int machine = livesInfo.machine;
            int status = livesInfo.status;

            for(int j = 0; j < this.angles.size(); ++j) {
                RecorderAngleView.AngleBtn angleBtn = (RecorderAngleView.AngleBtn)this.angles.get(j);
                if(machine == angleBtn.numFlag) {
                    angleBtn.status = status;
                    if(status == 0) {
                        angleBtn.angleI.setImageResource(ReUtils.getDrawableId(this.context, "letv_recorder_angle_blue"));
                    }

                    angleBtn.setEnable(true);
                }
            }
        }

    }

    public void reset() {
        if(this.angles != null) {
            for(int i = 0; i < this.angles.size(); ++i) {
                RecorderAngleView.AngleBtn angleBtn = (RecorderAngleView.AngleBtn)this.angles.get(i);
                angleBtn.setEnable(false);
                angleBtn.angleI.setImageResource(ReUtils.getDrawableId(this.context, "letv_recorder_angle_gray"));
            }
        }

    }

    public void update(Observable observable, Object data) {
        int flag = ((Integer)data).intValue();
        switch(flag) {
            case 1:
                Log.d("CameraView2", "[observer] recorder_stop angleView");
            default:
        }
    }

    class AngleBtn extends UiObservable {
        public View angle;
        public ImageView angleI;
        public boolean angleFlag = false;
        public int numFlag;
        public int status;

        public AngleBtn(int numFlag) {
            this.numFlag = numFlag;
        }

        public void setEnable(boolean enabled) {
            this.angle.setEnabled(enabled);
        }

        public void setVisibility(int visibility) {
            this.angle.setVisibility(visibility);
        }

        public void setOnClickListenner() {
            if(this.angle != null) {
                this.angle.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("flag", 9);
                        bundle.putInt("numFlag", AngleBtn.this.numFlag);
                        bundle.putInt("status", AngleBtn.this.status);
                        AngleBtn.this.notifyObserverPlus(bundle);
                    }
                });
            }

        }
    }
}
