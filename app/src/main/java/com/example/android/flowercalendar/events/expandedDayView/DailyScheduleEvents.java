package com.example.android.flowercalendar.events.expandedDayView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.calendar.CalendarUtils;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.events.frequentActivities.FrequentActivitiesViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.String.format;

public class DailyScheduleEvents extends ExpandedDayEvents {

    private ExpandableListView expandableListView;
    private int layout;
    private String newHour;
    private FreqActAdapterSchedule frequentActivitiesDrawerListAdapter;

    @Override
    public void onPause() {
        super.onPause();
        CalendarUtils.saveEventsNumberToPickedDate(pickedDay, context);
    }


    public void setContent(int layout) {
        this.layout = layout;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        frequentActivitiesDrawerListAdapter = new FreqActAdapterSchedule(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(layout, container, false);
        setHasOptionsMenu(true);
        expandableListView = rootView.findViewById(R.id.expandableListView);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        mDrawer.setScrimColor(Color.TRANSPARENT);
        navigationView = rootView.findViewById(R.id.nvView);
        RecyclerView freqActDrawList = rootView.findViewById(R.id.widgetGridView);
        addListData();
        addFreqActivList();
        freqActDrawList.setAdapter(frequentActivitiesDrawerListAdapter);
        freqActDrawList.setLayoutManager(new LinearLayoutManager(context));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem hour = menu.findItem(R.id.action_add_hour);
        hour.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog(3);
            return true;
        } else if (item.getItemId() == R.id.action_open_drawer) {
            mDrawer.openDrawer(navigationView);
            return true;
        } else if (item.getItemId() == R.id.action_add_hour) {
            timeSettingDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("DefaultLocale")
    private LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        pickedDay = findWhatDateItIs();

        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        List<Event> scheduledEventsList = eventsDao.sortByPickedDay(pickedDay, 3);

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(getContext()).calendarEventsDao();

        String currentShift;
        if (calendarEventsDao.findBypickedDate(pickedDay) == null) {
            currentShift = null;
        } else {
            currentShift = calendarEventsDao.findBypickedDate(pickedDay).getShiftNumber();

        }
        ShiftsDao shiftsDao = CalendarDatabase.getDatabase(getContext()).shiftsDao();

        String shiftSchedule = null;
        String shiftFinish = null;
        if (shiftsDao.findByShiftName(currentShift) != null) {
            shiftSchedule = shiftsDao.findByShiftName(currentShift).getSchedule();
            int shiftLenght = shiftsDao.findByShiftName(currentShift).getShift_length();

            if (!shiftSchedule.equals("")) {
                String[] split = shiftSchedule.split(":");
                int shiftStartHour = Integer.parseInt(split[0]);
                int shiftStartMinutes = Integer.parseInt(split[1]);

                shiftFinish = String.valueOf(LocalTime.of(shiftStartHour, shiftStartMinutes).plusHours(shiftLenght));
            }
        }


        if (!scheduledEventsList.isEmpty()) {
            for (int i = 0; i < 24; i++) {
                List<String> hour = new ArrayList<>();
                expandableListDetail.put(((format("%02d", i) + ":00")), hour);

                for (Event event : scheduledEventsList) {
                    String addedHour = scheduledEventsList.get(scheduledEventsList.indexOf(event)).getSchedule();
                    if (addedHour != null) {
                        String[] parts = addedHour.split(":");
                        if (format("%02d", i).equals(parts[0]) && !(format("%02d", i) + ":00").equals(addedHour)) {
                            hour.add(addedHour);
                        }
                    }
                }


                if (newHour != null) {
                    String[] parts = newHour.split(":");
                    if (format("%02d", i).equals(parts[0]) && !(format("%02d", i) + ":00").equals(newHour)) {
                        hour.add(newHour);
                    }
                }
            }
        } else {
            for (int i = 0; i < 24; i++) {
                List<String> hour = new ArrayList<>();
                expandableListDetail.put(((format("%02d", i) + ":00")), hour);

                if (shiftSchedule != null && !shiftSchedule.equals("")) {
                    String[] parts = shiftSchedule.split(":");
                    if (format("%02d", i).equals(parts[0]) && !(format("%02d", i) + ":00").equals(shiftSchedule)) {
                        hour.add(shiftSchedule);
                    }
                }

                if (shiftFinish != null) {
                    String[] parts = shiftFinish.split(":");
                    if (format("%02d", i).equals(parts[0]) && !(format("%02d", i) + ":00").equals(shiftFinish)) {
                        hour.add(shiftFinish);
                    }
                }

                if (newHour != null) {
                    String[] parts = newHour.split(":");
                    if (format("%02d", i).equals(parts[0])) {
                        hour.add(newHour);
                    }
                }
            }
        }
        return expandableListDetail;
    }


    @SuppressLint("DefaultLocale")
    private void timeSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hour, minute) -> {
                    newHour = (String.format("%02d:%02d", hour, minute));
                    addListData();
                }, 0, 0, true);
        timePickerDialog.show();
        if (newHour != null) {
            String[] parts = newHour.split(":");
            String hour = parts[0];
            expandableListView.setSelectionFromTop(Integer.parseInt(hour), 0);
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    public void addListData() {

        LocalTime now = LocalTime.now();

        LinkedHashMap<String, List<String>> expandableListDetail = getData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListHoursAdapter expandableListHoursAdapter = new ExpandableListHoursAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListHoursAdapter);
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> false);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);

        expandableListView.setSelectionFromTop(now.getHour(), 0);
    }


    private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }

    @Override
    public void removeData(int eventKind) {
        super.removeData(eventKind);
        addListData();
    }
}

