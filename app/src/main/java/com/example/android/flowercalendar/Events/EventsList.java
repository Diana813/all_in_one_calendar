package com.example.android.flowercalendar.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Shifts.ShiftsEditor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class EventsList extends Fragment {

    FloatingActionButton fab;


    public EventsList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_expanded_day_view, container, false);

        fab = rootView.findViewById(R.id.fab);
        onFabClick();

        return rootView;
    }

    private void onFabClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction tx =
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, new OneTimeEvents());
                tx.commit();
            }
        });
    }
}
