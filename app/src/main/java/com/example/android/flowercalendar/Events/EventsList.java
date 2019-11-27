package com.example.android.flowercalendar.Events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.MainActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class EventsList extends Fragment {

    private FloatingActionButton fab;
    private EventsListAdapter eventsListAdapter;
    private Context context;
    private RelativeLayout empty_view;
    static int newId;
    private String pickedDay;
    private String shiftNumber;
    private String shiftStart;
    private int shiftLength;

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
        empty_view = rootView.findViewById(R.id.empty_view_events);

        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        assert getArguments() != null;
        pickedDay = getArguments().getString("pickedDay");
        TextView date = rootView.findViewById(R.id.expandedDayDate);
        date.setText(pickedDay);
        onFabClick();
        initData();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate();


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
