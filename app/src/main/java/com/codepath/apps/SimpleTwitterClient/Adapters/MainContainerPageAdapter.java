package com.codepath.apps.SimpleTwitterClient.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.SimpleTwitterClient.Fragments.DirectMsgFragment;
import com.codepath.apps.SimpleTwitterClient.Fragments.TimeLineHomeFragment;
import com.codepath.apps.SimpleTwitterClient.Fragments.TimeLineMentionFragment;
import com.codepath.apps.SimpleTwitterClient.Fragments.ProfileViewFragment;
import com.codepath.apps.SimpleTwitterClient.R;

/**
 * Created by jordanhsu on 8/17/15.
 */
public class MainContainerPageAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private String[] mTabTitle = {"Home","Mentions","DirectMsg","Me"};
    private int tabIcons[] = {R.drawable.ic_twitter_home_gray
                            ,R.drawable.ic_mention_gray
                            ,R.drawable.ic_twitter_direct_msg_gray
                            ,R.drawable.ic_twitter_profile_gray};

    public MainContainerPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

                return TimeLineHomeFragment.newInstance();
            case 1:
                return TimeLineMentionFragment.newInstance();
            case 2:
                return DirectMsgFragment.newInstance();
            case 3:
                return ProfileViewFragment.newInstance();
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

    @Override
    public int getPageIconResId(int i) {
        return tabIcons[i];
    }

}
