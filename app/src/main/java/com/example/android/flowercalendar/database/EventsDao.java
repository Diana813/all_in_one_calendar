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

    @Query("SELECT * FROM event WHERE event_name = :eventName AND picked_day = :pickedDay AND eventKind = :eventKind")
    Event findByEventNameKindAndPickedDay(String eventName, String pickedDay, int eventKind);

    @Query("SELECT * FROM event WHERE event_name = :eventName AND eventKind = :eventKind")
    Event findByEventKindAndName(String eventName, int eventKind);

    @Query("SELECT * FROM event WHERE event_name = :eventName")
    List<Event> findByEventNameList(String eventName);

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

    @Query("DELETE FROM event WHERE event_name = :eventName")
    void deleteByEventName(String eventName);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay AND (eventKind = :eventKind OR eventKind = :eventKind2)")
    List<Event> findByEventDate(String pickedDay, int eventKind, int eventKind2);

    @Query("SELECT * FROM event WHERE eventKind = :eventKind ORDER BY event_name ASC, date(picked_day) ASC")
    LiveData<List<Event>> findByEventKind(int eventKind);

    @Query("SELECT * FROM event WHERE eventKind = :eventKind ORDER BY event_name ASC")
    List<Event> findByKind(int eventKind);

    @Query("DELETE FROM event WHERE (event_name = :event_name AND position = :position AND picked_day = :picked_day AND eventKind = :eventKind)")
    void deleteEvents(int position, String picked_day, String event_name, int eventKind);

    @Query("SELECT * FROM event WHERE (picked_day = :pickedDay AND eventKind = :eventKind AND schedule = :schedule) ORDER BY position ASC")
    LiveData<List<Event>> sortByOrder(String pickedDay, int eventKind, String schedule);

    @Query("DELETE FROM event WHERE picked_day = :pickedDay AND eventKind = :eventKind")
    void deleteByPickedDate(String pickedDay, int eventKind);

    @Query("SELECT * FROM event WHERE picked_day = :pickedDay AND eventKind = :eventKind ORDER BY position ASC")
    List<Event> sortByPickedDay(String pickedDay, int eventKind);

    @Query("SELECT * FROM event WHERE event_name = :eventName AND eventKind = :eventKind")
    List<Event> findAllByEventKindAndName(String eventName, int eventKind);

    @Query("DELETE FROM event WHERE picked_day = :pickedDay AND eventKind = :eventKind AND event_name = :event_name")
    void deleteByPickedDateKindAndName(String pickedDay, int eventKind, String event_name);
}

