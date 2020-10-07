package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

import com.dianaszczepankowska.AllInOneCalendar.android.CalendarProviderMethods;
import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DisplayCyclicalEventsInTheCalendar;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.google.android.gms.common.AccountPicker;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;
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
    private static final int REQUEST_CODE_EMAIL = 3;

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


    public void saveShiftToPickedDate(Context context, String newShiftNumber, LocalDate headerDate, LocalDate pickedDate, String email) {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents shiftToUpdate = calendarEventsDao.findBypickedDate(pickedDay);
        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
        Shift shiftToInsert = shiftsDao.findByShiftName(newShiftNumber);
        EventsDao eventsDao = getDatabase(context).eventsDao();


        if (shiftToUpdate != null) {
            Event shift = eventsDao.findByEventNameKindAndPickedDay(shiftToUpdate.getShiftNumber(), pickedDate.toString(), EventKind.SHIFTS.getIntValue());
            if (shiftToUpdate.getShiftNumber() != null && !shiftToUpdate.getShiftNumber().equals("")) {

                if (!shiftToUpdate.getShiftNumber().equals(newShiftNumber) && !shiftToUpdate.getShiftNumber().equals("") && shiftToUpdate.getShiftNumber() != null) {
                    CalendarProviderMethods.updateEvent(context, pickedDate, shiftToInsert.getSchedule(), shiftToInsert.getShift_name(), shiftToInsert.getShift_length(), "Work", shiftToUpdate.getShiftIdForGoogle());
                    shiftToUpdate.setShiftNumber(newShiftNumber);
                    shiftToUpdate.setAlarmOn(true);
                    calendarEventsDao.update(shiftToUpdate);
                    shift.setEvent_name(newShiftNumber);
                    shift.setAlarm(shiftToInsert.getAlarm());
                    shift.setPickedDay(pickedDate.toString());
                    shift.setSchedule(shiftToInsert.getSchedule());
                    shift.setEvent_length(shiftToInsert.getShift_length());
                    eventsDao.update(shift);
                }
            } else {
                String shiftIdForGoogle = UtilsEvents.createEventId(pickedDay);
                CalendarProviderMethods.addEventToGoogleCalendar(context, pickedDate, shiftToInsert.getSchedule(), newShiftNumber, "Work", shiftToInsert.getShift_length(), shiftIdForGoogle, email);
                shiftToUpdate.setShiftNumber(newShiftNumber);
                shiftToUpdate.setShiftIdForGoogle(shiftIdForGoogle);
                shiftToUpdate.setAlarmOn(true);
                calendarEventsDao.update(shiftToUpdate);

                eventsDao.insert(new Event(shiftToInsert.getPosition(), newShiftNumber, shiftToInsert.getSchedule(), shiftToInsert.getAlarm(), shiftToInsert.getShift_length(), pickedDay, EventKind.SHIFTS.getIntValue(), null, null, shiftIdForGoogle));

            }

        } else {
            String shiftIdForGoogle = UtilsEvents.createEventId(pickedDay);
            calendarEventsDao.insert(new CalendarEvents(String.valueOf(headerDate.getMonth().getValue()), true, pickedDay, "", newShiftNumber, shiftIdForGoogle));
            CalendarProviderMethods.addEventToGoogleCalendar(context, pickedDate, shiftToInsert.getSchedule(), newShiftNumber, "Work", shiftToInsert.getShift_length(), shiftIdForGoogle, email);
            //  CalendarProviderMethods.addAttendee(shiftIdForGoogle, context, "Gekrepten", "gekrepten.szczepankowska@gmail.com");
            eventsDao.insert(new Event(shiftToInsert.getPosition(), newShiftNumber, shiftToInsert.getSchedule(), shiftToInsert.getAlarm(), shiftToInsert.getShift_length(), pickedDay, EventKind.SHIFTS.getIntValue(), null, null, shiftIdForGoogle));
        }
    }


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

    public void getUserGoogleAccountEmail() {
        Intent intent =
                AccountPicker.newChooseAccountIntent(
                        new AccountPicker.AccountChooserOptions.Builder()
                                .setAllowableAccountsTypes(Collections.singletonList("com.google"))
                                .build());
        startActivityForResult(intent, REQUEST_CODE_EMAIL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            UserDataDao userDataDao = getDatabase(context).userDataDao();
            UserData owner = userDataDao.findByEmail(email);
            if (owner == null) {
                userDataDao.insert(new UserData(null, email, null));
            }
        }
    }
}
