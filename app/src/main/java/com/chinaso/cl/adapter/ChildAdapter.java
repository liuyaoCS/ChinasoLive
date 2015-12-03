package com.chinaso.cl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaso.cl.R;
import com.chinaso.cl.activity.MainActivity;

import java.util.List;

import com.recorder.net.model.LiveVideoListInfo;

/**
 * Created by ly on 2015/11/14.
 */
public class ChildAdapter extends BaseAdapter{
    List<LiveVideoListInfo> mDataSets;
    Context mContext;
    public ChildAdapter(Context context,List<LiveVideoListInfo> datas){
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
            viewHolder.avatar=(ImageView)convertView.findViewById(R.id.avatar);
            viewHolder.cover= (ImageView) convertView.findViewById(R.id.cover);
            viewHolder.uname= (TextView) convertView.findViewById(R.id.uname);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.uname.setText(mDataSets.get(position).getName());
        ((MainActivity)mContext).getImageCacheManager().loadImage(mDataSets.get(position).getAvatar(), viewHolder.avatar, 0, 0);
        ((MainActivity)mContext).getImageCacheManager().loadImage(mDataSets.get(position).getCover(),viewHolder.cover,0,0);
        //viewHolder.cover.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.cover));
        return convertView;
    }
    class ViewHolder{
        ImageView cover;
        ImageView avatar;
        TextView uname;
    }
}
