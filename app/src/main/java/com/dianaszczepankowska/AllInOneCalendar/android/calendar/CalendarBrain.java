package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DisplayCyclicalEventsInTheCalendar;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment.pickedDay;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;

public class CalendarBrain extends CalendarFrame {

    public static int cycleLenght;
    public int periodLenght;
    public static LocalDate periodStartDate;
    public static LocalDate periodFinishDate;
    public LocalDate calendarFill;
    public static String shiftNumber;
    String alarmHour;
    String alarmMinute;
    DisplayCyclicalEventsInTheCalendar displayCyclicalEventsInTheCalendar;
    public static ArrayList<String> listOfCyclicalEvents;



    public static String numberOfEvents(String event, Context context, LocalDate calendarFill, ArrayList<String> listOfCyclicalEvents) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (!listOfCyclicalEvents.isEmpty()) {
            for (String cyclicalEvent :
                    listOfCyclicalEvents) {
                String[] parts = cyclicalEvent.split(";");
                Event cyclicalEventInDB = eventsDao.findByEventNameKindAndPickedDay(parts[1], String.valueOf(calendarFill), 1);
                Event scheduledCyclicalEventInDb = eventsDao.findByEventNameKindAndPickedDay(parts[1], String.valueOf(calendarFill), 3);
                if ((parts[0].equals(String.valueOf(calendarFill)) && cyclicalEventInDB == null) && (parts[0].equals(String.valueOf(calendarFill)) && scheduledCyclicalEventInDb == null)) {
                    if (event.equals("")) {
                        event = "0";
                    }
                    event = String.valueOf(Integer.parseInt(event) + 1);
                }
            }
        }
        return event;
    }


    public void saveShiftToPickedDate(Context context, String newShiftNumber, LocalDate headerDate, String alarmHour, String alarmMinute, LocalDate pickedDate) {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents shiftToUpdate = calendarEventsDao.findBypickedDate(pickedDay);

        if (shiftToUpdate != null) {
            //updateAlarmIfShiftIsChanged(shiftToUpdate, newShiftNumber, context, alarmHour, alarmMinute, pickedDate);

            if (shiftToUpdate.getShiftNumber() != null) {
                if (!shiftToUpdate.getShiftNumber().equals(newShiftNumber)) {
                    shiftToUpdate.setShiftNumber(newShiftNumber);
                    shiftToUpdate.setAlarmOn(true);
                    calendarEventsDao.update(shiftToUpdate);


                }
            } else {
                shiftToUpdate.setShiftNumber(newShiftNumber);
                calendarEventsDao.update(shiftToUpdate);
            }

        } else {
            calendarEventsDao.insert(new CalendarEvents(String.valueOf(headerDate.getMonth().getValue()), true, pickedDay, "", newShiftNumber));
        }
    }


   /* public void updateAlarmIfShiftIsChanged(CalendarEvents shiftToUpdate, String newShiftNumber, Context context, String alarmHour, String alarmMinute, LocalDate pickedDate) {

        if (shiftToUpdate.getShiftNumber() != null && !shiftToUpdate.getShiftNumber().equals(newShiftNumber)) {
            ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
            Shift shift = shiftsDao.findByShiftName(shiftToUpdate.getShiftNumber());
            if (shift != null) {
                getAlarmTime(shift);
                AlarmUtils.deleteAlarmFromAPickedDay(pickedDate, alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);
            }


            Shift newShift = shiftsDao.findByShiftName(newShiftNumber);
            if (newShift != null) {
                getAlarmTime(newShift);
                AlarmUtils.setAlarmToPickedDay(alarmHour, alarmMinute, pickedDate, context,  ACTION_OPEN_ALARM_CLASS);
            }
        }
    }*/


    void getAlarmTime(Shift shift) {
        if (shift != null) {
            String alarm = shift.getAlarm();
            if (!alarm.equals("")) {
                String[] split = alarm.split(":");
                alarmHour = split[0];
                alarmMinute = split[1];
            }
        }
    }


    public LocalDate previousPeriodStartDate() {

        int year = calendarFill.getYear();
        int day = calendarFill.getDayOfMonth();
        Month month = calendarFill.getMonth();

        LocalDate firstDayOfPreviousMonth = LocalDate.of(year, month, 1);
        int previousMonthBeginningCell = (firstDayOfPreviousMonth.getDayOfWeek().getValue()) - 1;
        LocalDate firstDayOfPreviousMonthView = firstDayOfPreviousMonth.minusDays(previousMonthBeginningCell);

        for (int i = 0; i < 42; i++) {
            if (periodFinishDate.isAfter(firstDayOfPreviousMonthView)
                    || periodFinishDate.isEqual(firstDayOfPreviousMonthView)) {
                periodStartDate = periodStartDate.minusDays(cycleLenght);
                periodFinishDate = periodFinishDate.minusDays(cycleLenght);

                if (periodStartDate.isBefore(firstDayOfPreviousMonthView.minusDays(periodLenght))) {

                    periodStartDate = periodStartDate.plusDays(cycleLenght);
                    periodFinishDate = periodFinishDate.plusDays(cycleLenght);
                }
            }
        }
        return periodStartDate;
    }


    public static LocalDate setDateAtFirstDayOfAMonth(LocalDate calendarFill) {

        int year = calendarFill.getYear();
        Month month = calendarFill.getMonth();

        //Ustawiam datę na pierwszy dzień aktualnego miesiąca
        calendarFill = LocalDate.of(year, month, 1);

        return calendarFill;
    }


    public static LocalDate lastMondayOfCurrentMonth(LocalDate localeDate) {

        LocalDate firstDayOfNextMonth = localeDate.plusMonths(1);
        int nextMonthBeginningCell =
                (firstDayOfNextMonth.getDayOfWeek().getValue()) - 1;
        return firstDayOfNextMonth.minusDays(nextMonthBeginningCell);
    }


    public static LocalDate findWhatDateIsInTheFirstCellOfTheCalendar(LocalDate calendarFill) {

        //Sprawdzam, w której komórce kalendarza znajduje się pierwszy dzień miesiąca,
        //poprzez sprawdzenie odpowiadającego mu numeru dnia tygodnia. Odejmuję jeden, bo
        //poniedziałek ma numer 1, a komórki w ArrayList są naliczane od 0.
        int monthBeginningCell = (calendarFill.getDayOfWeek().getValue()) - 1;
        //Ustawiam datę na tę, która powinna się znaleźć w pierwszej komórce mojej ArrayList
        calendarFill = calendarFill.minusDays(monthBeginningCell);
        return calendarFill;
    }


    void deleteAllShifts(LocalDate date) {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        calendarEventsDao.deleteAllShifts(String.valueOf(date.getMonth().getValue()));
        calendarFill = date;
        if (periodStartDate != null) {
            periodStartDate = previousPeriodStartDate();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);
        }
    }
}
