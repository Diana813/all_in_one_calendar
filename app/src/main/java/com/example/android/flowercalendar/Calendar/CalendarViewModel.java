package com.example.android.flowercalendar.Calendar;

import android.app.Application;

import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CalendarViewModel  extends AndroidViewModel {

    private CalendarEventsDao calendarEventsDao;
    private LiveData<List<CalendarEvents>> calendarEventsLiveData;


    public CalendarViewModel(@NonNull Application application) {
        super(application);
        calendarEventsDao = CalendarDatabase.getDatabase(application).calendarEventsDao();
        calendarEventsLiveData = calendarEventsDao.getAllCalendarEvents();
    }

    public LiveData<List<CalendarEvents>> getEventsList() {
        return calendarEventsLiveData;
    }

    public void insert(CalendarEvents... calendarEvents) {
        calendarEventsDao.insert(calendarEvents);
    }

    public void insert(CalendarEvents calendarEvent) {
        calendarEventsDao.insert(calendarEvent);
    }

    public void update(CalendarEvents calendarEvent) {
        calendarEventsDao.update(calendarEvent);
    }

    public void deleteAll() {
        calendarEventsDao.deleteAll();
    }
}
