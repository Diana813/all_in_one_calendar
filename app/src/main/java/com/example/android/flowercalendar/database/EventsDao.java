package com.example.android.flowercalendar.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EventsDao {

    @Query("SELECT * FROM event WHERE event_name = :eventName")
    Event findByEventName(String eventName);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay AND schedule = :schedule")
    Event findBySchedule(String pickedDay, String schedule);

    @Query("DELETE FROM event WHERE picked_day = :pickedDay AND schedule = :schedule")
    void deleteBySchedule(String pickedDay, String schedule);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event... events);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAllEvents();

    @Query("DELETE FROM event WHERE event_name = :eventName")
    void deleteByEventName(String eventName);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay")
    List<Event> findByEventDate(String pickedDay);

    @Query("SELECT * FROM event WHERE eventKind = :eventKind ORDER BY event_name ASC")
    LiveData<List<Event>> findByEventKind(int eventKind);

    @Query("DELETE FROM event WHERE (event_name = :event_name AND position = :position AND picked_day = :picked_day)")
    void deleteEvents(int position, String picked_day, String event_name);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay AND eventKind = :eventKind ORDER BY position ASC")
    LiveData<List<Event>> sortByOrder(String pickedDay, int eventKind);

    @Query("DELETE FROM event WHERE picked_day = :pickedDay")
    void deleteByPickedDate(String pickedDay);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay AND eventKind = :eventKind ORDER BY position ASC")
    List<Event> sortByPickedDay(String pickedDay, int eventKind);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay")
    LiveData<List<Event>> findByEventDay(String pickedDay);

}



