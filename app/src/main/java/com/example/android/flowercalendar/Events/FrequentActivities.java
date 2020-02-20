package com.example.android.flowercalendar.Events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrequentActivities extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private EventsListAdapter eventsListAdapter;
    private FloatingActionButton fab;
    private RelativeLayout empty_view;
    static int newId;


    public FrequentActivities() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        eventsListAdapter = new EventsListAdapter(context, context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        eventsListAdapter.setIndexInDatabase();
        eventsListAdapter.deleteFromDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsListAdapter.setIndexInDatabase();
        eventsListAdapter.deleteFromDatabase();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrequentActivities.
     */
    // TODO: Rename and change types and number of parameters
    public static FrequentActivities newInstance(String param1, String param2) {
        FrequentActivities fragment = new FrequentActivities();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frequent_activities, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.FrequentActivities));

        fab = rootView.findViewById(R.id.fab);
        empty_view = rootView.findViewById(R.id.empty_view);


        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_freq_activ);
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        onFabClick();
        initData();
        return rootView;
    }

    private void initData() {

        FrequentActivitiesViewModel eventsViewModel = ViewModelProviders.of(this).get(FrequentActivitiesViewModel.class);
        eventsViewModel.getEventsList().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                assert events != null;
                eventsListAdapter.setEventsList(events);
                newId = events.size();
                if (events.isEmpty()) {
                    empty_view.setVisibility(View.VISIBLE);
                } else {
                    empty_view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void onFabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeEvents oneTimeEvents = new OneTimeEvents();
                Bundle args = new Bundle();
                args.putString("pickedDay", null);
                oneTimeEvents.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.flContent, oneTimeEvents).commit();
            }
        });
    }



}
