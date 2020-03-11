package com.example.android.flowercalendar.PersonalGrowth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.DepthPageTransformer;
import com.example.android.flowercalendar.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
        LifeAimsViewPagerAdapter adapter = new LifeAimsViewPagerAdapter(getContext(), getChildFragmentManager()) {
        };


        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        TabLayout tabs = rootView.findViewById(R.id.tabs);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Connect the tab layout with the view pager.
        tabs.setupWithViewPager(viewPager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        return rootView;

    }



}

