package com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BackgroundActivity;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EventsViewModel extends AndroidViewModel {
    private EventsDao eventsDao;
    private LiveData<List<Event>> eventsLiveDataToDoList;
    private LiveData<List<Event>> eventsLiveDataForAims;
    private LiveData<List<Event>> workLiveData;
    private LiveData<List<Event>> shiftLiveData;
    private LiveData<List<Event>> eventsLiveData;


    public EventsViewModel(@NonNull Application application) {
        super(application);

        eventsDao = CalendarDatabase.getDatabase(application).eventsDao();
        eventsLiveDataToDoList = eventsDao.sortByOrder(BackgroundActivityExpandedDayView.currentDate, EventKind.EVENTS.getIntValue(), "");
        eventsLiveDataForAims = eventsDao.sortByOrder(String.valueOf(LocalDate.now()), EventKind.EVENTS.getIntValue(), "");
        workLiveData = eventsDao.sortByOrder(BackgroundActivityExpandedDayView.currentDate, EventKind.WORK.getIntValue());
        shiftLiveData = eventsDao.sortByOrder(BackgroundActivityExpandedDayView.currentDate, EventKind.SHIFTS.getIntValue());
        eventsLiveData = eventsDao.sortByOrder(BackgroundActivityExpandedDayView.currentDate, EventKind.EVENTS.getIntValue());
    }

    public LiveData<List<Event>> getEventsList() {
        return eventsLiveDataToDoList;
    }

    public LiveData<List<Event>> getShiftLiveData() {
        return shiftLiveData;
    }

    public LiveData<List<Event>> getEventsListForAims() {
        return eventsLiveDataForAims;
    }

    public LiveData<List<Event>> getWorkLiveData() {
        return workLiveData;
    }

    public LiveData<List<Event>> getEventsLiveData() {
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