package com.example.iventcalendar.ui.info;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.info_tabs.TabFirstPhotos;
import com.example.iventcalendar.activities.tabs.info_tabs.TabFourthCrazyCount;
import com.example.iventcalendar.activities.tabs.info_tabs.TabSecondLocation;
import com.example.iventcalendar.activities.tabs.info_tabs.TabThirdPeople;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<String> fragmentDataList;
    private String date;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, List<String> data, String date) {
        super(fm);
        mContext = context;
        this.fragmentDataList = data;
        this.date = date;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch(position) {
            case 0:
                return TabFirstPhotos.newInstance(fragmentDataList.get(position), date);
            case 1:
                return TabSecondLocation.newInstance(fragmentDataList.get(position));
            case 2:
                return TabThirdPeople.newInstance(fragmentDataList.get(position));
            case 3:
                return TabFourthCrazyCount.newInstance(fragmentDataList.get(position));
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
        // Show 4 total pages.
        return 4;
    }
}