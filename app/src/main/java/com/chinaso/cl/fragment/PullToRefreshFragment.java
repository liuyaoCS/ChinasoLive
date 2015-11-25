package com.chinaso.cl.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chinaso.cl.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PullToRefreshFragment extends Fragment {

    PullToRefreshListView mListView;
    public PullToRefreshListView getPullToRefreshListView(){
        return mListView;
    }
    public PullToRefreshFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pulltorefresh, container, false);
        mListView = (PullToRefreshListView) view.findViewById(R.id.list_view);
        mListView.setMode(Mode.BOTH);
        return view;
    }
    public void sendPullFinishSignal(){
        mListView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mListView.onRefreshComplete();
            }
        }, 100);
    }

}
