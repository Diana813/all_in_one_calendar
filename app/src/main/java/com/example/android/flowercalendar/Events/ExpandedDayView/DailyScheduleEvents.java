package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.OneTimeEvents;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DailyScheduleEvents extends Fragment {

    private FloatingActionButton fab;
    private EventsListHoursAdapter testingListAdapter;
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
    private int layout;

    public DailyScheduleEvents() {
        // Required empty public constructor
    }

    public static DailyScheduleEvents newInstance() {
        return new DailyScheduleEvents();
    }

    public void setContent(int layout) {
        this.layout = layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        testingListAdapter = new EventsListHoursAdapter(context, context);
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

        View rootView = inflater.inflate(layout, container, false);

        fab = rootView.findViewById(R.id.fab);
        empty_view = rootView.findViewById(R.id.empty_view_events);


        recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(testingListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(testingListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //assert getArguments() != null;
//        pickedDay = getArguments().getString("pickedDay");
        //TextView date = rootView.findViewById(R.id.expandedDayDate);
       // date.setText(pickedDay);
        //onFabClick();
        //initData();
        //CalendarFragment calendarFragment = new CalendarFragment();
        //calendarFragment.saveEventsNumberToPickedDate();

       // hourTextView = rootView.findViewById(R.id.hour);
      //  addingHoursToThePlanner();


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

   /* private void initData() {

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
*/
    private void addingHoursToThePlanner() {
        hoursArrayList = new ArrayList<>();

        EventsListHoursAdapter planerAdapter = new EventsListHoursAdapter(context,context);
        int hour = 0;
        int minute = 0;
        for(int i = 0; i<24; i ++){
            hourTextView.setText(String.format("%02d:%02d", hour, minute));
            hour++;
        }

        recyclerView.setAdapter(planerAdapter);
    }
}

