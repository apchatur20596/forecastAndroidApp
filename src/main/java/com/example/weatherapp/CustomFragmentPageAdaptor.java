package com.example.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class CustomFragmentPageAdaptor extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public CustomFragmentPageAdaptor(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }
}
