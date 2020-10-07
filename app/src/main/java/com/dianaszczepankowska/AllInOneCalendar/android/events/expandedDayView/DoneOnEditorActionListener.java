package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ExpandableListHoursAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;

import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment.pickedDay;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;

public class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            DialogsUtils.hideKeyboard(v, v.getContext());
            String text = v.getText().toString();
            v.setText(text);

            String schedule = null;
            if (ExpandableListHoursAdapter.schedule != null) {
                schedule = ExpandableListHoursAdapter.schedule;
            }

            UtilsEvents.saveDataEvents(v, currentDate, text, "-1", schedule, EventKind.EVENTS.getIntValue(), UtilsEvents.createEventId(currentDate));
            return true;
        }
        return false;
    }
}
