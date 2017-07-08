package com.cs496.cs496project2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cs496.cs496project2.fragment.FriendsFragment;
import com.cs496.cs496project2.fragment.GalleryFragment;
import com.cs496.cs496project2.fragment.MatchFragment;
import com.cs496.cs496project2.fragment.ProfileFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new FriendsFragment();
            case 1: return new ProfileFragment();
            case 2: return new MatchFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
