package com.dianaszczepankowska.AllInOneCalendar.android.widget.events;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WidgetData {
    static List<Event> widgetData(Context context) {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        List<Event> allWidgetEvents = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<Event> widgetEvents = eventsDao.findByDateExcept(LocalDate.now().plusDays(i).toString(), EventKind.SHIFTS.getIntValue());
            allWidgetEvents.addAll(widgetEvents);
        }
        if (allWidgetEvents.isEmpty()) {
            allWidgetEvents.add(new Event(0, context.getString(R.string.no_plans), null, null, 0, LocalDate.now().toString(), EventKind.EVENTS.getIntValue(), null, null, null));
        }
        return allWidgetEvents;
    }
}
