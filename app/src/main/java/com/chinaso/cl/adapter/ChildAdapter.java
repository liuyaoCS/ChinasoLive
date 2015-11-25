package com.chinaso.cl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinaso.cl.R;

import java.util.List;

/**
 * Created by ly on 2015/11/14.
 */
public class ChildAdapter extends BaseAdapter{
    List<String> mDataSets;
    Context mContext;
    public ChildAdapter(Context context,List<String> datas){
        mDataSets =datas;
        mContext=context;
    }

    @Override
    public int getCount() {
        return mDataSets.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.child_item_view,null,false);
            viewHolder=new ViewHolder();
            viewHolder.tv= (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
;        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(mDataSets.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView tv;
    }
}
