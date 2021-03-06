package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.firstInMonth;


public class DisplayCyclicalEventsInTheCalendar extends UpcomingCyclicalEvent {


    public void displayCyclicalEvents(String startDate, String frequency, String pickedDaysOfWeek, String term, String eventName, LocalDate firstDayOfCalendarView, LocalDate lastDayOfCalendarView, ArrayList<String> listOfCyclicalEvents) {


        LocalDate startCyclicalEvent = DateUtils.refactorStringIntoDate(startDate);

        //days
        if (frequency.substring(0, 6).equals("0-0-0-")) {

            int dayFrequency = Integer.parseInt(frequency.substring(6));

            LocalDate finishCyclicalEventDate = null;

            if (term.contains("on_and_on")) {

                startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency);

                listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);

            } else if (term.contains(",")) {
                finishCyclicalEventDate = pickedFinishDate(term);
                if (findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency).isBefore(finishCyclicalEventDate)) {
                    startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency);
                    listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                } else {
                    return;
                }

            } else {

                finishCyclicalEventDate = startCyclicalEvent.plusDays(dayFrequency * Integer.parseInt(term));

                if (findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency).isBefore(finishCyclicalEventDate)) {
                    startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, dayFrequency);
                    listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                } else {
                    return;
                }
            }


            for (int i = 0; i < 50; i++) {
                if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {
                    startCyclicalEvent = startCyclicalEvent.plusDays(dayFrequency);

                    if (finishCyclicalEventDate != null && startCyclicalEvent.isAfter(finishCyclicalEventDate)) {
                        break;
                    }
                    listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                } else {
                    break;
                }
            }


            //weeks
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && frequency.substring(0, 4).equals("0-0-")) {

            int weeksFrequency = Integer.parseInt(frequency.substring(4, 5));


            startCyclicalEvent = findNewStartEventDate(startCyclicalEvent, firstDayOfCalendarView, (weeksFrequency * 7));

            String startDateDayOfAWeek = startCyclicalEvent.getDayOfWeek().toString().toLowerCase().substring(0, 3);
            if (pickedDaysOfWeek.contains(startDateDayOfAWeek)) {
                listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
            }


            LocalDate finishCyclicalEventDate;

            if (term.contains("on_and_on")) {
                finishCyclicalEventDate = null;
            } else if (term.contains(",")) {
                finishCyclicalEventDate = pickedFinishDate(term);

            } else {
                finishCyclicalEventDate = startCyclicalEvent.plusWeeks(weeksFrequency * Integer.parseInt(term));
            }

            if (pickedDaysOfWeek.contains("mon")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.MONDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }

            if (pickedDaysOfWeek.contains("tue")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.TUESDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }

            if (pickedDaysOfWeek.contains("wed")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.WEDNESDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }

            if (pickedDaysOfWeek.contains("thr")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.THURSDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }

            if (pickedDaysOfWeek.contains("fri")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.FRIDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }

            if (pickedDaysOfWeek.contains("sat")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.SATURDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }
            if (pickedDaysOfWeek.contains("sun")) {
                createListOfEventsForFrequencyWeeks(startCyclicalEvent, weeksFrequency, DayOfWeek.SUNDAY, eventName, firstDayOfCalendarView, lastDayOfCalendarView, startDate, finishCyclicalEventDate, listOfCyclicalEvents);
            }
            //months
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && !frequency.substring(0, 4).equals("0-0-")
                && frequency.substring(0, 2).equals("0-")) {

            int monthsFrequency = Integer.parseInt(frequency.substring(2, 3));

            LocalDate finishCyclicalEventDate;

            if (term.contains("on_and_on")) {
                finishCyclicalEventDate = null;
            } else if (term.contains(",")) {
                finishCyclicalEventDate = pickedFinishDate(term);

            } else {
                finishCyclicalEventDate = startCyclicalEvent.plusMonths(monthsFrequency * Integer.parseInt(term));
            }

            if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {
                listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
            }

            String weeksOrMonths = frequency.substring(4, 6);

            if (weeksOrMonths.contains("*m")) {
                for (int i = 0; i < 1000; i++) {
                    if (firstDayOfCalendarView.getMonthValue() > startCyclicalEvent.getMonthValue() || lastDayOfCalendarView.isAfter(startCyclicalEvent) || firstDayOfCalendarView.getYear() > startCyclicalEvent.getYear()) {
                        startCyclicalEvent = startCyclicalEvent.plusMonths(monthsFrequency);

                        if (finishCyclicalEventDate != null && startCyclicalEvent.isAfter(finishCyclicalEventDate)) {

                            break;
                        }

                        if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {
                            listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
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

                        if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {

                            if (finishCyclicalEventDate != null && startCyclicalEvent.isAfter(finishCyclicalEventDate)) {
                                break;
                            }
                            listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                        }
                    } else {
                        break;
                    }
                }
            }
            //years
        } else {

            int yearsFrequency;
            if (frequency.substring(0, 1).equals("-")) {
                yearsFrequency = 0;
            } else {
                yearsFrequency = Integer.parseInt(frequency.substring(0, 1));
            }

            LocalDate finishCyclicalEventDate;

            if (term.contains("on_and_on")) {
                finishCyclicalEventDate = null;
            } else if (term.contains(",")) {
                finishCyclicalEventDate = pickedFinishDate(term);

            } else {
                finishCyclicalEventDate = startCyclicalEvent.plusYears(yearsFrequency * Integer.parseInt(term));
            }

            if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {
                listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
            }
            for (int i = 0; i < 150; i++) {
                if (firstDayOfCalendarView.getYear() > startCyclicalEvent.getYear()) {
                    startCyclicalEvent = startCyclicalEvent.plusYears(yearsFrequency);

                    if (finishCyclicalEventDate != null && startCyclicalEvent.isAfter(finishCyclicalEventDate)) {
                        break;
                    }

                    if (DialogsUtils.isEqualOrBetweenDates(startCyclicalEvent, firstDayOfCalendarView, lastDayOfCalendarView)) {
                        listOfCyclicalEvents.add(startCyclicalEvent + ";" + eventName);
                    }
                } else {
                    break;
                }
            }
        }
    }


    private void createListOfEventsForFrequencyWeeks(LocalDate startCyclicalEvent,
                                                     int weeksFrequency, DayOfWeek dayOfWeek,
                                                     String eventName,
                                                     LocalDate firstDayOfCalendarView,
                                                     LocalDate lastDayOfCalendarView,
                                                     String startDate,
                                                     LocalDate finishCyclicalEventDate,
                                                     ArrayList<String> listOfCyclicalEvents) {

        LocalDate nextEv = dateAccordingToChoosenDayOfTheWeek(startCyclicalEvent, dayOfWeek, String.valueOf(weeksFrequency));

        LocalDate firstCyclicalEvent = DateUtils.refactorStringIntoDate(startDate);

        if (DialogsUtils.isEqualOrBetweenDates(nextEv, firstDayOfCalendarView, lastDayOfCalendarView)) {
            for (int i = 0; i < 10; i++) {
                if ((nextEv.minusWeeks(weeksFrequency).isAfter(firstDayOfCalendarView) || nextEv.minusWeeks(weeksFrequency).isEqual(firstDayOfCalendarView)) && (nextEv.minusWeeks(weeksFrequency).isAfter(firstCyclicalEvent))) {
                    nextEv = nextEv.minusWeeks(weeksFrequency);
                } else {
                    break;
                }

            }

            if (finishCyclicalEventDate != null && nextEv.isAfter(finishCyclicalEventDate)) {
                return;
            }

            listOfCyclicalEvents.add(nextEv + ";" + eventName);

        }

        for (int i = 0; i < 10; i++) {
            if (DialogsUtils.isEqualOrBetweenDates(nextEv, firstDayOfCalendarView, lastDayOfCalendarView)) {
                nextEv = nextEv.plusWeeks(weeksFrequency);


                if (finishCyclicalEventDate != null && nextEv.isAfter(finishCyclicalEventDate)) {
                    break;
                }

                listOfCyclicalEvents.add(nextEv + ";" + eventName);


            } else {
                break;
            }
        }

    }


    public LocalDate findNewStartEventDate(LocalDate startCyclicalEvent, LocalDate
            firstDayOfCalendarView, int frequency) {

        LocalDate newStartCyclicalEvent = startCyclicalEvent;
        long daysBetweenStartDateAndFirstDayOfCalendarView = DAYS.between(startCyclicalEvent, firstDayOfCalendarView) - 1;

        if (newStartCyclicalEvent.isBefore(firstDayOfCalendarView)) {
            int howManyTimesFrequency = (int) (daysBetweenStartDateAndFirstDayOfCalendarView / frequency);

            newStartCyclicalEvent = newStartCyclicalEvent.plusDays(howManyTimesFrequency * frequency + frequency);

        }
        return newStartCyclicalEvent;
    }

    int findDayOfWeekOccurence(Double dayOfMonth) {

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