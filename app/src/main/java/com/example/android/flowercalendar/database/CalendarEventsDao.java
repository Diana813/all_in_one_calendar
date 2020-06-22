package com.example.android.flowercalendar.database;

import java.util.List;

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

    @Query("SELECT * FROM calendar_events")
    List<CalendarEvents> findAllEvents();

    @Query("UPDATE calendar_events SET shiftNumber = '' WHERE pickedDate = :pickedDate")
    void deleteBypickedDate(String pickedDate);

    @Query("SELECT * FROM calendar_events WHERE shiftNumber = :shiftNumber")
    List<CalendarEvents> findByShiftNumber(String shiftNumber);

    @Query("DELETE FROM calendar_events")
    void deleteAll();

    @Query("UPDATE calendar_events SET shiftNumber = '' WHERE currentMonth = :currentMonth")
    void deleteAllShifts(String currentMonth);

    @Insert()
    void insert(CalendarEvents calendarEvent);
}


