package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

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

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.alarm.CyclicalEventsNotifications;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static java.lang.String.format;


public class CyclicalEventsDetails extends Fragment {

    private Context context;
    private String event_name_extra;
    private String event_start_date_extra;
    private String event_alarm_extra;
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

    private int frequency = -1;
    private int radioButtonMonthsOrWeeks;

    private CyclicalEventsFrequencySettingsDays settingsDays;
    private CyclicalEventsFrequencySettingsWeeks settingsWeeks;
    private CyclicalEventsFrequencySettingsMonths settingsMonths;
    private CyclicalEventsFrequencySettingsYears settingsYears;
    private CyclicalEventsNotifications cyclicalEventsNotifications;

    private String newEventName;
    private String newAlarm;
    private String newWhatTime;
    private String newHowOften;
    private int newHowLong;
    private String howLongTerm;

    private TextView resetStartTime;
    private TextView resetDuration;
    private TextView resetNotification;


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
        settingsDays = new CyclicalEventsFrequencySettingsDays();
        settingsWeeks = new CyclicalEventsFrequencySettingsWeeks();
        settingsMonths = new CyclicalEventsFrequencySettingsMonths();
        settingsYears = new CyclicalEventsFrequencySettingsYears();
        cyclicalEventsNotifications = new CyclicalEventsNotifications();
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle eventData = getArguments();
        assert eventData != null;

        event_name_extra = eventData.getString(EXTRA_EVENT_NAME);
        event_start_date_extra = eventData.getString(EXTRA_EVENT_START_DATE);
        event_alarm_extra = eventData.getString(EXTRA_EVENT_ALARM);
        event_length_minutes_extra = eventData.getString(EXTRA_EVENT_LENGHT_MINUTES);
        how_often_extra = eventData.getString(EXTRA_EVENT_HOW_OFTEN);
        what_time_extra = eventData.getString(EXTRA_EVENT_WHAT_TIME);
        how_long_term_extra = eventData.getString(EXTRA_HOW_LONG_TERM);

        if (event_name_extra == null) {
            event_name_extra = "-1";
        }
        if (event_alarm_extra == null) {
            event_alarm_extra = "";
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
        setResetStartTime();
        setResetDuration();
        setResetNotification();

        arrayOfpickedDaysOfAWeek = new ArrayList<>();

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
        resetStartTime = rootView.findViewById(R.id.resetStartTime);
        resetDuration = rootView.findViewById(R.id.resetDuration);
        resetNotification = rootView.findViewById(R.id.resetNotification);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            saveEvent(findStartTime());
            AppUtils.hideKeyboard(getView(), context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    private void displayDataIfExist() {

        if (!event_name_extra.equals("-1")) {
            eventNameEditText.setText(event_name_extra);
            eventStartTimeTextView.setText(what_time_extra);
            int eventDuration = Integer.parseInt(event_length_minutes_extra);
            int eventDurationInHours = eventDuration / 60;
            int eventDurationMinutes = eventDuration - eventDurationInHours * 60;
            if (eventDurationInHours != 0 && eventDurationMinutes != 0) {
                eventLengthEditTextHours.setText(String.valueOf(eventDurationInHours));
                eventLengthEditTextMinutes.setText(String.valueOf(eventDurationMinutes));
            } else {
                eventLengthEditTextHours.setHint("10");
                eventLengthEditTextMinutes.setHint("10");
            }

            alarmTextView.setText(event_alarm_extra);
            calendarView.setDate(AppUtils.dateStringToMilis(event_start_date_extra));
            displayHowOftenEditText();
            displayHowLongHeader();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setResetStartTime() {
        resetStartTime.setOnClickListener(v -> {
            eventStartTimeTextView.setHint("06:00");
            eventStartTimeTextView.setText("");
        });
    }

    @SuppressLint("SetTextI18n")
    private void setResetDuration() {
        resetDuration.setOnClickListener(v -> {
            eventLengthEditTextHours.setHint("10");
            eventLengthEditTextMinutes.setHint("10");
            eventLengthEditTextHours.setText("");
            eventLengthEditTextMinutes.setText("");
        });
    }

    @SuppressLint("SetTextI18n")
    private void setResetNotification() {
        resetNotification.setOnClickListener(v -> {
            alarmTextView.setHint("06:00");
            alarmTextView.setText("");
        });
    }


    @SuppressLint("SetTextI18n")
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
                String howManyWeeks = parts[2];
                String whichDaysOfWeek = parts[3];
                displayChoosenDaysOfTheWeek(whichDaysOfWeek);
                howOftenEditText.setText(howManyWeeks);
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
                        everyFourWeeks, context);

                String howManyMonths = parts[1];
                howOftenEditText.setText(howManyMonths);

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
                String howManyYears = parts[0];
                howOftenEditText.setText(howManyYears);
                frequency = 3;
            }

        } else {
            howOftenEditText.setText("");
        }

    }

    private void displayChoosenDaysOfTheWeek(String whichDaysOfWeek) {

        if (whichDaysOfWeek.contains("mon")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "mon", mon);
        }
        if (whichDaysOfWeek.contains("tue")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "tue", tue);
        }
        if (whichDaysOfWeek.contains("wed")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "wed", wed);
        }
        if (whichDaysOfWeek.contains("thr")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "thr", thr);
        }
        if (whichDaysOfWeek.contains("fri")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "fri", fri);
        }
        if (whichDaysOfWeek.contains("sat")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "sat", sat);
        }
        if (whichDaysOfWeek.contains("sun")) {
            settingsWeeks.highlightDaysOfWeekIfChoosen(whichDaysOfWeek, "sun", sun);
        }
    }


    @SuppressLint("SetTextI18n")
    private void displayHowLongHeader() {

        if (how_long_term_extra != null) {
            if (how_long_term_extra.equals("on_and_on")) {
                howLongHeader.setText(R.string.on_and_on);
            } else if (how_long_term_extra.contains(",")) {
                howLongHeader.setText(getString(R.string.until) + " " + how_long_term_extra);
            } else {
                howLongHeader.setText(how_long_term_extra + " " + getString(R.string.times));
            }
        } else {
            howLongHeader.setText(R.string.on_and_on);
        }
    }


    private void setTimeScheduleButton() {
        eventTimeButton.setOnClickListener(v -> AppUtils.eventTimeSettingDialog(eventStartTimeTextView, context));
    }


    private void setAlarmButton() {
        alarmButton.setOnClickListener(v -> AppUtils.eventTimeSettingDialog(alarmTextView, context));
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
                            everyFourWeeks, context);
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
                        dateString = AppUtils.displayDateInAProperFormat(calendar);
                        howLongHeader.setText(getString(R.string.until) + " " + dateString);

                    };

                    new DatePickerDialog(context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

                    term = 1;
                    break;

                case R.id.up_to_number:
                    howLongEditText.setVisibility(View.VISIBLE);
                    howLongHeader.setText(getString(R.string.Times) + " ");

                    term = 2;
                    break;

                default:
                    break;

            }
            return true;
        });
        popupMenu.show();
    }


    @SuppressLint("ShowToast")
    private void saveEvent(String startTime) {

        collectDataFromUserInput();

        if (newEventName.isEmpty() ||
                startTime == null || newHowOften == null || howOftenEditText.getText().toString().equals("")) {

            String emptyField = (getString(R.string.requirements));
            AppUtils.showFillInThisFieldDialog(emptyField, context);

            return;
        }

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (!event_name_extra.equals("-1")) {

            List<Event> eventsToUpdate = eventsDao.findByEventNameList(event_name_extra);
            if (eventsToUpdate != null) {
                for (Event eventToUpdate : eventsToUpdate) {
                    if (eventToUpdate.getAlarm() == null) {
                        eventToUpdate.setAlarm("");
                        eventsDao.update(eventToUpdate);
                    }

                    if (((!eventToUpdate.getEvent_name().equals(newEventName)) ||
                            (!eventToUpdate.getSchedule().equals(newWhatTime)) ||
                            (!eventToUpdate.getAlarm().equals(newAlarm)))
                            &&
                            ((event_start_date_extra.equals(startTime)) &&
                                    (event_length_minutes_extra.equals(String.valueOf(newHowLong))
                                            &&
                                            how_often_extra.equals(newHowOften) &&
                                            how_long_term_extra.equals(howLongTerm)))) {


                        cyclicalEventsNotifications.deleteNotification(eventToUpdate, context.getApplicationContext());

                        eventToUpdate.setEvent_name(newEventName);
                        eventToUpdate.setSchedule(newWhatTime);
                        eventToUpdate.setAlarm(newAlarm);
                        eventsDao.update(eventToUpdate);
                        CyclicalEventsNotifications.setNotification(eventToUpdate, context.getApplicationContext());


                    } else {

                        if (eventsToUpdate.get(0) != eventToUpdate) {
                            cyclicalEventsNotifications.deleteNotification(eventToUpdate, context.getApplicationContext());
                            eventToUpdate.setEvent_name("To delete");
                            eventsDao.update(eventToUpdate);
                            eventsDao.deleteByEventName("To delete");
                            return;
                        }
                        eventsToUpdate.get(0).setEvent_name(newEventName);
                        eventsToUpdate.get(0).setSchedule(newWhatTime);
                        eventsToUpdate.get(0).setAlarm(newAlarm);
                        eventsToUpdate.get(0).setEvent_length(newHowLong);
                        eventsToUpdate.get(0).setPickedDay(startTime);
                        eventsToUpdate.get(0).setFrequency(newHowOften);
                        eventsToUpdate.get(0).setTerm(howLongTerm);
                        eventsDao.update(eventsToUpdate.get(0));
                        CyclicalEventsNotifications.setNotification(eventToUpdate, context.getApplicationContext());

                    }
                }
            }
        } else {


            Event event = new Event(-56, newEventName, newWhatTime, newAlarm, newHowLong, startTime, 0, newHowOften, howLongTerm);
            eventsDao.insert(event);
            CyclicalEventsNotifications.setNotification(event, context.getApplicationContext());

        }

        CyclicalEvents cyclicalEvents = new CyclicalEvents();
        getParentFragmentManager().beginTransaction().replace(R.id.flContent, cyclicalEvents).commit();
    }


    private void collectDataFromUserInput() {

        newEventName = eventNameEditText.getText().toString();
        newAlarm = alarmTextView.getText().toString();
        newWhatTime = eventStartTimeTextView.getText().toString();
        findFrequency();
        findDuration();
        findTerm();

    }

    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    private String findStartTime() {

        String newEventStartDate;
        if (startDate != null) {

            String[] parts = startDate.split("-");
            newEventStartDate = parts[0] + "-" + format("%02d", Integer.parseInt(parts[1])) + "-" + parts[2];
        } else {
            newEventStartDate = event_start_date_extra;
        }
        return newEventStartDate;
    }

    private void findFrequency() {

        if (frequency == -1) {
            newHowOften = how_often_extra;
            if (!arrayOfpickedDaysOfAWeek.isEmpty()) {
                frequency = 1;
            }
        }

        if (frequency == 0) {

            newHowOften = "0" + "-" + "0" + "-" + "0" + "-" + howOftenEditText.getText().toString();

        } else if (frequency == 1) {
            if (arrayOfpickedDaysOfAWeek.isEmpty()) {
                arrayOfpickedDaysOfAWeek.add(AppUtils.refactorStringIntoDate(findStartTime()).getDayOfWeek().toString().toLowerCase().substring(0, 3));
            }

            newHowOften = "0" + "-" + "0" + "-" + howOftenEditText.getText().toString() + "-" + arrayOfpickedDaysOfAWeek + "-" + "0";


        } else if (frequency == 2) {

            String weeksOrMonths;
            if (radioButtonMonthsOrWeeks == 1) {
                weeksOrMonths = "*4weeks";
            } else {
                weeksOrMonths = "*months";
            }
            newHowOften = "0" + "-" + howOftenEditText.getText().toString() + "-" + weeksOrMonths + "-" + "0" + "-" + "0";

        } else if (frequency == 3) {
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
