package com.example.android.flowercalendar.database;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CalendarEventsDao {

    @Insert()
    void insert(CalendarEvents[] calendarEvents);

    @Update()
    void update(CalendarEvents calendarEvents);

    @Query("SELECT * FROM calendar_events WHERE pickedDate = :pickedDate")
    CalendarEvents findBypickedDate(String pickedDate);

    @Query("SELECT * FROM calendar_events WHERE id = (SELECT MAX(ID) FROM calendar_events)")
    CalendarEvents findLastCalendarEvent ();

    @Query("DELETE FROM calendar_events")
    void deleteAll();

    @Query("SELECT * FROM calendar_events")
    LiveData<List<CalendarEvents>> getAllCalendarEvents();

    @Insert()
    void insert(CalendarEvents calendarEvent);
}


