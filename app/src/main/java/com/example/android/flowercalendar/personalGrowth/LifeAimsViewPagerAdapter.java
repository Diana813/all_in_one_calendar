package com.example.android.flowercalendar.personalGrowth;

import com.example.android.flowercalendar.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LifeAimsViewPagerAdapter extends FragmentStateAdapter {

    private int[] layouts;

    LifeAimsViewPagerAdapter(BackgroundActivity backgroundActivity) {
        super(backgroundActivity);

        layouts = new int[]{R.layout.activity_life_aims, R.layout.personal_growth_plans_layout, R.layout.personal_growth_plans_layout, R.layout.personal_growth_plans_layout, R.layout.personal_growth_plans_layout};
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            LifeAims fragment = new LifeAims();
            fragment.setContent(layouts[position]);
            return fragment;
        } else if (position == 1) {
            FiveYearsPlan fragment = new FiveYearsPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        } else if (position == 2) {
            OneYearPlan fragment = new OneYearPlan();
            fragment.setContent(layouts[position]);
            return fragment;
        } else if (position == 3) {
            ThisMonthPlan fragment = new ThisMonthPlan();
            fragment.setContent(layouts[position]);
            return fragment;

        } else {
            OneDayPlan fragment = new OneDayPlan();
            fragment.setContent(layouts[position]);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}

