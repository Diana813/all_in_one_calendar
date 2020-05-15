package com.example.android.flowercalendar.Events.CyclicalEvents;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.database.CalendarEvents;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.firstInMonth;


public class DisplayCyclicalEventsInTheCalendar extends UpcomingCyclicalEvent {


    public void displayCyclicalEvents(String startDate, String frequency, String pickedDaysOfWeek, String term, String eventName, LocalDate firstDayOfCalendarView, LocalDate lastDayOfCalendarView) {


        LocalDate startCyclicalEvent = AppUtils.refactorStringIntoDate(startDate);

        //days
        if (frequency.substring(0, 6).equals("0-0-0-")) {

            int dayFrequency = Integer.parseInt(frequency.substring(6));

            startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency);
            CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);

            for (int i = 0; i < 50; i++) {
                if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                    startCyclicalEvent = startCyclicalEvent.plusDays(dayFrequency);
                    CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                } else {
                    break;
                }
            }


            //weeks
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && frequency.substring(0, 4).equals("0-0-")) {

            int weeksFrequency = Integer.parseInt(frequency.substring(4, 5));


            startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, (weeksFrequency * 7));


            if (pickedDaysOfWeek.contains("mon")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.MONDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }

            if (pickedDaysOfWeek.contains("tue")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.TUESDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }

            if (pickedDaysOfWeek.contains("wed")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.WEDNESDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }

            if (pickedDaysOfWeek.contains("thr")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.THURSDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }

            if (pickedDaysOfWeek.contains("fri")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.FRIDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }

            if (pickedDaysOfWeek.contains("sat")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.SATURDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }
            if (pickedDaysOfWeek.contains("sun")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.SUNDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate);
            }
            //months
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && !frequency.substring(0, 4).equals("0-0-")
                && frequency.substring(0, 2).equals("0-")) {

            int monthsFrequency = Integer.parseInt(frequency.substring(2, 3));

            if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
            }

            String weeksOrMonths = frequency.substring(4, 6);

            if (weeksOrMonths.contains("*m")) {
                for (int i = 0; i < 1000; i++) {
                    if (firstDayOfCalendarView.getMonthValue() > startCyclicalEvent.getMonthValue() || lastDayOfCalendarView.isAfter(startCyclicalEvent) || firstDayOfCalendarView.getYear() > startCyclicalEvent.getYear()) {
                        startCyclicalEvent = startCyclicalEvent.plusMonths(monthsFrequency);

                        if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                            CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                        }
                    } else {
                        break;
                    }
                }
            } else {
                for (int i = 0; i < 1000; i++) {
                    if (firstDayOfCalendarView.getMonthValue() > startCyclicalEvent.getMonthValue() || lastDayOfCalendarView.isAfter(startCyclicalEvent) || firstDayOfCalendarView.getYear() > startCyclicalEvent.getYear()) {

                        DayOfWeek dayOfWeek = startCyclicalEvent.getDayOfWeek();
                        LocalDate firstOcurranceDayOfWeek = startCyclicalEvent.plusMonths(monthsFrequency).with(firstInMonth(dayOfWeek));
                        startCyclicalEvent = firstOcurranceDayOfWeek.plusWeeks(findDayOfWeekOccurence((double) startCyclicalEvent.getDayOfMonth()) - 1);

                        if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                            CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                        }
                    } else {
                        break;
                    }
                }
            }
            //years
        } else {

            int yearsFrequency = Integer.parseInt(frequency.substring(0, 1));

            if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
            }
            for (int i = 0; i < 150; i++) {
                if (firstDayOfCalendarView.getYear() > startCyclicalEvent.getYear()) {
                    startCyclicalEvent = startCyclicalEvent.plusYears(yearsFrequency);

                    if ((startCyclicalEvent.isAfter(firstDayOfCalendarView) || startCyclicalEvent.isEqual(firstDayOfCalendarView)) && (startCyclicalEvent.isBefore(lastDayOfCalendarView) || startCyclicalEvent.isEqual(lastDayOfCalendarView))) {
                        CalendarFragment.listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                    }
                } else {
                    break;
                }
            }
        }
    }


    private void createListOfEventsForFrequencyWeeks(LocalDate startCyclicalEvent,
                                                     int weeksFrequency, DayOfWeek dayOfWeek, String eventName, LocalDate
                                                             firstDayOfCalendarView, LocalDate lastDayOfCalendarView, String startDate) {

        LocalDate nextEv = dateAccordingToChoosenDayOfTheWeek(startCyclicalEvent, dayOfWeek, String.valueOf(weeksFrequency));

        LocalDate firstCyclicalEvent = AppUtils.refactorStringIntoDate(startDate);

        if ((nextEv.isAfter(firstDayOfCalendarView) || nextEv.isEqual(firstDayOfCalendarView)) && (nextEv.isBefore(lastDayOfCalendarView) || nextEv.isEqual(lastDayOfCalendarView))) {
            for (int i = 0; i < 10; i++) {
                if ((nextEv.minusWeeks(weeksFrequency).isAfter(firstDayOfCalendarView) || nextEv.minusWeeks(weeksFrequency).isEqual(firstDayOfCalendarView)) && (nextEv.minusWeeks(weeksFrequency).isAfter(firstCyclicalEvent) || nextEv.minusWeeks(weeksFrequency).isEqual(firstCyclicalEvent))) {
                    nextEv = nextEv.minusWeeks(weeksFrequency);
                } else {
                    break;
                }

            }

            CalendarFragment.listOfCyclicalEvents.add(nextEv + ";" + eventName);

        }

        for (int i = 0; i < 10; i++) {
            if ((nextEv.isAfter(firstDayOfCalendarView) || nextEv.isEqual(firstDayOfCalendarView)) && (nextEv.isBefore(lastDayOfCalendarView) || nextEv.isEqual(lastDayOfCalendarView))) {
                nextEv = nextEv.plusWeeks(weeksFrequency);
                if ((nextEv.isAfter(firstDayOfCalendarView) || nextEv.isEqual(firstDayOfCalendarView)) && (nextEv.isBefore(lastDayOfCalendarView) || nextEv.isEqual(lastDayOfCalendarView))) {
                    CalendarFragment.listOfCyclicalEvents.add(nextEv + ";" + eventName);
                }

            } else {
                break;
            }
        }

    }

    private LocalDate findNewStartEventDate(LocalDate startCyclicalEvent, LocalDate
            firstDayOfCalendarView, int frequency) {

        LocalDate newStartCyclicalEvent = startCyclicalEvent;
        long daysBetweenStartDateAndFirstDayOfCalendarView = DAYS.between(startCyclicalEvent, firstDayOfCalendarView) - 1;

        if (newStartCyclicalEvent.isBefore(firstDayOfCalendarView)) {
            int howManyTimesFrequency = (int) (daysBetweenStartDateAndFirstDayOfCalendarView / frequency);

            newStartCyclicalEvent = newStartCyclicalEvent.plusDays(howManyTimesFrequency * frequency + frequency);

        }
        return newStartCyclicalEvent;
    }

    private int findDayOfWeekOccurence(Double dayOfMonth) {

        int numberOfPickedDayOfWeekOccurance;
        if (dayOfMonth / 7 <= 1) {
            numberOfPickedDayOfWeekOccurance = 1;
        } else if (dayOfMonth / 7 > 1 && dayOfMonth / 7 <= 2) {
            numberOfPickedDayOfWeekOccurance = 2;
        } else if (dayOfMonth / 7 > 2 && dayOfMonth / 7 <= 3) {
            numberOfPickedDayOfWeekOccurance = 3;
        } else if (dayOfMonth / 7 > 3 && dayOfMonth / 7 <= 4) {
            numberOfPickedDayOfWeekOccurance = 4;
        } else {
            numberOfPickedDayOfWeekOccurance = 5;
        }
        return numberOfPickedDayOfWeekOccurance;
    }
}