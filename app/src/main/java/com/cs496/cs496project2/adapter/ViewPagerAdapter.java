package com.cs496.cs496project2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cs496.cs496project2.fragment.ContactsFragment;
import com.cs496.cs496project2.fragment.GalleryFragment;
import com.cs496.cs496project2.fragment.MatchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new ContactsFragment();
            case 1: return new GalleryFragment();
            case 2: return new MatchFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
