package com.example.android.flowercalendar.Widget;

import android.content.Context;

import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.PeriodData;
import com.example.android.flowercalendar.database.PeriodDataDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class WidgetData {

    private static List<PeriodData> widgetPeriodData(Context context) {

        LocalDate today = LocalDate.now();
        List<PeriodData> widgetPeriodData = new ArrayList<>();
        PeriodDataDao periodDataDao = CalendarDatabase.getDatabase(context).periodDataDao();
        PeriodData periodData = periodDataDao.findLastPeriod();

        if (periodData != null) {
            String periodStartDate = periodData.getPeriodStartDate();
            String[] split = periodStartDate.split(":");
            LocalDate periodStart = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

            int cycleLength = periodData.getCycleLength();
            int periodLenght = periodData.getPeriodLength();

            for (int i = 0; i < 1000; i++) {

                if (periodStart.plusDays(cycleLength * i).isEqual(today) || (periodStart.plusDays(cycleLength * i).isAfter(today) && periodStart.plusDays(cycleLength * i).isBefore(today.plusDays(7)))) {
                    widgetPeriodData.add(new PeriodData(String.valueOf(periodStart.plusDays(cycleLength * i)), periodLenght, cycleLength));
                } else {
                    break;
                }
            }
        }

        return widgetPeriodData;
    }


    private static LocalDate refactorStringIntoDate(String stringDate) {

        LocalDate searchedDate;
        if (stringDate == null) {
            searchedDate = null;
        } else {
            String[] split = stringDate.split("-");
            searchedDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }

        return searchedDate;
    }


    private static LocalDate periodStart(Context context) {

        String periodStartDay;
        if (!widgetPeriodData(context).isEmpty()) {
            periodStartDay = widgetPeriodData(context).get(0).getPeriodStartDate();
        } else {
            periodStartDay = null;
        }
        return refactorStringIntoDate(periodStartDay);
    }


    private static LocalDate periodFinishDay(Context context) {

        LocalDate periodFinish;
        int periodLenght;
        if (!widgetPeriodData(context).isEmpty()) {
            periodLenght = widgetPeriodData(context).get(0).getPeriodLength();
        } else {
            periodLenght = 0;
        }
        if (periodStart(context) != null) {
            periodFinish = periodStart(context).plusDays(periodLenght);
        } else {
            periodFinish = null;
        }
        return periodFinish;
    }


    static List<DataListWidget> widgetData(Context context) {

        LocalDate today = LocalDate.now();
        int dayOfAMonth = today.getDayOfMonth();
        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();

        List<DataListWidget> widgetData = new ArrayList<>();

        LocalDate periodStart = periodStart(context);
        LocalDate periodFinish = periodFinishDay(context);

        for (int i = 0; i < 7; i++) {

            CalendarEvents event = calendarEventsDao.findBypickedDate(String.valueOf(today.plusDays(i)));


            if (event != null) {

                String shiftNumber;
                if (event.getShiftNumber() != null) {
                    shiftNumber = event.getShiftNumber();
                } else {
                    shiftNumber = "";
                }

                String eventNumber;
                if (event.getEventsNumber() != null) {
                    eventNumber = event.getEventsNumber();
                    if (eventNumber.equals("0")) {
                        eventNumber = "";
                    }
                } else {
                    eventNumber = "";
                }

                widgetData.add(new DataListWidget((dayOfAMonth + i), shiftNumber, eventNumber, periodStart, periodFinish));
            } else {
                widgetData.add(new DataListWidget((dayOfAMonth + i), "", "", periodStart, periodFinish));
            }
        }

        return widgetData;
    }
}