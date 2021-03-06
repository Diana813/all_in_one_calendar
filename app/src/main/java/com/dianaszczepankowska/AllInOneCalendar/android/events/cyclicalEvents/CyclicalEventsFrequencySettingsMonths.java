package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.time.LocalDate;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

class CyclicalEventsFrequencySettingsMonths {


    @SuppressLint("SetTextI18n")
    void monthsFrequencySettings(TextView howOftenHeader, LinearLayout howOftenEditLayout, TextView timeTextView, LinearLayout calendarHeader, RadioGroup monthRadioButton, LinearLayout chooseHowLong, View view2, String startDate, String event_start_date_extra, RadioButton everyFourWeeks, Context context) {

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


        everyFourWeeks.setText(context.getString(R.string.every) + " " + findDayOfWeekOccurence(dayOfMonth, context) + " " + findDayOfWeek(findStartDate(startDate, event_start_date_extra), context) + " " + context.getString(R.string.ofTheMonth));

    }

    private LocalDate findStartDate(String startDate, String event_start_date_extra) {

        LocalDate pickedStartDate;
        if (startDate != null) {
            pickedStartDate = refactorStringIntoDate(startDate);
        } else {
            pickedStartDate = refactorStringIntoDate(event_start_date_extra);
        }
        return pickedStartDate;
    }


    private String findDayOfWeek(LocalDate pickedStartDate, Context context) {

        if (pickedStartDate != null) {
            String pickedDayOfWeek = pickedStartDate.getDayOfWeek().toString().toLowerCase();
            String firstLetter = pickedDayOfWeek.substring(0, 1).toUpperCase();
            String restLetters = pickedDayOfWeek.substring(1);

            String dayOfWeekName = firstLetter + restLetters;

            switch (dayOfWeekName) {
                case "Monday":
                    return context.getString(R.string.monday);
                case "Tuesday":
                    return context.getString(R.string.tuesday);
                case "Wednesday":
                    return context.getString(R.string.wednesday);
                case "Thursday":
                    return context.getString(R.string.thursday);
                case "Friday":
                    return context.getString(R.string.friday);
                case "Saturday":
                    return context.getString(R.string.saturday);
                default:
                    return context.getString(R.string.sunday);
            }
        } else {
            return "?";
        }

    }

    String findDayOfWeekOccurence(Double dayOfMonth, Context context) {

        String numberOfPickedDayOfWeekOccurance;
        if (dayOfMonth / 7 <= 1) {
            numberOfPickedDayOfWeekOccurance = context.getString(R.string.first);
        } else if (dayOfMonth / 7 > 1 && dayOfMonth / 7 <= 2) {
            numberOfPickedDayOfWeekOccurance = context.getString(R.string.second);
        } else if (dayOfMonth / 7 > 2 && dayOfMonth / 7 <= 3) {
            numberOfPickedDayOfWeekOccurance = context.getString(R.string.third);
        } else if (dayOfMonth / 7 > 3 && dayOfMonth / 7 <= 4) {
            numberOfPickedDayOfWeekOccurance = context.getString(R.string.fourth);
        } else {
            numberOfPickedDayOfWeekOccurance = context.getString(R.string.fifth);
        }
        return numberOfPickedDayOfWeekOccurance;
    }
}
