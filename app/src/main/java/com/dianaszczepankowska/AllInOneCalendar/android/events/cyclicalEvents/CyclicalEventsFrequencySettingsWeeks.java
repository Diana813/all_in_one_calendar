package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.util.List;

class CyclicalEventsFrequencySettingsWeeks {

    void weeksFrequencySettings(TextView howOftenHeader, LinearLayout howOftenEditLayout, TextView timeTextView, LinearLayout calendarHeader, RadioGroup monthRadioButton, LinearLayout chooseHowLong, View view2) {

        howOftenHeader.setText(R.string.weeks);
        calendarHeader.setVisibility(View.VISIBLE);
        howOftenEditLayout.setVisibility(View.VISIBLE);
        timeTextView.setText(R.string.week);
        monthRadioButton.setVisibility(View.GONE);
        chooseHowLong.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
    }


    private void highlightDayOfAWeek(TextView textView, String dayOfAWeek, List<String> arrayOfpickedDaysOfAWeek) {
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.color.colorAccent);
        if (arrayOfpickedDaysOfAWeek != null) {
            arrayOfpickedDaysOfAWeek.add(dayOfAWeek);
        }
    }

    private void disableDayOfAWeek(TextView textView, String dayOfAWeek, List<String> arrayOfpickedDaysOfAWeek) {
        textView.setTextColor(Color.parseColor("#222222"));
        textView.setBackgroundResource(R.color.colorWhite);
        arrayOfpickedDaysOfAWeek.remove(dayOfAWeek);
    }

    void enableDisable(int i, TextView textView, String dayOfAWeek, List<String> arrayOfpickedDaysOfAWeek) {
        if (i % 2 == 0) {
            disableDayOfAWeek(textView, dayOfAWeek, arrayOfpickedDaysOfAWeek);
        } else {
            highlightDayOfAWeek(textView, dayOfAWeek, arrayOfpickedDaysOfAWeek);
        }
    }

    void highlightDaysOfWeekIfChoosen(String whichDaysOfWeek, String nameOfTheDay, TextView textView) {
        if (whichDaysOfWeek.contains(nameOfTheDay)) {
            highlightDayOfAWeek(textView, nameOfTheDay, null);
        }

    }
}
