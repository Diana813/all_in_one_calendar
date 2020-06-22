package com.example.android.flowercalendar.events.expandedDayView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.events.eventsUtils.EventsListAdapter;
import com.example.android.flowercalendar.events.eventsUtils.EventsViewModel;
import com.example.android.flowercalendar.events.eventsUtils.UtilsEvents;
import com.example.android.flowercalendar.events.frequentActivities.FrequentActivitiesViewModel;
import com.example.android.flowercalendar.gestures.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.utils.AppUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoList extends ExpandedDayEvents {


    private EventsListAdapter toDoListAdapter;
    private FrequentActivitiesDrawerListAdapter frequentActivitiesDrawerListAdapter;
    private int layout;
    private RecyclerView freqActDrawList;
    private RecyclerView toDoListRecyclerView;
    private ImageButton confirm;
    private EditText editText;
    private TextView eventsLabel;


    public void setContent(int layout) {
        this.layout = layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toDoListAdapter = new EventsListAdapter(context);
        frequentActivitiesDrawerListAdapter = new FrequentActivitiesDrawerListAdapter(context);
    }


    @Override
    public void onPause() {
        super.onPause();
        toDoListAdapter.setIndexInDB();
        toDoListAdapter.deleteFromDatabase(null);
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
        UtilsEvents.setConfirmButtonEvents(confirm, toDoListAdapter, editText, pickedDay, null, "0", "", 1);
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
        UtilsEvents.saveDataEvents(null, pickedDate, newEvent, "-1", "", 1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog(1);
            initData();
            return true;
        } else if (item.getItemId() == R.id.action_open_drawer) {
            mDrawer.openDrawer(navigationView);
            toDoListAdapter.deleteFromDatabase(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
