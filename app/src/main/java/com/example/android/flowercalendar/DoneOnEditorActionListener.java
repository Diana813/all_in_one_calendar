package com.example.android.flowercalendar;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.ExpandedDayView.ExpandableListHoursAdapter;

import static com.example.android.flowercalendar.Calendar.CalendarFragment.pickedDay;

public class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            AppUtils appUtils = new AppUtils();
            appUtils.hideKeyboard(v, v.getContext());
            String text = v.getText().toString();
            v.setText(text);

            String schedule = null;
            if (ExpandableListHoursAdapter.scheduleGroup != null) {
                schedule = ExpandableListHoursAdapter.scheduleGroup;
            } else if (ExpandableListHoursAdapter.scheduleItem != null) {
                schedule = ExpandableListHoursAdapter.scheduleItem;
            }

            appUtils.saveDataEvents(v, pickedDay, text, "-1", schedule, 3);
            return true;
        }
        return false;
    }
}
