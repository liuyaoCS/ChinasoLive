package com.chinaso.cl.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinaso.cl.R;
import com.chinaso.cl.adapter.TopTabPageAdapter;
import com.chinaso.cl.adapter.ViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ViewPager mViewPager =null;
    TabPageIndicator mIndicator;
    List<Fragment> mLists;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), getTestFragLists()));
        mViewPager.setAdapter(new TopTabPageAdapter(getChildFragmentManager(), getTestTitleLists(),getTestFragLists()));
        mViewPager.setCurrentItem(0);
        mIndicator= (TabPageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager,0);
        return view;
    }

    private List<Fragment> getTestFragLists() {
        List<Fragment> lists=new ArrayList<Fragment>();
        for(int i=0;i<2;i++){
            lists.add(new ViewPagerFragment1());
            //lists.add(new ViewPagerFragment2());
        }
        return lists;
    }
    private List<String> getTestTitleLists() {
        List<String> lists=new ArrayList<String>();
        for(int i=0;i<2;i++){
            lists.add("tab"+i);
        }
        return lists;
    }


}
