package com.example.iventcalendar.ui.settings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabFirstPhotos;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabSecondLocation;
import com.example.iventcalendar.activities.tabs.settings_tabs.TabThirdPeople;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch(position) {
            case 0:
                TabFirstPhotos photosSettings = new TabFirstPhotos();
                return photosSettings;
            case 1:
                TabSecondLocation locationSettings = new TabSecondLocation();
                return locationSettings;
            case 2:
                TabThirdPeople peopleSettings = new TabThirdPeople();
                return peopleSettings;
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}