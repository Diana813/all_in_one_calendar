package com.example.android.flowercalendar.widget;

import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.calendar.CalendarViews;
import com.example.android.flowercalendar.utils.AppUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class WidgetData {

    static List<CalendarViews> widgetData() {

        LocalDate today = LocalDate.now();
        List<CalendarViews> widgetData = new ArrayList<>();

        for (CalendarViews calendarView : CalendarFragment.calendarViewsArrayList) {
            if (AppUtils.isEqualOrBetweenDates(calendarView.getmCalendarFill(), today, today.plusDays(7))) {
                widgetData.add(calendarView);
            }
        }

        return widgetData;
    }
}
