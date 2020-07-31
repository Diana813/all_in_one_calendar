package com.dianaszczepankowska.AllInOneCalendar.android.widget;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarBrain;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarViews;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DisplayCyclicalEventsInTheCalendar;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarBrain.findWhatDateIsInTheFirstCellOfTheCalendar;
import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarBrain.lastMondayOfCurrentMonth;
import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarBrain.setDateAtFirstDayOfAMonth;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;

class WidgetData {

    private static LocalDate periodStartDate;
    private static LocalDate periodFinishDate;
    private static int cycleLenght;
    private static int periodLenght;

    static List<CalendarViews> widgetData(Context context) {


        DisplayCyclicalEventsInTheCalendar displayCyclicalEventsInTheCalendar = new DisplayCyclicalEventsInTheCalendar();
        List<CalendarViews> calendarViewsArrayList = new ArrayList<>();
        LocalDate calendarFill = LocalDate.now();
        calendarFill = setDateAtFirstDayOfAMonth(calendarFill);
        LocalDate lastMondayOfCurrentMonth = lastMondayOfCurrentMonth(calendarFill);
        calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
        loadPeriodData(context);
        displayPeriodEveryMonth(calendarFill);
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

            LocalDate firstDayOfTheMonth = setDateAtFirstDayOfAMonth(LocalDate.now());
            LocalDate firstCellOfTheCalendar = findWhatDateIsInTheFirstCellOfTheCalendar(firstDayOfTheMonth);
            LocalDate lastDayOfCalendarView = firstCellOfTheCalendar.plusDays(41);

            ArrayList<String> listOfCyclicalEvents = new ArrayList<>();

            EventsDao cyclicalEventsDao = CalendarDatabase.getDatabase(context).eventsDao();
            List<Event> cyclicalEvents = cyclicalEventsDao.findByKind(0);
            for (Event cyclicalEvent : cyclicalEvents) {
                String[] parts = cyclicalEvent.getFrequency().split("-");

                String pickedDaysOfWeek = parts[3];
                String term;
                if (cyclicalEvent.getTerm() == null) {
                    term = "on_and_on";
                } else {
                    term = cyclicalEvent.getTerm();
                }


                displayCyclicalEventsInTheCalendar.displayCyclicalEvents(cyclicalEvent.getPickedDay(), cyclicalEvent.getFrequency(), pickedDaysOfWeek, term, cyclicalEvent.getEvent_name(), firstCellOfTheCalendar, lastDayOfCalendarView, listOfCyclicalEvents);

            }


            event = CalendarBrain.numberOfEvents(event, context, calendarFill, listOfCyclicalEvents);
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
                            R.mipmap.period_icon_v2, false));

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

                    calendarViewsArrayList.add(new CalendarViews(calendarFill, LocalDate.now(), dayOfMonth, shiftNumber, event, false));

                }


            } else {
                calendarViewsArrayList.add(new CalendarViews(calendarFill, LocalDate.now(), dayOfMonth, shiftNumber, event, false));

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


    private static void loadPeriodData(Context context) {


        PeriodDataDao periodDataDao = getDatabase(context).periodDataDao();
        if (periodDataDao.findLastPeriod() != null) {
            String periodStart = periodDataDao.findLastPeriod().getPeriodStartDate();

            String[] parts;
            if (periodStart.contains(":")) {
                parts = periodStart.split(":");
            } else {
                parts = periodStart.split("-");
            }

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);

            periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            periodLenght = periodDataDao.findLastPeriod().getPeriodLength();
            cycleLenght = periodDataDao.findLastPeriod().getCycleLength();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

        } else {

            periodStartDate = null;
            periodFinishDate = null;

        }

    }

    private static void displayPeriodEveryMonth(LocalDate calendarFill) {

        //Żeby dane na temat okresu wyświetlały się kiedy rozpocznie się następny miesiąc
        if (periodStartDate != null) {
            for (int i = 0; i < 1000; i++) {
                calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
                if (periodFinishDate.isBefore(calendarFill)) {
                    periodStartDate = periodStartDate.plusDays(cycleLenght);
                    periodFinishDate = periodStartDate.plusDays(periodLenght - 1);
                }
                if (periodFinishDate.isEqual(calendarFill) || periodFinishDate.isAfter(calendarFill)) {
                    break;
                }
            }
        }
    }
}
