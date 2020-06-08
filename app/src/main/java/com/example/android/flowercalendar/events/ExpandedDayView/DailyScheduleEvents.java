package com.example.android.flowercalendar.events.ExpandedDayView;

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

import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.events.FrequentActivities.FrequentActivitiesViewModel;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.String.format;

public class DailyScheduleEvents extends Fragment {

    private ExpandableListView expandableListView;
    private int layout;
    private String newHour;
    private String pickedDay;
    private CalendarFragment calendarFragment;
    private FreqActAdapterSchedule frequentActivitiesDrawerListAdapter;
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private Context context;

    public DailyScheduleEvents() {
        // Required empty public constructor
    }

    public void onPause() {
        super.onPause();
        calendarFragment.saveEventsNumberToPickedDate(pickedDay);
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
        calendarFragment = new CalendarFragment();
        frequentActivitiesDrawerListAdapter = new FreqActAdapterSchedule(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        inflater.inflate(R.menu.expanded_day_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem hour = menu.findItem(R.id.action_add_hour);
        hour.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all_entries) {
            ToDoList.showDeleteConfirmationDialog(3);
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
    private void addListData() {

        LocalTime now = LocalTime.now();

        LinkedHashMap<String, List<String>> expandableListDetail = getData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListHoursAdapter expandableListHoursAdapter = new ExpandableListHoursAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListHoursAdapter);
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> false);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);

        expandableListView.setSelectionFromTop(now.getHour(), 0);
    }


    private String findWhatDateItIs() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BackgroundActivityExpandedDayView) {
            pickedDay = ((BackgroundActivityExpandedDayView) parentFragment).getPickedDate();
        }
        return pickedDay;
    }

    private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }

}

