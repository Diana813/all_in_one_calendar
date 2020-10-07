package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.LifeAimsViewPagerAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DownloadImageUtils.getImageFromInternet;

public class BackgroundActivity extends Fragment {

    public BackgroundActivity() {
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
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);


        LifeAimsViewPagerAdapter adapter = new LifeAimsViewPagerAdapter(this) {
        };
        ViewPager2 viewPager = rootView.findViewById(R.id.viewpager);
        TabLayout tabs = rootView.findViewById(R.id.tabs);
        viewPager.setAdapter(adapter);

        String[] titles = new String[]{getString(R.string.lifeAims), getString(R.string.bigPlan), getString(R.string.thisYear), getString(R.string.thisMonth), getString(R.string.dayPlan)};

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

        viewPager.setPageTransformer(new DepthPageTransformer());

        if (getArguments() != null) {
            Uri receivedUri;
            String message;
            if (getArguments().getParcelable("image") != null) {
                receivedUri = getArguments().getParcelable("image");
                message = "";
                LifeAims.getMessage(message);
                getImageFromInternet(receivedUri);
            } else if (getArguments().get("message") != null) {
                message = getArguments().getString("message");
                receivedUri = null;
                getImageFromInternet(receivedUri);
                LifeAims.getMessage(message);
            }
        }

        return rootView;
    }

}

