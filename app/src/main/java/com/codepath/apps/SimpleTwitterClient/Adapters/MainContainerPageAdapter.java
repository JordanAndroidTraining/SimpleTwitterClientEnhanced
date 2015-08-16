package com.codepath.apps.SimpleTwitterClient.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.SimpleTwitterClient.Fragments.HomeTimeLineFragment;

/**
 * Created by jordanhsu on 8/17/15.
 */
public class MainContainerPageAdapter extends FragmentPagerAdapter {
    private String[] mTabTitle = {"Home"};

    public MainContainerPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

                return HomeTimeLineFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

    @Override
    public int getCount() {
        return mTabTitle.length;
    }
}
