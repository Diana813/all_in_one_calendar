package com.example.android.flowercalendar.events.CyclicalEvents;

import com.example.android.flowercalendar.AppUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;

public class UpcomingCyclicalEvent {


    LocalDate displayNextEvent(String startDate, String frequency, String pickedDaysOfWeek, String term) {

        String[] parts = frequency.split("-");
        LocalDate nextEvent;
        LocalDate pickedStartDate = AppUtils.refactorStringIntoDate(startDate);

        //days
        if (frequency.substring(0, 6).equals("0-0-0-")) {

            nextEvent = displayDateIfTheFrequencyIsDays(parts, pickedStartDate, term);

            //weeks
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && frequency.substring(0, 4).equals("0-0-")) {

            nextEvent = displayDateIfTheFrequencyIsWeeks(parts, pickedStartDate, pickedDaysOfWeek, term);

            //months
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && !frequency.substring(0, 4).equals("0-0-")
                && frequency.substring(0, 2).equals("0-")) {

            nextEvent = displayDateIfTheFrequencyIsMonths(parts, pickedStartDate, frequency, term);

            //years
        } else {

            nextEvent = displayDateIfTheFrequencyIsYears(parts, pickedStartDate, term);

        }

        return nextEvent;
    }


    private LocalDate displayDateIfTheFrequencyIsDays(String[] parts, LocalDate pickedStartDate, String term) {

        String howManyDays = parts[3];
        int howManyTimesLoop = howManyTimesRepeatEvent(term);
        for (int i = 0; i < howManyTimesLoop; i++) {

            if (pickedStartDate.plusDays(Integer.parseInt(howManyDays)).
                    isBefore(LocalDate.now())) {

                pickedStartDate = pickedStartDate.plusDays(Integer.parseInt(howManyDays));

                LocalDate pickedFinishDate = pickedFinishDate(term);
                if (pickedFinishDate != null) {
                    if (pickedStartDate.isAfter(pickedFinishDate) || pickedStartDate.isEqual(pickedFinishDate)) {
                        pickedStartDate = null;
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        return pickedStartDate;
    }


    private LocalDate displayDateIfTheFrequencyIsWeeks(String[] parts, LocalDate pickedStartDate, String pickedDaysOfWeek, String term) {

        String howManyWeeks = parts[2];

        int howManyTimesLoop = howManyTimesRepeatEvent(term);
        for (int i = 0; i < howManyTimesLoop; i++) {
            if (pickedStartDate.isBefore(LocalDate.now())) {
                pickedStartDate = pickedStartDate.plusWeeks(Integer.parseInt(howManyWeeks));

                LocalDate pickedFinishDate = pickedFinishDate(term);
                if (pickedFinishDate != null) {
                    if (pickedStartDate.isAfter(pickedFinishDate) || pickedStartDate.isEqual(pickedFinishDate)) {
                        pickedStartDate = null;
                        break;
                    }
                }
            } else {
                break;
            }
        }


        ArrayList<LocalDate> listOfDates = listOfDates(pickedStartDate, pickedDaysOfWeek, howManyWeeks);

        if (!listOfDates.isEmpty()) {
            Collections.sort(listOfDates);
            return listOfDates.get(0);
        } else if (pickedStartDate != null) {
            return pickedStartDate.plusWeeks(Integer.parseInt(howManyWeeks));
        } else {
            return null;
        }
    }


    public static LocalDate dateAccordingToChoosenDayOfTheWeek(LocalDate pickedStartDate, DayOfWeek dayOfWeek, String howManyWeeks) {

        LocalDate startDayOfAWeek;

        if (Integer.parseInt(howManyWeeks) == 1) {
            if (pickedStartDate != null) {
                startDayOfAWeek = pickedStartDate.with(TemporalAdjusters.next(dayOfWeek));
            } else {
                startDayOfAWeek = null;
            }
        } else {
            if (pickedStartDate != null) {
                if (pickedStartDate.getDayOfWeek().getValue() > dayOfWeek.getValue()) {
                    startDayOfAWeek = pickedStartDate.plusWeeks(Integer.parseInt(howManyWeeks) - 1).with(TemporalAdjusters.next(dayOfWeek));
                } else if (pickedStartDate.getDayOfWeek().getValue() == dayOfWeek.getValue()) {
                    startDayOfAWeek = pickedStartDate;
                } else {
                    startDayOfAWeek = pickedStartDate.with(TemporalAdjusters.next(dayOfWeek));
                }
            } else {
                startDayOfAWeek = null;
            }
        }
        return startDayOfAWeek;
    }


    private LocalDate displayDateIfTheFrequencyIsMonths(String[] parts, LocalDate
            pickedStartDate, String frequency, String term) {

        CyclicalEventsFrequencySettingsMonths settingsMonths = new CyclicalEventsFrequencySettingsMonths();

        String howManyMonths = parts[1];

        int howManyTimesLoop = howManyTimesRepeatEvent(term);

        for (int i = 0; i < howManyTimesLoop; i++) {

            if (pickedStartDate.isBefore(LocalDate.now())) {
                pickedStartDate = pickedStartDate.plusMonths(Integer.parseInt(howManyMonths));

                LocalDate pickedFinishDate = pickedFinishDate(term);
                if (pickedFinishDate != null) {
                    if (pickedStartDate.isAfter(pickedFinishDate) || pickedStartDate.isEqual(pickedFinishDate)) {
                        pickedStartDate = null;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        LocalDate nextEvent = null;

        if (frequency.contains("*months")) {
            if (pickedStartDate != null) {
                if (pickedStartDate.isBefore(LocalDate.now())) {
                    nextEvent = pickedStartDate.plusMonths(Integer.parseInt(howManyMonths));
                } else {
                    nextEvent = pickedStartDate;
                }
            }

        } else if (frequency.contains("*4weeks")) {

            if (pickedStartDate != null) {
                double whichWeekOfMonth = pickedStartDate.getDayOfMonth();
                String whichWeek = settingsMonths.findDayOfWeekOccurence(whichWeekOfMonth);

                int i;
                switch (whichWeek) {
                    case "first":
                        i = 1;
                        break;
                    case "second":
                        i = 2;
                        break;
                    case "third":
                        i = 3;
                        break;
                    case "fourth":
                        i = 4;
                        break;
                    default:
                        i = 5;
                        break;
                }
                DayOfWeek day = pickedStartDate.getDayOfWeek();
                if (pickedStartDate.isBefore(LocalDate.now())) {
                    nextEvent = pickedStartDate.plusMonths(Integer.parseInt(howManyMonths)).with(TemporalAdjusters.dayOfWeekInMonth(i, day));
                } else {
                    nextEvent = pickedStartDate;
                }
            }
        }
        return nextEvent;
    }


    private LocalDate displayDateIfTheFrequencyIsYears(String[] parts, LocalDate
            pickedStartDate, String term) {

        String howManyYears = parts[0];
        int howManyTimesLoop = howManyTimesRepeatEvent(term);

        for (int i = 0; i < howManyTimesLoop; i++) {
            if (pickedStartDate.isBefore(LocalDate.now())) {
                pickedStartDate = pickedStartDate.plusYears(Integer.parseInt(howManyYears));

                LocalDate pickedFinishDate = pickedFinishDate(term);
                if (pickedFinishDate != null) {
                    if (pickedStartDate.isAfter(pickedFinishDate) || pickedStartDate.isEqual(pickedFinishDate)) {
                        pickedStartDate = null;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return pickedStartDate;
    }


    private int howManyTimesRepeatEvent(String term) {

        int howManyTimesRepeatEvent;
        if (term.contains("on_and_on")) {
            howManyTimesRepeatEvent = 10000;
        } else if (term.contains(",")) {
            howManyTimesRepeatEvent = 10000;
        } else {
            howManyTimesRepeatEvent = Integer.parseInt(term);
        }

        return howManyTimesRepeatEvent;
    }


    LocalDate pickedFinishDate(String term) {

        LocalDate pickedFinishDate = null;
        AppUtils appUtils = new AppUtils();
        if (term.contains(",")) {
            String dateStr = appUtils.changeSimpleDateFormat(term);
            String[] parts = dateStr.split("/");
            pickedFinishDate = LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        return pickedFinishDate;
    }


    ArrayList<LocalDate> listOfDates(LocalDate pickedStartDate, String pickedDaysOfWeek, String howManyWeeks) {

        ArrayList<LocalDate> listOfDates = new ArrayList<>();

        if (pickedDaysOfWeek.contains("mon")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.MONDAY, howManyWeeks));
        }

        if (pickedDaysOfWeek.contains("tue")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.TUESDAY, howManyWeeks));
        }

        if (pickedDaysOfWeek.contains("wed")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.WEDNESDAY, howManyWeeks));
        }

        if (pickedDaysOfWeek.contains("thr")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.THURSDAY, howManyWeeks));
        }

        if (pickedDaysOfWeek.contains("fri")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.FRIDAY, howManyWeeks));
        }

        if (pickedDaysOfWeek.contains("sat")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.SATURDAY, howManyWeeks));
        }
        if (pickedDaysOfWeek.contains("sun")) {
            listOfDates.add(dateAccordingToChoosenDayOfTheWeek(pickedStartDate, DayOfWeek.SUNDAY, howManyWeeks));
        }
        return listOfDates;
    }

}
