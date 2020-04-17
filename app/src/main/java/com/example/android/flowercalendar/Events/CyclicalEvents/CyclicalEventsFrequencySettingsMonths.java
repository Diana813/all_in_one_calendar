package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.R;

import java.time.LocalDate;

class CyclicalEventsFrequencySettingsMonths {

    @SuppressLint("SetTextI18n")
    void monthsFrequencySettings(TextView howOftenHeader, LinearLayout howOftenEditLayout, TextView timeTextView, LinearLayout calendarHeader, RadioGroup monthRadioButton, LinearLayout chooseHowLong, View view2, String startDate, String event_start_date_extra, RadioButton everyFourWeeks) {

        howOftenHeader.setText(R.string.months);
        monthRadioButton.setVisibility(View.VISIBLE);
        calendarHeader.setVisibility(View.GONE);
        howOftenEditLayout.setVisibility(View.VISIBLE);
        timeTextView.setText(R.string.month);
        chooseHowLong.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);

        double dayOfMonth;
        if (findStartDate(startDate, event_start_date_extra) != null) {
            dayOfMonth = findStartDate(startDate, event_start_date_extra).getDayOfMonth();
        } else {
            dayOfMonth = -1;
        }


        everyFourWeeks.setText("Every " + findDayOfWeekOccurence(dayOfMonth) + " " + findDayOfWeek(findStartDate(startDate, event_start_date_extra)) + " of the  month");

    }

    private LocalDate findStartDate(String startDate, String event_start_date_extra) {

        LocalDate pickedStartDate;
        if (startDate != null) {
            pickedStartDate = AppUtils.refactorStringIntoDate(startDate);
        } else {
            pickedStartDate = AppUtils.refactorStringIntoDate(event_start_date_extra);
        }
        return pickedStartDate;
    }


    private String findDayOfWeek(LocalDate pickedStartDate) {

        if (pickedStartDate != null) {
            String pickedDayOfWeek = pickedStartDate.getDayOfWeek().toString().toLowerCase();
            String firstLetter = pickedDayOfWeek.substring(0, 1).toUpperCase();
            String restLetters = pickedDayOfWeek.substring(1);
            return firstLetter + restLetters;
        } else {
            return "?";
        }

    }

    String findDayOfWeekOccurence(Double dayOfMonth) {

        String numberOfPickedDayOfWeekOccurance;
        if (dayOfMonth / 7 <= 1) {
            numberOfPickedDayOfWeekOccurance = "first";
        } else if (dayOfMonth / 7 > 1 && dayOfMonth / 7 <= 2) {
            numberOfPickedDayOfWeekOccurance = "second";
        } else if (dayOfMonth / 7 > 2 && dayOfMonth / 7 <= 3) {
            numberOfPickedDayOfWeekOccurance = "third";
        } else if (dayOfMonth / 7 > 3 && dayOfMonth / 7 <= 4) {
            numberOfPickedDayOfWeekOccurance = "fourth";
        } else {
            numberOfPickedDayOfWeekOccurance = "fifth";
        }
        return numberOfPickedDayOfWeekOccurance;
    }
}
