package com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.CalendarProviderMethods;
import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.EventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.ToDoList;
import com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities.FrequentActivities;
import com.google.common.eventbus.EventBus;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter.getContext;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

public class UtilsEvents {

    public static void setConfirmButtonEvents(Context context, View confirm, final EventsListAdapter adapter, final TextView textView, final String pickedDay, final String newEvent, String frequency, String schedule, int eventKind) {
        confirm.setOnClickListener(v -> {
            UserDataDao userDataDao = getDatabase(context).userDataDao();
            UserData owner = userDataDao.getAllUsers().get(0);
            String id = createEventId(pickedDay);
            saveDataEvents(textView, pickedDay, newEvent, frequency, schedule, eventKind, id);
            adapter.deleteFromDatabase(null);
            adapter.setIndexInDB();
            CalendarProviderMethods.addEventToGoogleCalendar(context, refactorStringIntoDate(pickedDay), schedule, textView.getText().toString(), Locale.getDefault().getCountry(), 0, id, owner.getEmail());
//            CalendarProviderMethods.addAttendee(id, context, "Gekrepten", "gekrepten.szczepankowska@gmail.com");
            textView.setText("");
        });

    }

    public static void setConfirmButtonEvents(Context context, final EventsListAdapter adapter, final TextView textView, final String pickedDay, final String newEvent, String frequency, String schedule, int eventKind) {
        UserDataDao userDataDao = getDatabase(context).userDataDao();
        UserData owner = userDataDao.getAllUsers().get(0);
        String id = createEventId(pickedDay);
        saveDataEvents(textView, pickedDay, newEvent, frequency, schedule, eventKind, id);
        adapter.deleteFromDatabase(null);
        adapter.setIndexInDB();
        CalendarProviderMethods.addEventToGoogleCalendar(context, refactorStringIntoDate(pickedDay), schedule, textView.getText().toString(), Locale.getDefault().getCountry(), 0, id, owner.getEmail());
//            CalendarProviderMethods.addAttendee(id, context, "Gekrepten", "gekrepten.szczepankowska@gmail.com");
    }


    public static void saveDataEvents(TextView plan, String pickedDay, String newEvent, String frequency, String schedule, int eventKind, String id) {

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

        Event cyclicalEvent = eventsDao.findByEventNameKindAndPickedDay(eventTextString, pickedDay, EventKind.CYCLICAL_EVENTS.getIntValue());
        Event cyclicalEventsScheduled;

        if (cyclicalEvent != null) {
            if (cyclicalEvent.getSchedule() != null && !cyclicalEvent.getSchedule().equals("")) {
                cyclicalEventsScheduled = cyclicalEvent;
                if (!cyclicalEventsScheduled.getEvent_name().equals(eventTextString) ||
                        !cyclicalEventsScheduled.getPickedDay().equals(pickedDay) || cyclicalEventsScheduled.getEventKind() != eventKind || !cyclicalEventsScheduled.getSchedule().equals(schedule)) {
                    cyclicalEventsScheduled.setEvent_name(eventTextString);
                    cyclicalEventsScheduled.setPickedDay(pickedDay);
                    cyclicalEventsScheduled.setEventKind(eventKind);
                    cyclicalEventsScheduled.setSchedule(schedule);
                    eventsDao.update(cyclicalEventsScheduled);
                }
            } else if (!cyclicalEvent.getEvent_name().equals(eventTextString) ||
                    !cyclicalEvent.getPickedDay().equals(pickedDay) || cyclicalEvent.getEventKind() != eventKind) {
                cyclicalEvent.setEvent_name(eventTextString);
                cyclicalEvent.setPickedDay(pickedDay);
                cyclicalEvent.setEventKind(eventKind);
                eventsDao.update(cyclicalEvent);
            }

        } else {
            eventsDao.insert(new Event(index, eventTextString, schedule, null, 0, pickedDay, eventKind, frequency, "0", id));
        }

    }

    public static void addNotification(View view, Context context) {
        NotificationPopUp notificationPopUp = new NotificationPopUp();
        notificationPopUp.showPopUp(view, context);
    }

    public static String createEventId(String pickedDay) {
        Calendar eventTime = Calendar.getInstance();
        if (pickedDay != null && !pickedDay.equals("")) {
            LocalDate eventStartDate = refactorStringIntoDate(pickedDay);
            eventTime.set(eventStartDate.getYear(), eventStartDate.getMonthValue() - 1, eventStartDate.getDayOfMonth());
            return eventTime.getTimeInMillis() + "300";
        }
        return null;
    }
}
