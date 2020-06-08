package com.example.android.flowercalendar.events.CyclicalEvents;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;

class CyclicalEventsFrequencySettingsDays {

    void daysFrequencySettings(TextView howOftenHeader, LinearLayout howOftenEditLayout, TextView timeTextView, LinearLayout calendarHeader, RadioGroup monthRadioButton, LinearLayout chooseHowLong, View view2) {

        howOftenHeader.setText(R.string.days);
        howOftenEditLayout.setVisibility(View.VISIBLE);
        timeTextView.setText(R.string.day);
        calendarHeader.setVisibility(View.GONE);
        monthRadioButton.setVisibility(View.GONE);
        chooseHowLong.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
    }
}
