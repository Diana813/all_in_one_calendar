package com.example.android.flowercalendar.events.ExpandedDayView;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.events.ExpandedDayView.ExpandableListHoursAdapter;

import static com.example.android.flowercalendar.calendar.CalendarFragment.pickedDay;

public class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            AppUtils appUtils = new AppUtils();
            appUtils.hideKeyboard(v, v.getContext());
            String text = v.getText().toString();
            v.setText(text);

            String schedule = null;
            if (ExpandableListHoursAdapter.schedule != null) {
                schedule = ExpandableListHoursAdapter.schedule;
            }

            appUtils.saveDataEvents(v, pickedDay, text, "-1", schedule, 3);
            return true;
        }
        return false;
    }
}
