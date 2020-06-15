package com.example.android.flowercalendar.personalGrowth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.gestures.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class BackgroundActivity extends Fragment {

    public BackgroundActivity() {
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
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.LifeAims));


        // Create an adapter that knows which fragment should be shown on each page
        LifeAimsViewPagerAdapter adapter = new LifeAimsViewPagerAdapter(this) {
        };


        ViewPager2 viewPager = rootView.findViewById(R.id.viewpager);
        TabLayout tabs = rootView.findViewById(R.id.tabs);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Connect the tab layout with the view pager.

        String[] titles = new String[]{getString(R.string.lifeAims), getString(R.string.bigPlan), getString(R.string.thisYear), getString(R.string.thisMonth), getString(R.string.dayPlan)};

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

        viewPager.setPageTransformer(new DepthPageTransformer());


        return rootView;

    }

}

