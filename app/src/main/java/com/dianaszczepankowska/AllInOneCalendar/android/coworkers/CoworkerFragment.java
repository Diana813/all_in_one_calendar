package com.dianaszczepankowska.AllInOneCalendar.android.coworkers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ColleaguesListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoworkerFragment extends Fragment {

    private ColleaguesListAdapter colleaguesListAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        colleaguesListAdapter = new ColleaguesListAdapter(context);
    }


    public CoworkerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_coworker, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.Coworkers));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);
        MainActivity.menu.findItem(R.id.events).setIcon(R.drawable.baseline_perm_contact_calendar_black_48).setChecked(true).setOnMenuItemClickListener(item ->{
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new CoworkerFragment()).addToBackStack("tag").commit();
            return true;
        });


        colleaguesListAdapter.setUsersList();
        RecyclerView friendsRecyclerView = rootView.findViewById(R.id.friendsList);
        friendsRecyclerView.setAdapter(colleaguesListAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

}
