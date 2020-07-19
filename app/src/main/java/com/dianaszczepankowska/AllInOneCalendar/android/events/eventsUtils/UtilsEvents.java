package com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils;

import android.widget.ImageButton;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.ToDoList;
import com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities.FrequentActivities;

import static com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BigPlanAdapter.getContext;

public class UtilsEvents {

    public static void setConfirmButtonEvents(ImageButton confirm, final EventsListAdapter adapter, final TextView textView, final String pickedDay, final String newEvent, String frequency, String schedule, int eventKind) {
        confirm.setOnClickListener(v -> {
            saveDataEvents(textView, pickedDay, newEvent, frequency, schedule, eventKind);
            adapter.deleteFromDatabase(null);
            adapter.setIndexInDB();
            textView.setText("");
        });

    }


    public static void saveDataEvents(TextView plan, String pickedDay, String newEvent, String frequency, String schedule, int eventKind) {

        String eventTextString;
        if (newEvent != null) {
            eventTextString = newEvent;
        } else {
            eventTextString = plan.getText().toString();
        }


        int index;
        if (pickedDay.equals("")) {
            index = FrequentActivities.getFreqActSize();
        } else {
            index = ToDoList.positionOfTheNextEventOnTheList();
        }


        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        if (!schedule.equals("")) {
            Event eventToUpdate = eventsDao.findBySchedule(pickedDay, schedule);

            if (eventToUpdate != null) {
                if (eventToUpdate.getSchedule().equals(schedule) &&
                        !eventToUpdate.getEvent_name().equals(eventTextString)) {
                    eventToUpdate.setEvent_name(eventTextString);
                    eventsDao.update(eventToUpdate);
                }

            }
        }

        Event cyclicalEvent = eventsDao.findByEventNameKindAndPickedDay(eventTextString, pickedDay, 1);
        Event cyclicalEventsScheduled = eventsDao.findByEventNameKindAndPickedDay(eventTextString, pickedDay, 3);
        if (cyclicalEvent != null) {
            if (!cyclicalEvent.getEvent_name().equals(eventTextString) ||
                    !cyclicalEvent.getPickedDay().equals(pickedDay) || cyclicalEvent.getEventKind() != eventKind) {
                cyclicalEvent.setEvent_name(eventTextString);
                cyclicalEvent.setPickedDay(pickedDay);
                cyclicalEvent.setEventKind(eventKind);
                eventsDao.update(cyclicalEvent);
            }

        } else if (cyclicalEventsScheduled != null) {
            if (!cyclicalEventsScheduled.getEvent_name().equals(eventTextString) ||
                    !cyclicalEventsScheduled.getPickedDay().equals(pickedDay) || cyclicalEventsScheduled.getEventKind() != eventKind || !cyclicalEventsScheduled.getSchedule().equals(schedule)) {
                cyclicalEventsScheduled.setEvent_name(eventTextString);
                cyclicalEventsScheduled.setPickedDay(pickedDay);
                cyclicalEventsScheduled.setEventKind(eventKind);
                cyclicalEventsScheduled.setSchedule(schedule);
                eventsDao.update(cyclicalEventsScheduled);
            }
        } else {

            eventsDao.insert(new Event(index, eventTextString, schedule, null, 0, pickedDay, eventKind, frequency, "0"));
        }

    }
}
