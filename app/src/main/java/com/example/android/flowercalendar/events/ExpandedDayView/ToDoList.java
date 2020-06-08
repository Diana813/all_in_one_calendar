package com.example.android.flowercalendar.events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.events.EventsListAdapter;
import com.example.android.flowercalendar.events.EventsViewModel;
import com.example.android.flowercalendar.events.FrequentActivities.FrequentActivitiesViewModel;
import com.example.android.flowercalendar.gestures.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoList extends Fragment {

    private static int eventsListSize;
    private EventsListAdapter toDoListAdapter;
    private FrequentActivitiesDrawerListAdapter frequentActivitiesDrawerListAdapter;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String pickedDay;
    private DrawerLayout mDrawer;
    private int layout;
    private RecyclerView freqActDrawList;
    private RecyclerView toDoListRecyclerView;
    private AppUtils appUtils;
    private ImageButton confirm;
    private EditText editText;
    private TextView eventsLabel;
    private Button freqActButton;
    private NavigationView navigationView;


    public ToDoList() {
        // Required empty public constructor
    }

    public static ToDoList newInstance() {
        return new ToDoList();
    }

    public void setContent(int layout) {
        this.layout = layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ToDoList.context = context;
        toDoListAdapter = new EventsListAdapter(context);
        frequentActivitiesDrawerListAdapter = new FrequentActivitiesDrawerListAdapter(context);
        appUtils = new AppUtils();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        toDoListAdapter.setIndexInDB();
        toDoListAdapter.deleteFromDatabase(null);
        setNumberOfEventsToPickedDate(pickedDay);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout, container, false);

        findViews(rootView);
        eventsLabel.setVisibility(View.GONE);
        editText.setTextColor(Color.BLACK);
        setAdapters();
        setHasOptionsMenu(true);
        pickedDay = findWhatDateItIs();
        initData();
        addFreqActivList();
        displayCyclicalEvents();
        appUtils.setConfirmButtonEvents(confirm, toDoListAdapter, editText, pickedDay, null, "0", "", 1);
        return rootView;
    }

    private void findViews(View rootView) {
        freqActDrawList = rootView.findViewById(R.id.widgetGridView);
        toDoListRecyclerView = rootView.findViewById(R.id.list);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        confirm = rootView.findViewById(R.id.confirm_button);
        editText = rootView.findViewById(R.id.editText);
        eventsLabel = rootView.findViewById(R.id.eventsLabel);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        navigationView = rootView.findViewById(R.id.nvView);
    }

    private void setAdapters() {
        freqActDrawList.setAdapter(frequentActivitiesDrawerListAdapter);
        freqActDrawList.setLayoutManager(new LinearLayoutManager(context));
        toDoListRecyclerView.setAdapter(toDoListAdapter);
        toDoListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(toDoListAdapter));
        itemTouchHelper.attachToRecyclerView(toDoListRecyclerView);
        mDrawer.setScrimColor(Color.TRANSPARENT);

    }

    void saveEvent(String newEvent, String pickedDate) {
        AppUtils appUtils = new AppUtils();
        appUtils.saveDataEvents(null, pickedDate, newEvent, "-1", "", 1);
    }

    private String findWhatDateItIs() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BackgroundActivityExpandedDayView) {
            pickedDay = ((BackgroundActivityExpandedDayView) parentFragment).getPickedDate();
        }
        return pickedDay;
    }


    public void setNumberOfEventsToPickedDate(String pickedDay) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate(pickedDay);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expanded_day_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog(1);
            return true;
        } else if (item.getItemId() == R.id.action_open_drawer) {
            mDrawer.openDrawer(navigationView);
            toDoListAdapter.deleteFromDatabase(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static void showDeleteConfirmationDialog(int eventKind) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> removeData(eventKind));
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private static void removeData(int eventKind) {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        eventsDao.deleteByPickedDate(pickedDay, eventKind);
    }


    private void initData() {

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            toDoListAdapter.setEventsList(events);
            eventsListSize = events.size();

        });
    }


    private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }


    public static int positionOfTheNextEventOnTheList() {
        return eventsListSize + 1;
    }


    private void displayCyclicalEvents() {

        if (!CalendarFragment.listOfCyclicalEvents.isEmpty()) {

            for (String cyclicalEvent : CalendarFragment.listOfCyclicalEvents) {
                String[] parts = cyclicalEvent.split(";");

                if (parts[0].equals(pickedDay)) {
                    EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
                    Event event = eventsDao.findByEventKindAndName(parts[1], 0);
                    if (event != null) {
                        appUtils.saveDataEvents(null, pickedDay, event.getEvent_name(), event.getFrequency(), event.getSchedule(), 1);

                    }

                }
            }
        }

    }

}
