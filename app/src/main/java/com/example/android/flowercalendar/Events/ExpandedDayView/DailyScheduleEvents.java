package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static java.lang.String.format;

public class DailyScheduleEvents extends Fragment {

    private ExpandableListView expandableListView;
    private int layout;
    private FloatingActionButton fab;
    private String newHour;
    private String pickedDay;


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
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        setNumberOfEventsToPickedDate(pickedDay);
        addListData();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(layout, container, false);


        expandableListView = rootView.findViewById(R.id.expandableListView);
        addListData();

        fab = rootView.findViewById(R.id.fab);
        setOnFabClicListener();
        return rootView;
    }

    @SuppressLint("DefaultLocale")
    private LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        pickedDay = findWhatDateItIs();

        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        List<Event> scheduledEventsList = eventsDao.sortByPickedDay(pickedDay, 3);
        if (!scheduledEventsList.isEmpty()) {
            for (int i = 0; i < 24; i++) {
                List<String> hour = new ArrayList<>();
                expandableListDetail.put(((format("%02d", i) + ":00")), hour);

                for (Event event : scheduledEventsList) {
                    String addedHour = scheduledEventsList.get(scheduledEventsList.indexOf(event)).getSchedule();
                    String[] parts = addedHour.split(":");
                    if (format("%02d", i).equals(parts[0]) && !(format("%02d", i) + ":00").equals(addedHour)) {
                        hour.add(addedHour);
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


    private void setOnFabClicListener() {
        fab.setOnClickListener(v -> timeSettingDialog());
    }


    @SuppressLint("DefaultLocale")
    private void timeSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hour, minute) -> {
                    newHour = (String.format("%02d:%02d", hour, minute));
                    addListData();
                }, 0, 0, true);
        timePickerDialog.show();
    }


    private void addListData() {

        LinkedHashMap<String, List<String>> expandableListDetail = getData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListHoursAdapter expandableListAdapter = new ExpandableListHoursAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> false);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);

    }


    private void setNumberOfEventsToPickedDate(String pickedDay) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate(pickedDay);
    }


    private String findWhatDateItIs() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BackgroundActivityExpandedDayView) {
            pickedDay = ((BackgroundActivityExpandedDayView) parentFragment).getPickedDate();
        }
        return pickedDay;
    }


}

