package com.example.android.flowercalendar.events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.events.CyclicalEvents.DeleteCyclicalEvent;
import com.example.android.flowercalendar.utils.AppUtils;
import com.example.android.flowercalendar.utils.CopyPasteEditText;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.example.android.flowercalendar.calendar.CalendarFragment.pickedDay;

public class ExpandableListHoursAdapter extends BaseExpandableListAdapter {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail;
    public static String schedule;
    private int index = -1;
    private int indexExpandedListPosition = -1;
    private String shiftSchedule = findShiftSchedule();
    private String yesterdayShiftSchedule = findPreviousShiftSchedule();
    private int shiftLength = findShiftDuration();
    private int previousShiftLenght = findPreviousShiftDuration();
    private ImageView imageViewGroupList;
    private TextView listTitleTextViewGroupList;
    private CopyPasteEditText editTextGroupList;
    private DeleteCyclicalEvent deleteCyclicalEvent = new DeleteCyclicalEvent();
    private LocalTime shiftStartTime;

    ExpandableListHoursAdapter(Context context, List<String> expandableListTitle,
                               LinkedHashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }


    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @SuppressLint({"InflateParams"})
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.single_hours_item, null);
        }

        CopyPasteEditText editText = convertView.findViewById(R.id.editText);
        TextView expandedListTextView = convertView
                .findViewById(R.id.hour);

        handleEditTextSingleItem(editText, listPosition, expandedListPosition, expandedListText);

        addDataFromDB(expandedListText, editText);
        addShift(convertView, expandedListText);
        expandedListTextView.setText(expandedListText);
        editText.addListener(() -> AppUtils.saveDataEvents(editText, pickedDay, editText.getText().toString(), "-1", schedule, 3));

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @SuppressLint({"InflateParams"})
    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.single_hours_group, null);
        }

        findViewsGroupList(convertView);

        listTitleTextViewGroupList.setText(listTitle);
        showDownArrow(imageViewGroupList, listPosition);
        editTextGroupListHandler(editTextGroupList, listPosition, listTitle);
        addDataFromDB(listTitle, editTextGroupList);
        addShift(convertView, listTitle);

        return convertView;
    }


    private void addShift(View convertView, String listTitle) {

        ImageView work = convertView.findViewById(R.id.work);

        if ((shiftSchedule != null && !shiftSchedule.equals("") && shiftLength != -1)) {
            String[] parts = shiftSchedule.split(":");
            int shiftStartHour = Integer.parseInt(parts[0]);
            int shiftStartMinutes = Integer.parseInt(parts[1]);

            shiftStartTime = LocalTime.of(shiftStartHour, shiftStartMinutes);
        }
        String[] currentHourParts = listTitle.split(":");
        int currentHour = Integer.parseInt(currentHourParts[0]);
        int currentMinutes = Integer.parseInt(currentHourParts[1]);

        LocalTime currentTime = LocalTime.of(currentHour, currentMinutes);


        if (shiftStartTime != null && (shiftStartTime.equals(currentTime) || shiftStartTime.plusHours(shiftLength).equals(currentTime) || (shiftStartTime.isBefore(currentTime) && shiftStartTime.plusHours(shiftLength).isAfter(currentTime)))) {
            work.setVisibility(View.VISIBLE);
        } else if (previousShiftLenght > 0 && (LocalTime.MIDNIGHT.equals(currentTime) || LocalTime.MIDNIGHT.plusHours(previousShiftLenght).equals(currentTime) || (LocalTime.MIDNIGHT.isBefore(currentTime) && LocalTime.MIDNIGHT.plusHours(previousShiftLenght).isAfter(currentTime)))) {
            work.setVisibility(View.VISIBLE);
        } else {
            work.setVisibility(View.GONE);

        }


    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


    private void findViewsGroupList(View convertView) {

        imageViewGroupList = convertView.findViewById(R.id.downArrow);
        listTitleTextViewGroupList = convertView
                .findViewById(R.id.hour);
        editTextGroupList = convertView.findViewById(R.id.editText);

    }


    private String findShiftSchedule() {

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();

        String schedule = null;
        if (calendarEventsDao.findBypickedDate(pickedDay) != null) {
            String todaysShift = calendarEventsDao.findBypickedDate(pickedDay).getShiftNumber();
            if (todaysShift != null && !todaysShift.equals("")) {
                ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
                schedule = shiftsDao.findByShiftName(todaysShift).getSchedule();
            }
        }
        return schedule;

    }

    private String findPreviousShiftSchedule() {

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();

        LocalDate yest = AppUtils.refactorStringIntoDate(pickedDay).minusDays(1);
        String yesterday = String.valueOf(yest);

        String schedule = null;
        if (calendarEventsDao.findBypickedDate(yesterday) != null) {
            String yesterdayShift = calendarEventsDao.findBypickedDate(yesterday).getShiftNumber();
            if (yesterdayShift != null && !yesterdayShift.equals("")) {
                ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
                schedule = shiftsDao.findByShiftName(yesterdayShift).getSchedule();
            }
        }
        return schedule;
    }


    private int findShiftDuration() {

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
        int duration = -1;
        if (calendarEventsDao.findBypickedDate(pickedDay) != null) {
            String todaysShift = calendarEventsDao.findBypickedDate(pickedDay).getShiftNumber();
            ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
            if (todaysShift != null && !todaysShift.equals("")) {
                duration = shiftsDao.findByShiftName(todaysShift).getShift_length();
                if (!shiftSchedule.equals("")) {
                    String[] parts = shiftSchedule.split(":");
                    int shiftStartHour = Integer.parseInt(parts[0]);
                    int shiftStartMinutes = Integer.parseInt(parts[1]);

                    shiftStartTime = LocalTime.of(shiftStartHour, shiftStartMinutes);
                    if (shiftStartTime != null) {
                        if (shiftStartTime.plusHours(duration).isBefore(LocalTime.NOON)) {
                            Duration dur = Duration.between(shiftStartTime, LocalTime.MIDNIGHT.minusMinutes(1));
                            duration = (int) dur.toHours();
                        }
                    }
                }
            }
        }
        return duration;
    }

    private int findPreviousShiftDuration() {

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
        LocalDate yest = AppUtils.refactorStringIntoDate(pickedDay).minusDays(1);
        String yesterday = String.valueOf(yest);
        int previousShiftduration = 0;
        if (calendarEventsDao.findBypickedDate(yesterday) != null) {
            String yesterdayShift = calendarEventsDao.findBypickedDate(yesterday).getShiftNumber();
            ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
            if (yesterdayShift != null && !yesterdayShift.equals("")) {
                int duration = shiftsDao.findByShiftName(yesterdayShift).getShift_length();
                if (!yesterdayShiftSchedule.equals("")) {
                    String[] parts = yesterdayShiftSchedule.split(":");
                    int shiftStartHour = Integer.parseInt(parts[0]);
                    int shiftStartMinutes = Integer.parseInt(parts[1]);

                    LocalTime shiftStartTime = LocalTime.of(shiftStartHour, shiftStartMinutes);
                    if (shiftStartTime != null) {
                        if (shiftStartTime.plusHours(duration).isBefore(LocalTime.NOON)) {
                            Duration dur = Duration.between(LocalTime.MIDNIGHT, shiftStartTime.plusHours(duration));
                            previousShiftduration = (int) dur.toHours();
                        }
                    }
                }
            }
        }
        return previousShiftduration;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void editTextGroupListHandler(CopyPasteEditText editText, int listPosition, String listTitle) {

        editText.setOnEditorActionListener(new DoneOnEditorActionListener());
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        editText.setFocusable(false);
        editText.setOnTouchListener((v, event) -> {
            editText.setFocusableInTouchMode(true);
            index = listPosition;
            if (index != -1) {
                schedule = (String) getGroup(index);
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            String pasteData;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);

            }
        });

        editText.setOnLongClickListener(v -> {
            EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
            Event cyclicalEvent = eventsDao.findBySchedule(pickedDay, listTitle);
            if (cyclicalEvent != null) {
                deleteCyclicalEvent.deleteCyclicalEventFromPickedDay(cyclicalEvent.getEvent_name(), eventsDao);
            }
            eventsDao.deleteBySchedule(pickedDay, listTitle);
            editText.setText("");
            return false;
        });

        editText.addListener(() -> AppUtils.saveDataEvents(editText, pickedDay, editText.getText().toString(), "-1", schedule, 3));
    }

    private void showDownArrow(ImageView imageView, int listPosition) {

        if (getChildrenCount(listPosition) > 0) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private void addDataFromDB(String listTitle, CopyPasteEditText editText) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        Event event = eventsDao.findBySchedule(pickedDay, listTitle);
        if (event != null) {
            if (event.getSchedule().equals(listTitle)) {
                editText.setText(event.getEvent_name());
            } else {
                editText.setText("");
            }
        } else {
            editText.setText("");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleEditTextSingleItem(CopyPasteEditText editText, int listPosition, int expandedListPosition, String expandedListText) {

        editText.setOnEditorActionListener(new DoneOnEditorActionListener());
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        editText.setOnTouchListener((v, event) -> {
            index = listPosition;
            indexExpandedListPosition = expandedListPosition;
            if (index != -1 && indexExpandedListPosition != -1) {
                schedule = (String) getChild(index, indexExpandedListPosition);
            }
            return false;
        });

        editText.setOnLongClickListener(v -> {
            EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
            eventsDao.deleteBySchedule(pickedDay, expandedListText);
            deleteCyclicalEvent.deleteCyclicalEventFromPickedDay(expandedListText, eventsDao);
            editText.setText("");
            return false;
        });
    }
}

