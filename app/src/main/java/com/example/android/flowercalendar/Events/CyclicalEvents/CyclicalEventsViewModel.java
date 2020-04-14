package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.app.Application;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CyclicalEventsViewModel extends AndroidViewModel {
    private EventsDao eventsDao;
    private LiveData<List<Event>> eventsLiveData;

    public CyclicalEventsViewModel(@NonNull Application application) {
        super(application);
        eventsDao = CalendarDatabase.getDatabase(application).eventsDao();
        eventsLiveData = eventsDao.findByEventKind(0);

    }

    LiveData<List<Event>> getEventsList() {
        return eventsLiveData;
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
