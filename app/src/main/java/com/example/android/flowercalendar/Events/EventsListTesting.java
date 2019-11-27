package com.example.android.flowercalendar.Events;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.Calendar.CalendarAdapter;
import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventsListTesting extends Fragment {

    private FloatingActionButton fab;
    private TestingListAdapter testingListAdapter;
    private Context context;
    private RelativeLayout empty_view;
    static int newId;
    private String pickedDay;
    private String shiftNumber;
    private String shiftStart;
    private int shiftLength;
    private ArrayList<Event> hoursArrayList;
    TextView hourTextView;
    private RecyclerView recyclerView;

    public EventsListTesting() {
        // Required empty public constructor
    }

    public static EventsListTesting newInstance() {
        return new EventsListTesting();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        testingListAdapter = new TestingListAdapter(context, context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        testingListAdapter.setIndexInDatabase();
        testingListAdapter.deleteFromDatabase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_expanded_day_view, container, false);

        fab = rootView.findViewById(R.id.fab);
        empty_view = rootView.findViewById(R.id.empty_view_events);


        recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(testingListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(testingListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        assert getArguments() != null;
        pickedDay = getArguments().getString("pickedDay");
        TextView date = rootView.findViewById(R.id.expandedDayDate);
        date.setText(pickedDay);
        onFabClick();
        initData();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate();

        hourTextView = rootView.findViewById(R.id.hour);
        addingHoursToThePlanner();


        return rootView;
    }

    private void onFabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeEvents oneTimeEvents = new OneTimeEvents();
                Bundle args = new Bundle();
                args.putString("pickedDay", pickedDay);
                oneTimeEvents.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.flContent, oneTimeEvents).commit();
            }
        });
    }

    private void initData() {

        EventsViewModel eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel.class);
        eventsViewModel.getEventsList().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                assert events != null;
                testingListAdapter.setEventsList(events);
                newId = events.size();
                if (events.isEmpty()) {
                    empty_view.setVisibility(View.VISIBLE);
                } else {
                    empty_view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addingHoursToThePlanner() {
        hoursArrayList = new ArrayList<>();

        TestingListAdapter planerAdapter = new TestingListAdapter(context,context);
        int hour = 0;
        int minute = 0;
        for(int i = 0; i<24; i ++){
            hourTextView.setText(String.format("%02d:%02d", hour, minute));
            hour++;
        }

        recyclerView.setAdapter(planerAdapter);
    }
}

