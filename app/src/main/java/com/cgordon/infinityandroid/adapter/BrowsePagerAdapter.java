package com.cgordon.infinityandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;

import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.fragment.UnitListFragment;

/**
 * Created by cgordon on 6/10/2015.
 */
public class BrowsePagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = BrowsePagerAdapter.class.getSimpleName();

    public BrowsePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new UnitListFragment();
        } else if (position == 1) {
            fragment  = new UnitFragment();
        } else {
            Log.e(TAG, "Asked for a BrowsePagerFragment position that doesn't exist: " + position);
        }
        return fragment;
    }

}
