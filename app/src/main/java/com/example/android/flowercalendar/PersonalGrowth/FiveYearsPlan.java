package com.example.android.flowercalendar.PersonalGrowth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FiveYearsPlan extends Fragment {

    private int layout;

    public FiveYearsPlan() {
        // Required empty public constructor
    }

    public void setContent(int layout){
        this.layout=layout;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.LifeAims));
        return rootView;
    }
}

