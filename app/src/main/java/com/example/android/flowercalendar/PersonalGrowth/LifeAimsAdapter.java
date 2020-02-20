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
        layouts = new int[]{R.layout.activity_life_aims, R.layout.big_plan, R.layout.big_plan, R.layout.big_plan};
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
        }else{
            ThisMonthPlan fragment = new ThisMonthPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        }
    }


    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.lifeAims);
        } else if (position == 1) {
            return mContext.getString(R.string.bigPlan);

        } else if (position == 2) {
            return mContext.getString(R.string.thisYear);
        }else{
            return mContext.getString(R.string.thisMonth);
        }
    }

}

