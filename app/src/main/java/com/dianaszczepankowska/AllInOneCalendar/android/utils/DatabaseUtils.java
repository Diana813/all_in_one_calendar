package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePathDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter.getContext;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_OPEN_NOTIFICATION_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsEditor.newShiftLength;
import static com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsFragment.newId;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

public class DatabaseUtils {


    public static void setConfirmButton(ImageButton confirm, final PlansRecyclerViewAdapter adapter, final TextView aim, final int i, String pickedDay, String frequency, String schedule, int eventKind) {

        confirm.setOnClickListener(v -> {
            saveDataPersonalGrowth(adapter, aim, i, pickedDay);
            adapter.deleteFromDatabase();
            adapter.setAimIndexInDB();
            if (pickedDay != null) {
                UtilsEvents.saveDataEvents(aim, pickedDay, null, frequency, schedule, eventKind, UtilsEvents.createEventId(pickedDay));
            }
            aim.setText("");
        });

    }

    public static void savePlansData(final PlansRecyclerViewAdapter adapter, final TextView aim, final int i, String pickedDay) {
        saveDataPersonalGrowth(adapter, aim, i, pickedDay);
        adapter.deleteFromDatabase();
        adapter.setAimIndexInDB();

    }

    public static void savePlansData(final PlansRecyclerViewAdapter adapter, String aim, final int i, String pickedDay) {
        saveDataPersonalGrowth(adapter, aim, i, pickedDay);
        adapter.deleteFromDatabase();
        adapter.setAimIndexInDB();
    }


    private static void saveDataPersonalGrowth(PlansRecyclerViewAdapter plansRecyclerViewAdapter, TextView aim, int i, String date) {

        String aimTextString = aim.getText().toString();
        int index = plansRecyclerViewAdapter.getItemCount();

        LocalDate now = DateUtils.refactorStringIntoDate(date);

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i, String.valueOf(index), aimTextString, 0, String.valueOf(now)));

    }

    private static void saveDataPersonalGrowth(PlansRecyclerViewAdapter plansRecyclerViewAdapter, String aim, int i, String date) {

        int index = plansRecyclerViewAdapter.getItemCount();

        LocalDate now = DateUtils.refactorStringIntoDate(date);

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i, String.valueOf(index), aim, 0, String.valueOf(now)));

    }

    public static void saveImagePathToDb(String path, Context context) {

        ImagePathDao imagePathDao = getDatabase(context).imagePathDao();
        imagePathDao.insert(new ImagePath(0, path));

    }

    public static void saveShiftData(TextInputEditText shiftNameEditText, TextView shiftStartTextView, TextView alarmTextView, TextInputEditText shiftLengthEditText, Context context, String shift_name_extra) {

        String newShiftName = Objects.requireNonNull(shiftNameEditText.getText()).toString();
        String newShiftStart = shiftStartTextView.getText().toString();
        String newAlarm = alarmTextView.getText().toString();


        try {
            newShiftLength = Integer.parseInt(Objects.requireNonNull(shiftLengthEditText.getText()).toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();

        }

        if (newShiftName.isEmpty() &&
                newAlarm.isEmpty() && newShiftStart.isEmpty()) {
            return;
        }


        ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();

        if (!shift_name_extra.equals("-1")) {

            Shift shiftToUpdate = shiftsDao.findByShiftName(shift_name_extra);
            if (shiftToUpdate != null) {
                CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
                List<CalendarEvents> changedAlarmShiftsList = calendarEventsDao.findByShiftNumber(shiftToUpdate.getShift_name());


                if (shiftToUpdate.getAlarm() != null && !shiftToUpdate.getAlarm().equals(newAlarm) && !shiftToUpdate.getAlarm().equals("")) {

                    String[] parts = shiftToUpdate.getAlarm().split(":");
                    String alarmHour = parts[0];
                    String alarmMinute = parts[1];

                    if (!newAlarm.equals("")) {
                        String[] parts2 = newAlarm.split(":");
                        String newAlarmHour = parts2[0];
                        String newAlarmMinute = parts2[1];

                        if (changedAlarmShiftsList != null) {
                            for (CalendarEvents calendarEvents : changedAlarmShiftsList) {
                                AlarmUtils.deleteAlarmFromAPickedDay(refactorStringIntoDate(calendarEvents.getPickedDate()), alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);

                                AlarmUtils.setAlarmToPickedDay(newAlarmHour, newAlarmMinute, refactorStringIntoDate(calendarEvents.getPickedDate()), context, ACTION_OPEN_ALARM_CLASS, null);
                            }

                        }
                    } else {
                        for (CalendarEvents calendarEvents : changedAlarmShiftsList) {
                            AlarmUtils.deleteAlarmFromAPickedDay(refactorStringIntoDate(calendarEvents.getPickedDate()), alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);

                        }
                    }
                } else if (shiftToUpdate.getAlarm() != null && shiftToUpdate.getAlarm().equals("") && !newAlarm.equals("")) {
                    String[] parts2 = newAlarm.split(":");
                    String newAlarmHour = parts2[0];
                    String newAlarmMinute = parts2[1];
                    for (CalendarEvents calendarEvents : changedAlarmShiftsList) {
                        AlarmUtils.setAlarmToPickedDay(newAlarmHour, newAlarmMinute, refactorStringIntoDate(calendarEvents.getPickedDate()), context, ACTION_OPEN_ALARM_CLASS, null);
                    }

                }

                if ((!shiftToUpdate.getShift_name().equals(newShiftName)) ||
                        (!shiftToUpdate.getSchedule().equals(newShiftStart)) ||
                        (!shiftToUpdate.getAlarm().equals(newAlarm)) ||
                        (shiftToUpdate.getShift_length() != newShiftLength)) {
                    shiftToUpdate.setShift_name(newShiftName);
                    shiftToUpdate.setSchedule(newShiftStart);
                    shiftToUpdate.setAlarm(newAlarm);
                    shiftToUpdate.setShift_length(newShiftLength);
                    shiftsDao.update(shiftToUpdate);
                }
            }

        } else {

            shiftsDao.insert(new Shift(newId, newShiftName, newShiftStart, newAlarm, newShiftLength));

        }

    }


    public static void saveWorkAndEventData(TextInputEditText nameEditText, TextView startTextView, TextView alarmTextView, TextInputEditText lengthEditText, Context context, String name_extra, int eventKind, int newWorkLength) {

        String newWorkName = Objects.requireNonNull(nameEditText.getText()).toString();
        String newWorkStart = startTextView.getText().toString();
        String newAlarm = alarmTextView.getText().toString();


        try {
            newWorkLength = Integer.parseInt(Objects.requireNonNull(lengthEditText.getText()).toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();

        }

        if (newWorkName.isEmpty() &&
                newAlarm.isEmpty() && newWorkStart.isEmpty()) {
            return;
        }


        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (!name_extra.equals("-1")) {

            Event eventToUpdate = eventsDao.findByEventNameKindAndPickedDay(name_extra, currentDate, eventKind);
            if (eventToUpdate != null) {
                if (eventToUpdate.getAlarm() != null) {
                    AlarmUtils.deleteAlarmFromAPickedDay(refactorStringIntoDate(currentDate), AlarmUtils.getAlarmHour(eventToUpdate), AlarmUtils.getAlarmMinute(eventToUpdate), context, ACTION_OPEN_NOTIFICATION_CLASS);
                }
                if ((!eventToUpdate.getEvent_name().equals(newWorkName)) ||
                        (!eventToUpdate.getSchedule().equals(newWorkStart)) ||
                        (!eventToUpdate.getAlarm().equals(newAlarm)) ||
                        (eventToUpdate.getEvent_length() != newWorkLength)) {
                    eventToUpdate.setEvent_name(newWorkName);
                    eventToUpdate.setSchedule(newWorkStart);
                    eventToUpdate.setAlarm(newAlarm);
                    eventToUpdate.setEvent_length(newWorkLength);
                    eventsDao.update(eventToUpdate);
                    AlarmUtils.setAlarmToPickedDay(AlarmUtils.getAlarmHour(eventToUpdate), AlarmUtils.getAlarmMinute(eventToUpdate), refactorStringIntoDate(currentDate), context, ACTION_OPEN_NOTIFICATION_CLASS, eventToUpdate.getEvent_name());
                }
            }


        } else {

            eventsDao.insert(new Event(newId, newWorkName, newWorkStart, newAlarm, newWorkLength, currentDate, eventKind, null, null, UtilsEvents.createEventId(currentDate)));
            AlarmUtils.setAlarmToPickedDay(AlarmUtils.getAlarmHour(newAlarm), AlarmUtils.getAlarmMinute(newAlarm), refactorStringIntoDate(currentDate), context, ACTION_OPEN_NOTIFICATION_CLASS, newWorkName);

        }

    }


}
