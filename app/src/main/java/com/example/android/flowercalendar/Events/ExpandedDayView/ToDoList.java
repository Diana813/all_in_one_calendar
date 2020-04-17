package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.Events.EventsViewModel;
import com.example.android.flowercalendar.Events.FrequentActivities.FrequentActivitiesViewModel;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoList extends Fragment {

    private EventsListAdapter toDoListAdapter;
    private FrequentActivitiesDrawerListAdapter frequentActivitiesDrawerListAdapter;
    private Context context;
    public static int newId;
    private String pickedDay;
    private DrawerLayout mDrawer;
    private int layout;
    private RecyclerView freqActDrawList;
    private RecyclerView toDoListRecyclerView;
    private AppUtils appUtils = new AppUtils();
    private ImageButton confirm;
    private EditText editText;
    private TextView eventsLabel;


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
        this.context = context;
        toDoListAdapter = new EventsListAdapter(context);
        frequentActivitiesDrawerListAdapter = new FrequentActivitiesDrawerListAdapter(context, context);
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
        appUtils.setConfirmButtonEvents(confirm, toDoListAdapter, editText, 1, pickedDay, null, "0");
        return rootView;
    }

    private void findViews(View rootView) {
        freqActDrawList = rootView.findViewById(R.id.widgetGridView);
        toDoListRecyclerView = rootView.findViewById(R.id.list);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        confirm = rootView.findViewById(R.id.confirm_button);
        editText = rootView.findViewById(R.id.editText);
        eventsLabel = rootView.findViewById(R.id.eventsLabel);
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

    void saveEvent(String newEvent, String pickedDate, EventsListAdapter toDoListAdapter) {
        appUtils.saveDataEvents(toDoListAdapter, null, pickedDate, newEvent, "0");
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

        if (item.getItemId() == R.id.freqAct) {
            mDrawer = Objects.requireNonNull(getActivity()).findViewById(R.id.activity_expanded_day_view);
            NavigationView navigationView = getActivity().findViewById(R.id.nvView);
            mDrawer.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initData() {

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            toDoListAdapter.setEventsList(events);
            newId = events.size();
            AppUtils.updateWidget(context);
        });
    }

    private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }

}
