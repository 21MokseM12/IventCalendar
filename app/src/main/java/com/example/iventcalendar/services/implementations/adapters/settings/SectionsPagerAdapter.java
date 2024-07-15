package com.example.iventcalendar.services.implementations.adapters.settings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.iventcalendar.R;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabFirstPhotos;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabSecondLocation;
import com.example.iventcalendar.entities.tabs.settings_tabs.TabThirdPeople;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private final String date;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String date) {
        super(fm);
        mContext = context;
        this.date = date;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return TabFirstPhotos.newInstance(date);
            case 1: return new TabSecondLocation();
            case 2: return new TabThirdPeople();
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
        return 3;
    }
}