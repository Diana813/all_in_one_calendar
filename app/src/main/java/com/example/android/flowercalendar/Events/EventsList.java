package com.example.android.flowercalendar.Events;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventsList extends Fragment {

    private FloatingActionButton fab;
    private EventsListAdapter eventsListAdapter;
    private FrequentActivitiesListAdapter frequentActivitiesListAdapter;
    private EventsListHoursAdapter eventsListHoursAdapter;
    private Context context;
    static int newId;
    private String pickedDay;
    private String shiftNumber;
    private String shiftStart;
    private int shiftLength;
    private DrawerLayout mDrawer;
    private RecyclerView hoursListRecyclerView;
    private ArrayList listOfHours;


    public EventsList() {
        // Required empty public constructor
    }

    public static EventsList newInstance() {
        return new EventsList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        eventsListAdapter = new EventsListAdapter(context, context);
        frequentActivitiesListAdapter = new FrequentActivitiesListAdapter(context, context);
        eventsListHoursAdapter = new EventsListHoursAdapter(context, context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsListAdapter.setIndexInDatabase();
        eventsListAdapter.deleteFromDatabase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_expanded_day_view, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.OneTimeEvents));
        fab = rootView.findViewById(R.id.fab);
        RecyclerView listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(frequentActivitiesListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        hoursListRecyclerView = rootView.findViewById(R.id.list_of_hours);
        hoursListRecyclerView.setAdapter(eventsListHoursAdapter);
        hoursListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelperHours = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListHoursAdapter));
        itemTouchHelperHours.attachToRecyclerView(hoursListRecyclerView);

        setHasOptionsMenu(true);
        mDrawer = rootView.findViewById(R.id.drawer_layout);
        mDrawer.setScrimColor(Color.TRANSPARENT);


        assert getArguments() != null;
        pickedDay = getArguments().getString("pickedDay");
        TextView date = rootView.findViewById(R.id.expandedDayDate);
        date.setText(pickedDay);
        onFabClick();
        initData();
        addFreqActivList();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate();

        addHoursList();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expanded_day_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.freqAct) {
            mDrawer = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer_layout);
            NavigationView navigationView = getActivity().findViewById(R.id.nvView);
            mDrawer.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                eventsListAdapter.setEventsList(events);
                newId = events.size();

            }
        });
    }

    private void addFreqActivList() {

        FrequentActivitiesViewModel eventsViewModel = ViewModelProviders.of(this).get(FrequentActivitiesViewModel.class);
        eventsViewModel.getEventsList().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                assert events != null;
                frequentActivitiesListAdapter.setEventsList(events);
            }
        });
    }

    private void addHoursList() {

        listOfHours = new ArrayList<String>();
        while(listOfHours.size() < 24) {
            listOfHours.add(new Hours("00:00", null));
        }

    }


   /* public void initWorkData() {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents isThereAShift = calendarEventsDao.findBypickedDate(pickedDay);

        if (isThereAShift != null && isThereAShift.getShiftNumber() != null) {
            shiftNumber = isThereAShift.getShiftNumber();
        }

        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
        Shift todaysShift = shiftsDao.findByShiftName(shiftNumber);
        if (todaysShift != null) {
            shiftStart = todaysShift.getSchedule();
            shiftLength = todaysShift.getShift_length();
        }


        EventsDao eventsDao = getDatabase(context).eventsDao();
        Event eventToUpdate = eventsDao.findByEventName("Work");
        if (eventToUpdate != null) {
            if ((!eventToUpdate.getEvent_name().equals("Work")) ||
                    (!eventToUpdate.getSchedule().equals(shiftStart)) ||
                    (eventToUpdate.getEvent_length() != shiftLength)) {
                eventToUpdate.setEvent_name("Work");
                eventToUpdate.setSchedule(shiftStart);
                eventToUpdate.setEvent_length(shiftLength);
                eventsDao.update(eventToUpdate);
            }
        } else {

            eventsDao.insert(new Event(newId, "Work", shiftStart, "", shiftLength, pickedDay));

        }


    }*/

}
