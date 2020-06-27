package com.example.android.flowercalendar.widget;

import android.content.Context;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.calendar.CalendarBrain;
import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.calendar.CalendarViews;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.utils.AppUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.flowercalendar.calendar.CalendarBrain.cycleLenght;
import static com.example.android.flowercalendar.calendar.CalendarBrain.findWhatDateIsInTheFirstCellOfTheCalendar;
import static com.example.android.flowercalendar.calendar.CalendarBrain.lastMondayOfCurrentMonth;
import static com.example.android.flowercalendar.calendar.CalendarBrain.periodFinishDate;
import static com.example.android.flowercalendar.calendar.CalendarBrain.periodStartDate;
import static com.example.android.flowercalendar.calendar.CalendarBrain.setDateAtFirstDayOfAMonth;
import static com.example.android.flowercalendar.calendar.CalendarFragment.calendarViewsArrayList;

class WidgetData {

    static List<CalendarViews> widgetData(Context context) {

        List<CalendarViews> calendarViewsArrayList = new ArrayList<>();
        LocalDate calendarFill = LocalDate.now();
        calendarFill = setDateAtFirstDayOfAMonth(calendarFill);
        LocalDate lastMondayOfCurrentMonth = lastMondayOfCurrentMonth(calendarFill);
        calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
        //Wypełniam kalendarz
        while (calendarViewsArrayList.size() < 42) {

            CalendarEventsDao eventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
            CalendarEvents calendarEvent = eventsDao.findBypickedDate(calendarFill.toString());
            String shiftNumber;
            String event;
            int dayOfMonth = calendarFill.getDayOfMonth();

            if (calendarEvent == null) {
                shiftNumber = "";
                event = "";
            } else {
                shiftNumber = calendarEvent.getShiftNumber();
                if (calendarEvent.getShiftNumber() == null) {
                    shiftNumber = "";
                }
                event = calendarEvent.getEventsNumber();
                if (calendarEvent.getEventsNumber().equals("0")
                        || calendarEvent.getEventsNumber() == null) {
                    event = "";
                }
            }

            event = CalendarBrain.numberOfEvents(event, context, calendarFill);

            if (periodStartDate != null) {
                if ((periodStartDate.isEqual(calendarFill) ||
                        periodFinishDate.isEqual(calendarFill)) ||
                        (periodStartDate.isBefore(calendarFill) &&
                                periodFinishDate.isAfter(calendarFill))) {

                    calendarViewsArrayList.add(new CalendarViews(
                            calendarFill,
                            LocalDate.now(),
                            periodStartDate,
                            periodFinishDate,
                            dayOfMonth,
                            shiftNumber,
                            event,
                            R.mipmap.period_icon_v2));

                    //Kiedy w kalendarzu wypełni się komórka z ostatnim dniem okresu
                    //trzeba dodać długość cyklu do daty początkowej i do daty końcowej okresu,
                    //ale tylko pod warunkiem, że ten okres nie powinien wyświetlić się na
                    //następnej stronie kalendarza. Jeśli musi się wyświetlić, data tego okresu powinna
                    //zostać taka jak była
                    if (periodFinishDate.isEqual(calendarFill) &&
                            periodFinishDate.isBefore(lastMondayOfCurrentMonth)) {

                        periodStartDate = periodStartDate.plusDays(cycleLenght);
                        periodFinishDate = periodFinishDate.plusDays(cycleLenght);

                    }
                } else {

                    calendarViewsArrayList.add(new CalendarViews(calendarFill, LocalDate.now(), dayOfMonth, shiftNumber, event));

                }


            } else {
                calendarViewsArrayList.add(new CalendarViews(calendarFill, LocalDate.now(), dayOfMonth, shiftNumber, event));

            }
            calendarFill = calendarFill.plusDays(1);
        }


        LocalDate today = LocalDate.now();
        List<CalendarViews> widgetData = new ArrayList<>();

        for (
                CalendarViews calendarView : calendarViewsArrayList) {
            if (AppUtils.isEqualOrBetweenDates(calendarView.getmCalendarFill(), today, today.plusDays(7))) {
                widgetData.add(calendarView);
            }
        }

        return widgetData;
    }
}
