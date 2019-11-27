package com.example.android.flowercalendar.PersonalGrowth;

import android.content.Context;

import com.example.android.flowercalendar.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LifeAimsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int[] layouts;

    LifeAimsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        layouts = new int[]{R.layout.activity_life_aims, R.layout.activity_five_years_plan};
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            LifeAims fragment = new LifeAims();
            fragment.setContent(layouts[position]);
            return fragment;
        } else {
            FiveYearsPlan fragment = new FiveYearsPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.lifeAims);
        } else {
            return mContext.getString(R.string.bigPlan);
        }
    }
}

