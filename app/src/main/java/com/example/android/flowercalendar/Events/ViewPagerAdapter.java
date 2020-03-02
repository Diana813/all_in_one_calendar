package com.example.android.flowercalendar.Events;

import android.content.Context;

import com.example.android.flowercalendar.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int[] layouts;

    ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        layouts = new int[]{R.layout.testing_layout, R.layout.activity_expanded_day_view};
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            DailyScheduleEvents fragment = new DailyScheduleEvents();
            fragment.setContent(layouts[position]);
            return fragment;
        } else {
            EventsList fragment = new EventsList();
            fragment.setContent(layouts[position]);
            return fragment;

        }
    }


    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.dailySchedule);
        } else {
            return mContext.getString(R.string.ToDo);
        }
    }

}
