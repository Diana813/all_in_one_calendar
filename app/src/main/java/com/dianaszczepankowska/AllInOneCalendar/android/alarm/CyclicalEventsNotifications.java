package com.dianaszczepankowska.AllInOneCalendar.android.alarm;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.UpcomingCyclicalEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_OPEN_NOTIFICATION_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_SET_NOTIFICATION;

public class CyclicalEventsNotifications {


    public static void setNotification(Event event, Context context) {

        if (event.getAlarm() == null || event.getAlarm().equals("")) {
            return;
        }
        String[] alarmParts = event.getAlarm().split(":");
        String alarmHour = alarmParts[0];
        String alarmMinutes = alarmParts[1];
        String action = "Open notification class";

        String frequency = event.getFrequency();

        long intervalMilis;
        long startDate = AppUtils.dateStringToMilis(event.getPickedDay());
        String eventName = event.getEvent_name();

        //days
        if (frequency.substring(0, 6).equals("0-0-0-")) {
            int dayFrequency = Integer.parseInt(frequency.substring(6));
            LocalDate upcomingEvent = AppUtils.refactorStringIntoDate(event.getPickedDay()).plusDays(dayFrequency);
            long nextEvent = AppUtils.dateStringToMilis(upcomingEvent.toString());
            intervalMilis = nextEvent - startDate;
            AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            //weeks
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && frequency.substring(0, 4).equals("0-0-")) {

            int weeksFrequency = Integer.parseInt(frequency.substring(4, 5));

            String[] parts = event.getFrequency().split("-");
            String pickedDaysOfWeek = parts[3];

            if (pickedDaysOfWeek.contains("mon")) {
                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.MONDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }

            if (pickedDaysOfWeek.contains("tue")) {

                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.TUESDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);

            }

            if (pickedDaysOfWeek.contains("wed")) {

                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.WEDNESDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }

            if (pickedDaysOfWeek.contains("thr")) {

                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.THURSDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }


            if (pickedDaysOfWeek.contains("fri")) {
                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.FRIDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }

            if (pickedDaysOfWeek.contains("sat")) {
                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.SATURDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }
            if (pickedDaysOfWeek.contains("sun")) {
                LocalDate startDateWeeks = UpcomingCyclicalEvent.dateAccordingToChoosenDayOfTheWeek(AppUtils.refactorStringIntoDate(event.getPickedDay()), DayOfWeek.SUNDAY, String.valueOf(weeksFrequency));
                long start = AppUtils.dateStringToMilis(startDateWeeks.toString());
                long upcomingEvent = AppUtils.dateStringToMilis(startDateWeeks.plusWeeks(weeksFrequency).toString());
                intervalMilis = upcomingEvent - start;
                AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
            }


            //months
        } else if (!frequency.substring(0, 6).equals("0-0-0-")
                && !frequency.substring(0, 4).equals("0-0-")
                && frequency.substring(0, 2).equals("0-")) {

            long intervalMil = Long.parseLong("2592000000");
            AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, ACTION_SET_NOTIFICATION, intervalMil, eventName);

            //years
        } else {
            int yearsFrequency = Integer.parseInt(frequency.substring(0, 1));

            LocalDate upcomingEvent = AppUtils.refactorStringIntoDate(event.getPickedDay()).plusYears(yearsFrequency);
            long nextEvent = AppUtils.dateStringToMilis(upcomingEvent.toString());
            intervalMilis = nextEvent - startDate;
            AlarmUtils.setCyclicalNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, action, intervalMilis, eventName);
        }

    }


    public void deleteNotification(Event event, Context context) {

        if (event == null) {
            return;
        }
        if (event.getAlarm() != null && !event.getAlarm().equals("")) {
            String[] alarmParts = event.getAlarm().split(":");
            String alarmHour = alarmParts[0];
            String alarmMinutes = alarmParts[1];

            AlarmUtils.deleteAlarmFromAPickedDay(AppUtils.refactorStringIntoDate(event.getPickedDay()), alarmHour, alarmMinutes, context, ACTION_OPEN_NOTIFICATION_CLASS);

            AlarmUtils.deleteAlarmFromAPickedDay(AppUtils.refactorStringIntoDate(event.getPickedDay()), alarmHour, alarmMinutes, context, ACTION_SET_NOTIFICATION);
        }
    }

}