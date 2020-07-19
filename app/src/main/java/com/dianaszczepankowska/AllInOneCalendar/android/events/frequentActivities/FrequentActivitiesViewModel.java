package com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;

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
        eventsLiveData = eventsDao.sortByOrder("", 2, "");
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
