package com.android.wassally.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.wassally.fragment.CompletedFragment;
import com.android.wassally.fragment.InProgressFragment;
import com.android.wassally.fragment.WaitingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new WaitingFragment();
            case 1 :
                return new InProgressFragment();
            case 2 :
                return new CompletedFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Waiting";
            case 1 :
                return "In Progress";
            case 2 :
                return "Completed";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
