package com.example.android.flowercalendar.database;

import java.time.LocalDate;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendar_events")
public class CalendarEvents {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "shiftNumber")
    private String shiftNumber;

    @ColumnInfo(name = "currentMonth")
    private String currentMonth;

    @ColumnInfo(name = "eventsNumber")
    private String eventsNumber;

    @ColumnInfo(name = "pickedDate")
    private String pickedDate;

    public CalendarEvents(String shiftNumber, String eventsNumber, String pickedDate, String currentMonth) {
        this.shiftNumber = shiftNumber;
        this.eventsNumber = eventsNumber;
        this.pickedDate = pickedDate;
        this.currentMonth = currentMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(String shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    String getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(String pickedDate) {
        this.pickedDate = pickedDate;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getEventsNumber() {
        return eventsNumber;
    }

    public void setEventsNumber(String eventsNamber) {
        this.eventsNumber = eventsNamber;
    }
}
