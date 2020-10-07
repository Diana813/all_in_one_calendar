package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

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

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ExpandableListHoursAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities.FrequentActivitiesViewModel;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents.addNotification;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.displayHeaderDateInToolbar;
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


   /* public void setContent(int layout) {
        this.layout = layout;
    }*/


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        frequentActivitiesDrawerListAdapter = new FreqActAdapterSchedule(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scheduled_events_layout, container, false);

        Objects.requireNonNull(getActivity()).setTitle(context.getString(R.string.dailySchedule));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(displayHeaderDateInToolbar(currentDate));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(R.drawable.baseline_chevron_left_black_24);


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
        } else if (item.getItemId() == R.id.action_add_notification) {
            addNotification(getView(), context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("DefaultLocale")
    private LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        pickedDay = findWhatDateItIs();

        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        List<Event> scheduledEventsList = eventsDao.sortByPickedDay(pickedDay, EventKind.EVENTS.getIntValue());

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
            shiftFinish = DialogsUtils.findShiftFinish(shiftSchedule, shiftLenght);
        }


        if (!scheduledEventsList.isEmpty()) {
            for (int i = 0; i < 24; i++) {
                if (scheduledEventsList.get(i).getSchedule() != null) {
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

