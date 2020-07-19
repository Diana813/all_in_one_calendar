package com.dianaszczepankowska.AllInOneCalendar.android.coworkers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class CoworkerFragment extends Fragment {

    public CoworkerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_coworker, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.Coworkers));
        return rootView;
    }

}
