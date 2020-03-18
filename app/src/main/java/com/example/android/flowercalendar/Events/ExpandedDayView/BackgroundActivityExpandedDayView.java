package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.DepthPageTransformer;
import com.example.android.flowercalendar.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class BackgroundActivityExpandedDayView extends Fragment {

    private String pickedDay;

    public BackgroundActivityExpandedDayView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.background_viewpager, container, false);
        pickedDay = pickedDate();
        Objects.requireNonNull(getActivity()).setTitle(pickedDay);

        // Create an adapter that knows which fragment should be shown on each page
        ViewPagerAdapter adapter = new ViewPagerAdapter(this) {
        };
        ViewPager2 viewPager = rootView.findViewById(R.id.viewpager);
        TabLayout tabs = rootView.findViewById(R.id.tabs);
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Connect the tab layout with the view pager.
        String[] titles = new String[]{getString(R.string.dailySchedule), getString(R.string.ToDo)};

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

        viewPager.setPageTransformer(new DepthPageTransformer());
        return rootView;
    }

    private String pickedDate() {
        assert getArguments() != null;
        return getArguments().getString("pickedDay");

    }


    public String getPickedDate() {
        return pickedDay;
    }


}
