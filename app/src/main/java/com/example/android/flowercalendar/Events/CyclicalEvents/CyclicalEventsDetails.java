package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.example.android.flowercalendar.Events.ExpandedDayView.ToDoList.newId;


public class CyclicalEventsDetails extends Fragment {

    private Context context;
    private String event_name_extra;
    private String event_start_date_extra;
    private String event_alarm_extra;
    private String event_length_hours_extra;
    private String event_length_minutes_extra;
    private String how_often_extra;
    private String what_time_extra;
    private String how_long_term_extra;
    private static final String EXTRA_EVENT_NAME = "event_name";
    private static final String EXTRA_EVENT_ID = "id";
    private static final String EXTRA_EVENT_START_DATE = "event_start_date";
    private static final String EXTRA_EVENT_ALARM = "event_alarm";
    private static final String EXTRA_EVENT_LENGHT_HOURS = "event_length_hours";
    private static final String EXTRA_EVENT_LENGHT_MINUTES = "event_length_minutes";
    private static final String EXTRA_EVENT_HOW_OFTEN = "how_often";
    private static final String EXTRA_EVENT_WHAT_TIME = "what_time";
    private static final String EXTRA_HOW_LONG_TERM = "how_long_term";


    private TextView eventStartTimeTextView;
    private ImageView eventTimeButton;
    private ImageView alarmButton;
    private TextView alarmTextView;
    private EditText eventNameEditText;
    private EditText eventLengthEditTextHours;
    private EditText eventLengthEditTextMinutes;
    private CalendarView calendarView;
    private Calendar calendar;
    private ImageButton howOftenButton;
    private TextView howOftenHeader;
    private LinearLayout howOftenEditLayout;
    private LinearLayout chooseHowLong;
    private View view2;
    private LinearLayout calendarHeader;
    private RadioGroup monthRadioButton;
    private TextView timeTextView;
    private ImageButton howLongButton;
    private TextView howLongHeader;
    private EditText howOftenEditText;
    private String dateString;
    private String startDate;
    private EditText howLongEditText;
    private int term = -1;
    private RadioButton everyFourWeeks;
    private RadioButton everyMonth;
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thr;
    private TextView fri;
    private TextView sat;
    private TextView sun;
    private int m;
    private int t;
    private int w;
    private int th;
    private int f;
    private int s;
    private int sn;
    private List<String> arrayOfpickedDaysOfAWeek;

    private int frequency;
    private int radioButtonMonthsOrWeeks;

    private AppUtils appUtils;
    private CyclicalEventsFrequencySettingsDays settingsDays;
    private CyclicalEventsFrequencySettingsWeeks settingsWeeks;
    private CyclicalEventsFrequencySettingsMonths settingsMonths;
    private CyclicalEventsFrequencySettingsYears settingsYears;

    private String newEventName;
    private String newAlarm;
    private String newEventStartDate;
    private String newWhatTime;
    private String newHowOften;
    private int newHowLong;
    private String howLongTerm;


    public CyclicalEventsDetails() {
        // Required empty public constructor
    }

    public static CyclicalEventsDetails newInstance(int id, String eventName, String event_start_date, String howOften, String whatTime, Integer event_length_hours, Integer event_length_minutes, String alarm, String how_long_term) {

        CyclicalEventsDetails fragment = new CyclicalEventsDetails();
        Bundle eventData = new Bundle();
        eventData.putInt(EXTRA_EVENT_ID, id);
        eventData.putString(EXTRA_EVENT_NAME, eventName);
        eventData.putString(EXTRA_EVENT_START_DATE, event_start_date);
        eventData.putString(EXTRA_EVENT_HOW_OFTEN, howOften);
        eventData.putString(EXTRA_EVENT_WHAT_TIME, whatTime);
        eventData.putString(EXTRA_EVENT_LENGHT_HOURS, String.valueOf(event_length_hours));
        eventData.putString(EXTRA_EVENT_LENGHT_MINUTES, String.valueOf(event_length_minutes));
        eventData.putString(EXTRA_EVENT_ALARM, alarm);
        eventData.putString(EXTRA_HOW_LONG_TERM, how_long_term);
        fragment.setArguments(eventData);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        appUtils = new AppUtils();
        settingsDays = new CyclicalEventsFrequencySettingsDays();
        settingsWeeks = new CyclicalEventsFrequencySettingsWeeks();
        settingsMonths = new CyclicalEventsFrequencySettingsMonths();
        settingsYears = new CyclicalEventsFrequencySettingsYears();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle eventData = getArguments();
        assert eventData != null;

        event_name_extra = eventData.getString(EXTRA_EVENT_NAME);
        event_start_date_extra = eventData.getString(EXTRA_EVENT_START_DATE);
        event_alarm_extra = eventData.getString(EXTRA_EVENT_ALARM);
        event_length_hours_extra = eventData.getString(EXTRA_EVENT_LENGHT_HOURS);
        event_length_minutes_extra = eventData.getString(EXTRA_EVENT_LENGHT_MINUTES);
        how_often_extra = eventData.getString(EXTRA_EVENT_HOW_OFTEN);
        what_time_extra = eventData.getString(EXTRA_EVENT_WHAT_TIME);
        how_long_term_extra = eventData.getString(EXTRA_HOW_LONG_TERM);

        if (event_name_extra == null) {
            event_name_extra = "-1";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cyclical_events_details, container, false);


        findViews(rootView);
        setHasOptionsMenu(true);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.OneTimeEvents));

        displayDataIfExist();
        setTimeScheduleButton();
        setAlarmButton();
        setEventStartDate();
        setHowOftenButtonOnClickListener();
        setHowLongButtonOnClickListener();
        setRadioGroupClickListener();
        setDayOfWeekOnClickListener();

        return rootView;
    }

    private void findViews(View rootView) {

        eventStartTimeTextView = rootView.findViewById(R.id.eventStart);
        eventTimeButton = rootView.findViewById(R.id.event_time_button);
        alarmButton = rootView.findViewById(R.id.alarm_button);
        alarmTextView = rootView.findViewById(R.id.alarm);
        eventNameEditText = rootView.findViewById(R.id.event_name_edit_text);
        eventLengthEditTextHours = rootView.findViewById(R.id.how_long_edit_text_hours);
        eventLengthEditTextMinutes = rootView.findViewById(R.id.how_long_edit_text_minutes);
        calendarView = rootView.findViewById(R.id.cyclicalEventsCalendar);
        howOftenButton = rootView.findViewById(R.id.howOftenButton);
        howOftenHeader = rootView.findViewById(R.id.howOftenHeader);
        howOftenEditLayout = rootView.findViewById(R.id.howOftenEditLayout);
        chooseHowLong = rootView.findViewById(R.id.chooseHowLong);
        view2 = rootView.findViewById(R.id.view2);
        calendarHeader = rootView.findViewById(R.id.calendarHeader);
        monthRadioButton = rootView.findViewById(R.id.monthRadioButton);
        timeTextView = rootView.findViewById(R.id.timeTextView);
        howLongButton = rootView.findViewById(R.id.howLongButton);
        howLongHeader = rootView.findViewById(R.id.howLongHeader);
        howOftenEditText = rootView.findViewById(R.id.howOftenEditText);
        howLongEditText = rootView.findViewById(R.id.howLongEditText);
        everyFourWeeks = rootView.findViewById(R.id.everyFourWeeks);
        everyMonth = rootView.findViewById(R.id.everyMonth);
        mon = rootView.findViewById(R.id.mon);
        tue = rootView.findViewById(R.id.tue);
        wed = rootView.findViewById(R.id.wed);
        thr = rootView.findViewById(R.id.thr);
        fri = rootView.findViewById(R.id.fri);
        sat = rootView.findViewById(R.id.sat);
        sun = rootView.findViewById(R.id.sun);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            saveEvent();
            appUtils.hideKeyboard(getView(), context);

            CyclicalEvents cyclicalEvents = new CyclicalEvents();
            getParentFragmentManager().beginTransaction().replace(R.id.flContent, cyclicalEvents).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    private void displayDataIfExist() {

        if (!event_name_extra.equals("-1")) {
            eventNameEditText.setText(event_name_extra);
            eventStartTimeTextView.setText(what_time_extra);
            eventLengthEditTextHours.setText(event_length_hours_extra);
            eventLengthEditTextMinutes.setText(event_length_minutes_extra);
            alarmTextView.setText(event_alarm_extra);
            calendarView.setDate(appUtils.eventStartDayToMilis(event_start_date_extra));
            displayHowOftenEditText();
            displayHowLongHeader();
        }
    }


    private void displayHowOftenEditText() {

        if (how_often_extra != null) {

            String[] parts = how_often_extra.split("-");
            if (how_often_extra.substring(0, 6).equals("0-0-0-")) {

                settingsDays.daysFrequencySettings(howOftenHeader,
                        howOftenEditLayout,
                        timeTextView,
                        calendarHeader,
                        monthRadioButton,
                        chooseHowLong,
                        view2);
                String howManyDays = parts[3];
                howOftenEditText.setText(howManyDays);
                frequency = 0;

            } else if (!how_often_extra.substring(0, 6).equals("0-0-0-")
                    && how_often_extra.substring(0, 4).equals("0-0-")) {

                settingsWeeks.weeksFrequencySettings(howOftenHeader,
                        howOftenEditLayout,
                        timeTextView,
                        calendarHeader,
                        monthRadioButton,
                        chooseHowLong,
                        view2);
                String howManyDays = parts[2];
                howOftenEditText.setText(howManyDays);
                frequency = 1;

            } else if (!how_often_extra.substring(0, 6).equals("0-0-0-")
                    && !how_often_extra.substring(0, 4).equals("0-0-")
                    && how_often_extra.substring(0, 2).equals("0-")) {

                settingsMonths.monthsFrequencySettings(howOftenHeader,
                        howOftenEditLayout,
                        timeTextView,
                        calendarHeader,
                        monthRadioButton,
                        chooseHowLong,
                        view2,
                        startDate,
                        event_start_date_extra,
                        everyFourWeeks);
                String howManyDays = parts[1];
                howOftenEditText.setText(howManyDays);

                if (how_often_extra.contains("*months")) {
                    everyMonth.setChecked(true);
                    radioButtonMonthsOrWeeks = 0;
                } else if (how_often_extra.contains("*4weeks")) {
                    everyFourWeeks.setChecked(true);
                    radioButtonMonthsOrWeeks = 1;
                } else {
                    everyMonth.setChecked(true);
                }
                frequency = 2;

            } else {
                settingsYears.yearsFrequencySettings(howOftenHeader,
                        howOftenEditLayout,
                        timeTextView,
                        calendarHeader,
                        monthRadioButton,
                        chooseHowLong,
                        view2);
                String howManyDays = parts[0];
                howOftenEditText.setText(howManyDays);
                frequency = 3;
            }

        } else {
            howOftenEditText.setText("");
        }

    }


    @SuppressLint("SetTextI18n")
    private void displayHowLongHeader() {

        if (how_long_term_extra != null) {
            if (how_long_term_extra.equals("on_and_on")) {
                howLongHeader.setText(R.string.on_and_on);
            } else if (how_long_term_extra.contains(",")) {
                howLongHeader.setText("Until " + how_long_term_extra);
            } else {
                howLongHeader.setText(how_long_term_extra + " times");
            }
        } else {
            howLongHeader.setText(R.string.on_and_on);
        }
    }


    private void setTimeScheduleButton() {
        eventTimeButton.setOnClickListener(v -> appUtils.eventTimeSettingDialog(eventStartTimeTextView, context));
    }


    private void setAlarmButton() {
        alarmButton.setOnClickListener(v -> appUtils.eventTimeSettingDialog(alarmTextView, context));
    }


    private void setEventStartDate() {
        calendarView
                .setOnDateChangeListener(
                        (view, year, month, dayOfMonth) -> startDate = year + "-" + (month + 1) + "-" + dayOfMonth);
    }


    private void setHowOftenButtonOnClickListener() {
        howOftenButton.setOnClickListener(v -> showPopUpMenu());
    }


    private void showPopUpMenu() {

        PopupMenu popupMenu = new PopupMenu(getActivity(), howOftenButton);
        popupMenu.getMenuInflater().inflate(R.menu.how_often_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.days:
                    settingsDays.daysFrequencySettings(howOftenHeader,
                            howOftenEditLayout,
                            timeTextView,
                            calendarHeader,
                            monthRadioButton,
                            chooseHowLong,
                            view2);
                    frequency = 0;
                    break;

                case R.id.weeks:
                    settingsWeeks.weeksFrequencySettings(howOftenHeader,
                            howOftenEditLayout,
                            timeTextView,
                            calendarHeader,
                            monthRadioButton,
                            chooseHowLong,
                            view2);
                    frequency = 1;
                    break;

                case R.id.months:
                    settingsMonths.monthsFrequencySettings(howOftenHeader,
                            howOftenEditLayout,
                            timeTextView,
                            calendarHeader,
                            monthRadioButton,
                            chooseHowLong,
                            view2,
                            startDate,
                            event_start_date_extra,
                            everyFourWeeks);
                    frequency = 2;
                    break;

                case R.id.years:
                    settingsYears.yearsFrequencySettings(howOftenHeader,
                            howOftenEditLayout,
                            timeTextView,
                            calendarHeader,
                            monthRadioButton,
                            chooseHowLong,
                            view2);
                    frequency = 3;
                    break;

                default:
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void setDayOfWeekOnClickListener() {

        arrayOfpickedDaysOfAWeek = new ArrayList<>();

        mon.setOnClickListener(v -> {
            m++;
            settingsWeeks.enableDisable(m, mon, "mon", arrayOfpickedDaysOfAWeek);
        });

        tue.setOnClickListener(v -> {
            t++;
            settingsWeeks.enableDisable(t, tue, "tue", arrayOfpickedDaysOfAWeek);
        });

        wed.setOnClickListener(v -> {
            w++;
            settingsWeeks.enableDisable(w, wed, "wed", arrayOfpickedDaysOfAWeek);
        });

        thr.setOnClickListener(v -> {
            th++;
            settingsWeeks.enableDisable(th, thr, "thr", arrayOfpickedDaysOfAWeek);
        });

        fri.setOnClickListener(v -> {
            f++;
            settingsWeeks.enableDisable(f, fri, "fri", arrayOfpickedDaysOfAWeek);
        });

        sat.setOnClickListener(v -> {
            s++;
            settingsWeeks.enableDisable(s, sat, "sat", arrayOfpickedDaysOfAWeek);
        });

        sun.setOnClickListener(v -> {
            sn++;
            settingsWeeks.enableDisable(sn, sun, "sun", arrayOfpickedDaysOfAWeek);
        });
    }


    private void setRadioGroupClickListener() {

        monthRadioButton.setOnCheckedChangeListener((group, checkedId) -> {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.everyMonth:
                    radioButtonMonthsOrWeeks = 0;
                    break;

                case R.id.everyFourWeeks:
                    radioButtonMonthsOrWeeks = 1;
                    break;
                default:
                    break;
            }

        });
    }


    private void setHowLongButtonOnClickListener() {
        howLongButton.setOnClickListener(v -> showPopUpHowLongMenu());
    }


    @SuppressLint("SetTextI18n")
    private void showPopUpHowLongMenu() {

        PopupMenu popupMenu = new PopupMenu(getActivity(), howLongButton);
        popupMenu.getMenuInflater().inflate(R.menu.how_long_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.on_and_on:
                    howLongEditText.setVisibility(View.GONE);
                    howLongHeader.setText(R.string.on_and_on);
                    term = 0;
                    break;

                case R.id.to_date:

                    howLongEditText.setVisibility(View.GONE);

                    calendar = Calendar.getInstance(TimeZone.getDefault());

                    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateString = appUtils.displayDateInAProperFormat(calendar);
                        howLongHeader.setText("Until " + dateString);

                    };

                    new DatePickerDialog(context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

                    term = 1;
                    break;

                case R.id.up_to_number:
                    howLongEditText.setVisibility(View.VISIBLE);
                    howLongHeader.setText("Times: ");

                    term = 2;
                    break;

                default:
                    break;

            }
            return true;
        });
        popupMenu.show();
    }


    private void saveEvent() {

        collectDataFromUserInput();

        if (newEventName.isEmpty() ||
                newEventStartDate.isEmpty() || newHowOften.isEmpty()) {

            //ToDo toast
            return;
        }

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (!event_name_extra.equals("-1")) {

            Event eventToUpdate = eventsDao.findByEventName(event_name_extra);
            if (eventToUpdate != null) {
                if ((!eventToUpdate.getEvent_name().equals(newEventName)) ||
                        (!eventToUpdate.getSchedule().equals(newWhatTime)) ||
                        (!eventToUpdate.getAlarm().equals(newAlarm)) ||
                        (!eventToUpdate.getPickedDay().equals(newEventStartDate)) ||
                        (eventToUpdate.getEvent_length() != newHowLong) ||
                        !eventToUpdate.getFrequency().equals(newHowOften) ||
                        !eventToUpdate.getTerm().equals(howLongTerm)) {
                    eventToUpdate.setEvent_name(newEventName);
                    eventToUpdate.setSchedule(newWhatTime);
                    eventToUpdate.setAlarm(newAlarm);
                    eventToUpdate.setEvent_length(newHowLong);
                    eventToUpdate.setPickedDay(newEventStartDate);
                    eventToUpdate.setFrequency(newHowOften);
                    eventToUpdate.setTerm(howLongTerm);
                    eventsDao.update(eventToUpdate);
                }
            }
        } else {

            eventsDao.insert(new Event(String.valueOf(newId), newEventName, newWhatTime, newAlarm, newHowLong, newEventStartDate, 0, newHowOften, howLongTerm));

        }
    }


    private void collectDataFromUserInput() {

        newEventName = eventNameEditText.getText().toString();
        newAlarm = alarmTextView.getText().toString();
        newWhatTime = eventStartTimeTextView.getText().toString();
        findStartTime();
        findFrequency();
        findDuration();
        findTerm();

    }

    private void findStartTime() {

        if (startDate != null) {
            newEventStartDate = startDate;
        } else {
            newEventStartDate = event_start_date_extra;
        }
    }

    private void findFrequency() {

        if (frequency == 0) {

            newHowOften = "0" + "-" + "0" + "-" + "0" + "-" + howOftenEditText.getText().toString();

        } else if (frequency == 1) {

            newHowOften = "0" + "-" + "0" + "-" + howOftenEditText.getText().toString() + "-" + "0";

        } else if (frequency == 2) {

            String weeksOrMonths;
            if (radioButtonMonthsOrWeeks == 1) {
                weeksOrMonths = "*4weeks";
            } else {
                weeksOrMonths = "*months";
            }
            newHowOften = "0" + "-" + howOftenEditText.getText().toString() + "-" + weeksOrMonths + "-" + "0" + "-" + "0";

        } else {
            newHowOften = howOftenEditText.getText().toString() + "-" + "0" + "-" + "0" + "-" + "0";
        }

    }


    private void findDuration() {

        int howManyHours;
        if (!eventLengthEditTextHours.getText().toString().equals("")) {
            howManyHours = Integer.parseInt(eventLengthEditTextHours.getText().toString());
        } else {
            howManyHours = 0;
        }

        int howManyMinutes;

        if (!eventLengthEditTextMinutes.getText().toString().equals("")) {
            howManyMinutes = Integer.parseInt(eventLengthEditTextMinutes.getText().toString());
        } else {
            howManyMinutes = 0;
        }

        newHowLong = howManyHours * 60 + howManyMinutes;
    }

    private void findTerm() {

        if (term != -1) {
            if (term == 0) {
                howLongTerm = "on_and_on";
            } else if (term == 1) {
                howLongTerm = dateString;
            } else {
                howLongTerm = howLongEditText.getText().toString();
            }
        } else {
            howLongTerm = how_long_term_extra;
        }
    }
}