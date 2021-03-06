package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.TemporalAdjusters.firstInMonth;

public class DeleteCyclicalEvent {

    private boolean hasFinishDate;
    private boolean isFrequencyDays;
    private boolean isFrequencyWeeks;
    private boolean isFrequencyMonths;
    private DisplayCyclicalEventsInTheCalendar displayCyclicalEventsInTheCalendar;

    public void deleteCyclicalEventFromPickedDay(String aimContent, EventsDao eventsDao) {

        displayCyclicalEventsInTheCalendar = new DisplayCyclicalEventsInTheCalendar();

        List<Event> cyclicalEvents = eventsDao.findAllByEventKindAndName(aimContent, 0);
        if (cyclicalEvents != null && !cyclicalEvents.isEmpty()) {
            Event cyclicalEventToDelete = null;

            for (Event cyclicalEvent : cyclicalEvents) {

                checkIfThereIsCyclicalEventFinishDate(cyclicalEvent);

                if (hasFinishDate) {
                    // this event could be deleted before and
                    // there could be many events with this name
                    cyclicalEventToDelete = findTheRightEventToDelete(cyclicalEvent);
                    if (cyclicalEventToDelete != null) {
                        break;
                    }
                } else {
                    // if it doesn't have a finish date it means that
                    // it's the first time the event is deleted and there is only one event
                    // on the list
                    cyclicalEventToDelete = cyclicalEvent;
                    break;
                }

            }

            if (cyclicalEventToDelete == null) {
                cyclicalEventToDelete = cyclicalEvents.get(0);

            }

            assert cyclicalEventToDelete != null;
            String frequency = cyclicalEventToDelete.getFrequency();

            if (frequency.substring(0, 6).equals("0-0-0-")) {
                isFrequencyDays = true;
            } else if (!frequency.substring(0, 6).equals("0-0-0-")
                    && frequency.substring(0, 4).equals("0-0-")) {
                isFrequencyWeeks = true;
            } else if (!frequency.substring(0, 6).equals("0-0-0-")
                    && !frequency.substring(0, 4).equals("0-0-")
                    && frequency.substring(0, 2).equals("0-")) {
                isFrequencyMonths = true;
            }

            deleteEvent(frequency, eventsDao, aimContent, cyclicalEventToDelete);

        }
    }


    private void insertCyclicalEventWithNewStartDate(EventsDao eventsDao, String aimContent, Event cyclicalEventToDelete, String term, String frequency, LocalDate newFinishDay) {

        String[] parts = cyclicalEventToDelete.getFrequency().split("-");
        String pickedDaysOfAWeek = parts[3];

        String id = UtilsEvents.createEventId(cyclicalEventToDelete.getPickedDay());

        int searchedFrequency = findFrequency(frequency);

        //add new event with new startDate
        String newStartDate;
        if (isFrequencyDays) {
            newStartDate = newFinishDay.plusDays(searchedFrequency).toString();

        } else if (isFrequencyWeeks) {
            ArrayList<LocalDate> listOfDaysOfWeek = UpcomingCyclicalEvent.listOfDates(newFinishDay, pickedDaysOfAWeek, String.valueOf(searchedFrequency));
            if (!listOfDaysOfWeek.isEmpty()) {
                Collections.sort(listOfDaysOfWeek);
            }

            newStartDate = listOfDaysOfWeek.get(0).toString();

        } else if (isFrequencyMonths) {
            if (frequency.contains("*months")) {
                newStartDate = newFinishDay.plusMonths(searchedFrequency).toString();

            } else {
                DayOfWeek dayOfWeek = newFinishDay.getDayOfWeek();
                LocalDate firstOcurranceDayOfWeek = newFinishDay.plusMonths(searchedFrequency).with(firstInMonth(dayOfWeek));
                newStartDate = firstOcurranceDayOfWeek.plusWeeks(displayCyclicalEventsInTheCalendar.findDayOfWeekOccurence((double) newFinishDay.getDayOfMonth()) - 1).toString();
            }
        } else {
            newStartDate = newFinishDay.plusYears(searchedFrequency).toString();
        }

        LocalDate eventFinishDate;
        if (cyclicalEventToDelete.getTerm().contains(",")) {
            eventFinishDate = DateUtils.changeLongMonthDateFormatToLocalDate(cyclicalEventToDelete.getTerm());
        } else {
            eventFinishDate = null;
        }

        if (eventFinishDate == null || DateUtils.refactorStringIntoDate(newStartDate).isBefore(eventFinishDate) ||
                DateUtils.refactorStringIntoDate(newStartDate).isEqual(eventFinishDate)) {
            eventsDao.insert(new Event(-56, aimContent, cyclicalEventToDelete.getSchedule(), cyclicalEventToDelete.getAlarm(), cyclicalEventToDelete.getEvent_length(), newStartDate, 0, cyclicalEventToDelete.getFrequency(), term, id));
        }

    }


    private void checkIfThereIsCyclicalEventFinishDate(Event cyclicalEvent) {
        hasFinishDate = cyclicalEvent.getTerm().contains(",");
    }


    private Event findTheRightEventToDelete(Event cyclicalEvent) {

        LocalDate cyclicalEventFinishDate = DateUtils.changeLongMonthDateFormatToLocalDate(cyclicalEvent.getTerm());
        LocalDate pickedDate = DateUtils.refactorStringIntoDate(CalendarFragment.pickedDay);


        Event cyclicalEventToDelete;
        // if picked date is before finish date it means that it's the right event
        if (cyclicalEventFinishDate.isAfter(pickedDate) || cyclicalEventFinishDate.isEqual(pickedDate)) {
            cyclicalEventToDelete = cyclicalEvent;
        } else {
            cyclicalEventToDelete = null;
        }
        return cyclicalEventToDelete;
    }


    private void updateOldCyclicalEventWithNewFinishDate(LocalDate newFinishDay, EventsDao eventsDao, Event cyclicalEventToDelete) {

        String newFinishDate = newFinishDay.minusDays(1).format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));

        if (CalendarFragment.pickedDay.equals(cyclicalEventToDelete.getPickedDay())) {
            eventsDao.deleteByPickedDateKindAndName(CalendarFragment.pickedDay, 0, cyclicalEventToDelete.getEvent_name());
        } else {
            cyclicalEventToDelete.setTerm(newFinishDate);
            eventsDao.update(cyclicalEventToDelete);
        }

    }


    private void deleteEvent(String frequency, EventsDao eventsDao, String aimContent, Event cyclicalEventToDelete) {

        String term = cyclicalEventToDelete.getTerm();
        LocalDate newFinishDay = DateUtils.refactorStringIntoDate(CalendarFragment.pickedDay);
        LocalDate cyclicalEventStartDate = DateUtils.refactorStringIntoDate(cyclicalEventToDelete.getPickedDay());

        int searchedFrequency = findFrequency(frequency);

        if (term.contains("on_and_on")) {

            insertCyclicalEventWithNewStartDate(eventsDao, aimContent, cyclicalEventToDelete, term, frequency, newFinishDay);

            updateOldCyclicalEventWithNewFinishDate(newFinishDay, eventsDao, cyclicalEventToDelete);

            //if there is a finish date
        } else if (term.contains(",")) {

            insertCyclicalEventWithNewStartDate(eventsDao, aimContent, cyclicalEventToDelete, term, frequency, newFinishDay);

            updateOldCyclicalEventWithNewFinishDate(newFinishDay, eventsDao, cyclicalEventToDelete);

            // if there is a number of repeats
        } else {

            int howManyTimesRepeatEvent = Integer.parseInt(term);
            long newHowManyTimesRepeatEvent = findHowManyTimesRepeat(howManyTimesRepeatEvent, cyclicalEventStartDate, newFinishDay, searchedFrequency);

            insertCyclicalEventWithNewStartDate(eventsDao, aimContent, cyclicalEventToDelete, String.valueOf(newHowManyTimesRepeatEvent), frequency, newFinishDay);

            updateOldCyclicalEventWithNewFinishDate(newFinishDay, eventsDao, cyclicalEventToDelete);
        }

    }


    private int findFrequency(String frequency) {

        int searchedFrequency;
        if (isFrequencyDays) {
            searchedFrequency = Integer.parseInt(frequency.substring(6));

        } else if (isFrequencyWeeks) {
            searchedFrequency = Integer.parseInt(frequency.substring(4, 5));
        } else if (isFrequencyMonths) {
            searchedFrequency = Integer.parseInt(frequency.substring(2, 3));
        } else {
            searchedFrequency = Integer.parseInt(frequency.substring(0, 1));
        }
        return searchedFrequency;
    }

    private long findHowManyTimesRepeat(int howManyTimesRepeatEvent, LocalDate cyclicalEventStartDate, LocalDate newFinishDay, int searchedFrequency) {

        long newHowManyTimesRepeatEvent;
        if (isFrequencyDays) {
            newHowManyTimesRepeatEvent = howManyTimesRepeatEvent - ((DAYS.between(cyclicalEventStartDate, newFinishDay) - 1) / searchedFrequency);
        } else if (isFrequencyWeeks) {
            newHowManyTimesRepeatEvent = howManyTimesRepeatEvent - ((WEEKS.between(cyclicalEventStartDate, newFinishDay) - 1) / searchedFrequency);
        } else if (isFrequencyMonths) {
            newHowManyTimesRepeatEvent = howManyTimesRepeatEvent - ((MONTHS.between(cyclicalEventStartDate, newFinishDay) - 1) / searchedFrequency);
        } else {
            newHowManyTimesRepeatEvent = howManyTimesRepeatEvent - ((YEARS.between(cyclicalEventStartDate, newFinishDay) - 1) / searchedFrequency);
        }

        return newHowManyTimesRepeatEvent;
    }
}
