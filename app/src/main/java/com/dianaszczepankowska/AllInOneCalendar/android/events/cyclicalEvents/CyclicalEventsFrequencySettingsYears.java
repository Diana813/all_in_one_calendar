package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

class CyclicalEventsFrequencySettingsYears {

    void yearsFrequencySettings(TextView howOftenHeader, LinearLayout howOftenEditLayout, TextView timeTextView, LinearLayout calendarHeader, RadioGroup monthRadioButton, LinearLayout chooseHowLong, View view2) {

        howOftenHeader.setText(R.string.Years);
        howOftenEditLayout.setVisibility(View.VISIBLE);
        timeTextView.setText(R.string.years);
        calendarHeader.setVisibility(View.GONE);
        monthRadioButton.setVisibility(View.GONE);
        chooseHowLong.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
    }
}
