package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.content.Context;

import com.example.android.flowercalendar.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private Context mContext;
    private int[] layouts;

    ViewPagerAdapter(BackgroundActivityExpandedDayView backgroundActivityExpandedDayView) {
        super(backgroundActivityExpandedDayView);
        layouts = new int[]{R.layout.testing_layout, R.layout.todo_list};
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            DailyScheduleEvents fragment = new DailyScheduleEvents();
            fragment.setContent(layouts[position]);
            return fragment;
        } else {
            ToDoList fragment = new ToDoList();
            fragment.setContent(layouts[position]);
            return fragment;

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
