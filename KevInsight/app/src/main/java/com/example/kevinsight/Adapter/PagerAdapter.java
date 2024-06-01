package com.example.kevinsight.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kevinsight.Fragment.EntertainmentFragment;
import com.example.kevinsight.Fragment.HealthFragment;
import com.example.kevinsight.Fragment.HomeFragment;
import com.example.kevinsight.Fragment.ScienceFragment;
import com.example.kevinsight.Fragment.SearchFragment;
import com.example.kevinsight.Fragment.SportsFragment;
import com.example.kevinsight.Fragment.TechnologyFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int tabcount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new SearchFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
