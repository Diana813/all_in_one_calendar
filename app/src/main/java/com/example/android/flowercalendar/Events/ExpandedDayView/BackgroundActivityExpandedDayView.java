package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.android.flowercalendar.Alarm.AlarmUtils;
import com.example.android.flowercalendar.DepthPageTransformer;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class BackgroundActivityExpandedDayView extends Fragment {

    private String pickedDay;
    private String alarmHour;
    private String alarmMinutes;
    private boolean isAlarmOn;
    private String shiftNumber;
    private AlarmUtils alarmUtils = new AlarmUtils();

    public BackgroundActivityExpandedDayView() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.background_viewpager, container, false);
        pickedDay = pickedDate();
        Objects.requireNonNull(getActivity()).setTitle(pickedDay);

        // Create an adapter that knows which fragment should be shown on each page
        ViewPagerAdapter adapter = new ViewPagerAdapter(this) {
        };
        ViewPager2 viewPager = rootView.findViewById(R.id.viewpager);
        TabLayout tabs = rootView.findViewById(R.id.tabs);
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Connect the tab layout with the view pager.
        String[] titles = new String[]{getString(R.string.ToDo), getString(R.string.dailySchedule)};

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

        viewPager.setPageTransformer(new DepthPageTransformer());


        setAlarmClockSwitch(rootView);
        return rootView;
    }

    private String pickedDate() {
        assert getArguments() != null;
        return getArguments().getString("pickedDay");

    }


    public String getPickedDate() {
        return pickedDay;
    }

    @SuppressLint("SetTextI18n")
    private void setAlarmClockSwitch(View rootView) {

        if (findShift() != null && !shiftNumber.equals("")) {

            Switch alarmClockSwitch = rootView.findViewById(R.id.alarmClockCheckBox);
            alarmClockSwitch.setVisibility(View.VISIBLE);

            if (isAlarmOn) {
                alarmClockSwitch.setChecked(true);
                alarmClockSwitch.setText("Alarm clock on: " + findAlarmTime());
            } else {
                alarmClockSwitch.setChecked(false);
                alarmClockSwitch.setText(R.string.alarmClockOff);
            }
            alarmClockSwitch.setOnCheckedChangeListener((alarmClockSwitch1, isChecked) -> {

                if (isChecked) {
                    alarmClockSwitch1.setText("Alarm clock on: " + findAlarmTime());

                    if (alarmHour != null) {
                        alarmUtils.setAlarmToPickedDay(alarmHour, alarmMinutes, pickedAlarmDate(), getContext());
                    }

                    CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                    CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(pickedDay);
                    if (shiftTofind != null) {
                        shiftTofind.setAlarmOn(true);
                        calendarEventsDao.update(shiftTofind);
                    }


                } else {
                    alarmClockSwitch1.setText(R.string.alarmClockOff);
                    alarmUtils.deleteAlarmFromAPickedDay(pickedAlarmDate(), alarmHour, alarmMinutes, Objects.requireNonNull(getContext()));
                    CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                    CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(pickedDay);
                    shiftTofind.setAlarmOn(false);
                    calendarEventsDao.update(shiftTofind);
                }
            });
        }
    }


    private String findShift() {

        CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
        CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(pickedDay);
        if (shiftTofind != null) {
            shiftNumber = shiftTofind.getShiftNumber();
            isAlarmOn = shiftTofind.isAlarmOn();
        } else {
            shiftNumber = null;
        }

        return shiftNumber;
    }

    private String findAlarmTime() {

        String alarm;
        ShiftsDao shiftsDao = getDatabase(getContext()).shiftsDao();
        Shift shift = shiftsDao.findByShiftName(findShift());
        if (shift != null) {
            alarm = shift.getAlarm();
            if (!alarm.equals("")) {
                String[] split = alarm.split(":");
                alarmHour = split[0];
                alarmMinutes = split[1];
            } else {
                CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(pickedDay);
                shiftTofind.setAlarmOn(false);
                calendarEventsDao.update(shiftTofind);
                alarm = "no alarm provided";
            }

        } else {
            alarm = "no alarm provided";
        }
        return alarm;

    }


    private LocalDate pickedAlarmDate() {

        String[] parts = pickedDay.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
    }

}
