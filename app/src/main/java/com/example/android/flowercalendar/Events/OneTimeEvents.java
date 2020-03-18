package com.example.android.flowercalendar.Events;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.flowercalendar.Events.CyclicalEvents.CyclicalEvents;
import com.example.android.flowercalendar.Events.ExpandedDayView.ToDoList;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.util.Objects;

import static com.example.android.flowercalendar.Events.ExpandedDayView.ToDoList.newId;


public class OneTimeEvents extends Fragment {

    private Context context;
    private String event_name_extra;
    private String event_schedule_extra;
    private String event_alarm_extra;
    private String event_length_hours_extra;
    private static final String EXTRA_EVENT_NAME = "event_name";
    private static final String EXTRA_EVENT_ID = "id";
    private static final String EXTRA_EVENT_SCHEDULE = "event_start_time";
    private static final String EXTRA_EVENT_ALARM = "event_alarm";
    private static final String EXTRA_EVENT_LENGHT_HOURS = "event_length_hours";
    private static final String EXTRA_EVENT_PICKEDDAY = "pickedDay";


    private TextView eventStart;
    private ImageView scheduleButton;
    private TextView alarmTextView;
    private EditText eventNameEditText;
    private EditText eventLengthEditTextHours;
    private EditText eventLengthEditTextMinutes;
    private int newEventLength;
    private String pickedDay;

    public OneTimeEvents() {
        // Required empty public constructor
    }

    public static OneTimeEvents newInstance(int id, String eventName, String event_start, String alarm, Integer event_length_hours, String pickedDay) {

        OneTimeEvents fragment = new OneTimeEvents();
        Bundle eventData = new Bundle();
        eventData.putInt(EXTRA_EVENT_ID, id);
        eventData.putString(EXTRA_EVENT_NAME, eventName);
        eventData.putString(EXTRA_EVENT_SCHEDULE, event_start);
        eventData.putString(EXTRA_EVENT_ALARM, alarm);
        eventData.putString(EXTRA_EVENT_LENGHT_HOURS, String.valueOf(event_length_hours));
        eventData.putString(EXTRA_EVENT_PICKEDDAY, pickedDay);
        fragment.setArguments(eventData);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle eventData = getArguments();
        assert eventData != null;

        event_name_extra = eventData.getString(EXTRA_EVENT_NAME);
        String event_id_extra = eventData.getString(EXTRA_EVENT_ID);
        event_schedule_extra = eventData.getString(EXTRA_EVENT_SCHEDULE);
        event_alarm_extra = eventData.getString(EXTRA_EVENT_ALARM);
        event_length_hours_extra = eventData.getString(EXTRA_EVENT_LENGHT_HOURS);
        String event_picked_day_extra = eventData.getString(EXTRA_EVENT_PICKEDDAY);

        if (event_name_extra == null) {
            event_name_extra = "-1";
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one_time_events, container, false);

        setHasOptionsMenu(true);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.OneTimeEvents));

        eventStart = rootView.findViewById(R.id.eventStart);
        scheduleButton = rootView.findViewById(R.id.schedule_button);
        alarmTextView = rootView.findViewById(R.id.alarm);
        eventNameEditText = rootView.findViewById(R.id.event_name_edit_text);
        eventLengthEditTextHours = rootView.findViewById(R.id.how_long_edit_text_hours);
        eventLengthEditTextMinutes = rootView.findViewById(R.id.how_long_edit_text_minutes);

        if (!event_name_extra.equals("-1")) {
            eventNameEditText.setText(event_name_extra);
            eventNameEditText.setSelection(event_name_extra.length());
            eventStart.setText(event_schedule_extra);
            alarmTextView.setText(event_alarm_extra);
            eventLengthEditTextHours.setText(event_length_hours_extra);

        }

        assert getArguments() != null;
        pickedDay = getArguments().getString("pickedDay");
        if (pickedDay == null) {
            pickedDay = "";
        }


        setScheduleButton();

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                saveEvent();
                final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);

                if (!pickedDay.equals("")) {
                    ToDoList toDoList = new ToDoList();
                    Bundle args = new Bundle();
                    args.putString("pickedDay", pickedDay);
                    toDoList.setArguments(args);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.flContent, toDoList).commit();
                } else {
                    CyclicalEvents cyclicalEvents = new CyclicalEvents();
                    Bundle args = new Bundle();
                    args.putString("pickedDay", pickedDay);
                    cyclicalEvents.setArguments(args);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.flContent, cyclicalEvents).commit();

                }

                return true;

            case R.id.action_delete_all_entries:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void eventTimeSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        eventStart.setText(String.format("%02d:%02d", hour, minute));

                    }
                }, 0, 0, true);
        timePickerDialog.show();
    }

    private void setScheduleButton() {
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTimeSettingDialog();
            }
        });
    }

    private void saveEvent() {

        String newEventName = eventNameEditText.getText().toString();
        String newEventStart = eventStart.getText().toString();
        String newAlarm = alarmTextView.getText().toString();


        try {
            newEventLength = Integer.parseInt(eventLengthEditTextHours.getText().toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();

        }

        if (newEventName.isEmpty() &&
                newAlarm.isEmpty() && newEventStart.isEmpty()) {
            return;
        }


        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (!event_name_extra.equals("-1")) {

            Event eventToUpdate = eventsDao.findByEventName(event_name_extra);
            if (eventToUpdate != null) {
                if ((!eventToUpdate.getEvent_name().equals(newEventName)) ||
                        (!eventToUpdate.getSchedule().equals(newEventStart)) ||
                        (!eventToUpdate.getAlarm().equals(newAlarm)) ||
                        (eventToUpdate.getEvent_length() != newEventLength)) {
                    eventToUpdate.setEvent_name(newEventName);
                    eventToUpdate.setSchedule(newEventStart);
                    eventToUpdate.setAlarm(newAlarm);
                    eventToUpdate.setEvent_length(newEventLength);
                    eventsDao.update(eventToUpdate);
                }
            }
        } else {

            eventsDao.insert(new Event(String.valueOf(newId), newEventName, newEventStart, newAlarm, newEventLength, pickedDay, 0));

        }
    }


}
