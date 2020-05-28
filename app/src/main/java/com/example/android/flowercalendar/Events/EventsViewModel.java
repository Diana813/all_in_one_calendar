package com.example.android.flowercalendar.Events;

import android.app.Application;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EventsViewModel extends AndroidViewModel {
    private EventsDao eventsDao;
    private LiveData<List<Event>> eventsLiveDataToDoList;
    private LiveData<List<Event>> eventsLiveDataForAims;


    public EventsViewModel(@NonNull Application application) {
        super(application);

        eventsDao = CalendarDatabase.getDatabase(application).eventsDao();
        eventsLiveDataToDoList = eventsDao.sortByOrder(CalendarFragment.pickedDay, 1, "");
        eventsLiveDataForAims = eventsDao.sortByOrder(String.valueOf(LocalDate.now()), 1, "");
    }

    public LiveData<List<Event>> getEventsList() {
        return eventsLiveDataToDoList;
    }

    public LiveData<List<Event>> getEventsListForAims() {
        return eventsLiveDataForAims;
    }


    public void insert(Event... events) {
        eventsDao.insert(events);
    }

    public void insert(Event event) {
        eventsDao.insert(event);
    }

    public void update(Event event) {
        eventsDao.update(event);
    }

    public void deleteAll() {
        eventsDao.deleteAll();
    }

}