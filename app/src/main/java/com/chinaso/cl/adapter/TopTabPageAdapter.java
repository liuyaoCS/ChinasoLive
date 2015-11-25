package com.chinaso.cl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ly on 2015/11/15.
 */
public class TopTabPageAdapter extends FragmentPagerAdapter{
    List<String> mTitleSets;
    List<Fragment> mFragSets;
    public TopTabPageAdapter(FragmentManager fm, List<String> titleSets, List<Fragment> fragSets) {
        super(fm);
        mTitleSets =titleSets;
        mFragSets=fragSets;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragSets.get(position);
    }

    @Override
    public int getCount() {
        return mTitleSets.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleSets.get(position);
    }
}
