package com.developertest.sterlingproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragmentList;
    private String[] tabTitles;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.tabFragmentList = fragments;
        this.tabTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragmentList.get(position % tabFragmentList.size());
    }

    @Override
    public int getCount() {
        return tabFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(tabTitles != null && tabTitles.length > 0){
            return  tabTitles[position];
        }
        return null;
    }
}
