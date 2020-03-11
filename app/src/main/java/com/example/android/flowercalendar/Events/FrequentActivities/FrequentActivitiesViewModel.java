package com.example.android.flowercalendar.Events.FrequentActivities;

import android.app.Application;

import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FrequentActivitiesViewModel extends AndroidViewModel {
    private EventsDao eventsDao;
    private LiveData<List<Event>> eventsLiveData;

    public FrequentActivitiesViewModel(@NonNull Application application) {
        super(application);
        eventsDao = CalendarDatabase.getDatabase(application).eventsDao();
        eventsLiveData = eventsDao.sortByOrder("");
    }

    public LiveData<List<Event>> getEventsList() {
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
