package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;

import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment.pickedDay;

public class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            AppUtils.hideKeyboard(v, v.getContext());
            String text = v.getText().toString();
            v.setText(text);

            String schedule = null;
            if (ExpandableListHoursAdapter.schedule != null) {
                schedule = ExpandableListHoursAdapter.schedule;
            }

            UtilsEvents.saveDataEvents(v, pickedDay, text, "-1", schedule, 3);
            return true;
        }
        return false;
    }
}
