package com.chinaso.cl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragLists;

    public ViewPagerAdapter(FragmentManager fm,List<Fragment> lists) {
        super(fm);
        fragLists=lists;
    }

    @Override
    public Fragment getItem(int position) {
        return fragLists.get(position);
    }

    @Override
    public int getCount() {
        return fragLists.size();
    }

}