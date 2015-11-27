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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.ReUtils;
import java.util.ArrayList;

public class RecorderMachineView extends FrameLayout {
    private Context context;
    private GridView gridView;
    private HorizontalScrollView container;
    private ArrayList<LivesInfo> livesInfos;
    private UiObservable oberverable = new UiObservable();

    public RecorderMachineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initView();
    }

    public RecorderMachineView(Context context) {
        super(context);
        this.context = context;
        this.initView();
    }

    public UiObservable getMachineObserable() {
        return this.oberverable;
    }

    private void initView() {
        LayoutInflater.from(this.context).inflate(ReUtils.getLayoutId(this.context, "letv_recorder_machine_layout"), this);
        this.gridView = (GridView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_machine_view"));
        this.container = (HorizontalScrollView)this.findViewById(ReUtils.getId(this.context, "letv_recorder_grid_view_container"));
        RecorderMachineView.GridViewAdapter adapter = new RecorderMachineView.GridViewAdapter(/*(RecorderMachineView.GridViewAdapter)null*/);
        this.gridView.setAdapter(adapter);
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 9);
                bundle.putInt("numFlag", position);
                RecorderMachineView.this.oberverable.notifyObserverPlus(bundle);
            }
        });
    }

    private void initGridView() {
        this.container.setVisibility(VISIBLE);
        int size = this.livesInfos.size();
        byte rowNum = 1;
        int gridItemWidth = this.getResources().getDimensionPixelSize(ReUtils.getDimen(this.context, "letv_recorder_machine_item_width"));
        int gridItemHeight = this.getResources().getDimensionPixelSize(ReUtils.getDimen(this.context, "letv_recorder_machine_item_width"));
        int gap = this.getResources().getDimensionPixelSize(ReUtils.getDimen(this.context, "letv_recorder_machine_item_gap"));
        int columnNum = size / rowNum;
        if(columnNum % rowNum != 0) {
            ++columnNum;
        }

        int gridViewWidth = (gridItemWidth + gap) * columnNum;
        int gridViewHeight = (gridItemHeight + gap) * rowNum;
        LayoutParams params = new LayoutParams(gridViewWidth, gridViewHeight);
        this.gridView.setLayoutParams(params);
        this.gridView.setColumnWidth(gridItemWidth);
        this.gridView.setHorizontalSpacing(gap);
        this.gridView.setVerticalSpacing(gap);
        this.gridView.setStretchMode(GridView.NO_STRETCH);
        this.gridView.setNumColumns(columnNum);
    }

    public void setRecorderInfo(ArrayList<LivesInfo> livesInfos) {
        this.livesInfos = livesInfos;
        this.initGridView();
    }

    private class GridViewAdapter extends BaseAdapter {
        private RecorderMachineView.GridViewAdapter.ViewHolder holder;

        private GridViewAdapter() {
        }

        public int getCount() {
            return RecorderMachineView.this.livesInfos != null?RecorderMachineView.this.livesInfos.size():0;
        }

        public Object getItem(int position) {
            return RecorderMachineView.this.livesInfos != null?(LivesInfo)RecorderMachineView.this.livesInfos.get(position):null;
        }

        public long getItemId(int position) {
            return (long)position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(RecorderMachineView.this.context).inflate(ReUtils.getLayoutId(RecorderMachineView.this.context, "letv_recorder_machine_item"), parent, false);
                this.holder = new RecorderMachineView.GridViewAdapter.ViewHolder();
                this.holder.img = (ImageView)convertView.findViewById(ReUtils.getId(RecorderMachineView.this.context, "letv_recorder_angle_i"));
                this.holder.txt = (TextView)convertView.findViewById(ReUtils.getId(RecorderMachineView.this.context, "letv_recorder_machine_num"));
                convertView.setTag(this.holder);
            } else {
                this.holder = (RecorderMachineView.GridViewAdapter.ViewHolder)convertView.getTag();
            }

            LivesInfo livesInfo = (LivesInfo)RecorderMachineView.this.livesInfos.get(position);
            if(livesInfo.status == 0) {
                this.holder.img.setImageResource(ReUtils.getDrawableId(RecorderMachineView.this.context, "letv_recorder_angle_blue"));
                this.holder.txt.setTextColor(ReUtils.getColorId(RecorderMachineView.this.context, "letv_recorder_text_blue_color"));
            }

            this.holder.txt.setText(String.valueOf(position + 1));
            return convertView;
        }

        class ViewHolder {
            ImageView img;
            TextView txt;

            ViewHolder() {
            }
        }
    }
}
