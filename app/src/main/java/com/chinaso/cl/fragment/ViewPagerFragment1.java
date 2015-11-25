package com.chinaso.cl.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinaso.cl.adapter.ChildAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import recorder.activity.PlayActivity;
import recorder.net.NetworkService;
import recorder.net.model.LiveVideoInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment1 extends PullToRefreshFragment implements AdapterView.OnItemClickListener{

    PullToRefreshListView childListView;
    List<String> mDatas;
    public ViewPagerFragment1() {
        // Required empty public constructor
        mDatas=new ArrayList<String>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View base=super.onCreateView(inflater,container,savedInstanceState);
        childListView=getPullToRefreshListView();
        childListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉的时候数据重置
                sendPullFinishSignal();
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉的时候添加选项
                sendPullFinishSignal();
            }

        });
        childListView.setOnItemClickListener(this);

        loadVideoList();

        return base;
    }

    private void loadVideoList() {
        NetworkService.getInstance().getVideoList(new Callback<List<LiveVideoInfo>>() {
            @Override
            public void success(List<LiveVideoInfo> liveVideoInfos, Response response) {
                for(LiveVideoInfo lvi:liveVideoInfos){
                    mDatas.add(lvi.getLetvid());
                }
                childListView.setAdapter(new ChildAdapter(getActivity(),mDatas));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.i("ly","get video list err:"+retrofitError);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        String activityId=mDatas.get(position-1);
        Log.i("ly","activiyId="+activityId);

        intent.putExtra("activityID",activityId);
        getActivity().startActivity(intent);
    }
}
