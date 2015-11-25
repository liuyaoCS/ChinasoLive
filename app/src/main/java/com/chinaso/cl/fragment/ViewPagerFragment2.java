package com.chinaso.cl.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaso.cl.R;
import com.chinaso.cl.adapter.ChildAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment2 extends PullToRefreshFragment {

    PullToRefreshListView childListView;
    public ViewPagerFragment2() {
        // Required empty public constructor
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
        childListView.setAdapter(new ChildAdapter(getActivity(), getTestDatas()));
        return base;
    }
    public List<String> getTestDatas(){
        List<String> datas=new ArrayList<String>();
        for(int i=0;i<20;i++){
            datas.add(i,"i am item "+i);
        }
        return datas;
    }


}
