package com.example.android.flowercalendar.PersonalGrowth;

import android.content.Context;

import com.example.android.flowercalendar.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LifeAimsViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int[] layouts;

    LifeAimsViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        layouts = new int[]{R.layout.activity_life_aims, R.layout.personal_growth_plans_layout, R.layout.personal_growth_plans_layout, R.layout.personal_growth_plans_layout,R.layout.personal_growth_plans_layout};
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            LifeAims fragment = new LifeAims();
            fragment.setContent(layouts[position]);
            return fragment;
        } else if (position == 1) {
            BigPlan fragment = new BigPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        } else if(position == 2) {
            OneYearPlan fragment = new OneYearPlan();
            fragment.setContent(layouts[position]);
            return fragment;
        }else if(position == 3){
            ThisMonthPlan fragment = new ThisMonthPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        }else {
            OneDayPlan fragment = new OneDayPlan();
            fragment.setContent(layouts[position]);
            return fragment;
        }
    }


    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.lifeAims);
        } else if (position == 1) {
            return mContext.getString(R.string.bigPlan);

        } else if (position == 2) {
            return mContext.getString(R.string.thisYear);
        }else if(position == 3){
            return mContext.getString(R.string.thisMonth);
        }else{
            return mContext.getString(R.string.dayPlan);
        }
    }

}

