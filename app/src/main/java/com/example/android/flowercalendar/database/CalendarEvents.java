package com.example.android.flowercalendar.database;

import java.time.LocalDate;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendar_events")
public class CalendarEvents {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "colorSettings")
    private int colorSettings;

    @ColumnInfo(name = "calendarFillString")
    private String calendarFillString;

    @ColumnInfo(name = "headerDateString")
    private String headerDateString;

    @ColumnInfo(name = "periodStartString")
    private String periodStartString;

    @ColumnInfo(name = "periodEndStriong")
    private String periodEndString;

    @ColumnInfo(name = "dayNumber")
    private int dayNumber;

    @ColumnInfo(name = "shiftNumber")
    private String shiftNumber;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "imageResourceId")
    private int imageResourceId;

    @ColumnInfo(name = "pickedDate")
    private String pickedDate;

    public CalendarEvents(int colorSettings, String calendarFillString, String headerDateString, String periodStartString, String periodEndString, int dayNumber, String shiftNumber, String eventName, int imageResourceId, String pickedDate) {

        this.colorSettings = colorSettings;
        this.calendarFillString = calendarFillString;
        this.headerDateString = headerDateString;
        this.periodStartString = periodStartString;
        this.periodEndString = periodEndString;
        this.dayNumber = dayNumber;
        this.shiftNumber = shiftNumber;
        this.eventName = eventName;
        this.imageResourceId = imageResourceId;
        this.pickedDate = pickedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getColorSettings() {
        return colorSettings;
    }

    public void setColorSettings(int colorSettings) {
        this.colorSettings = colorSettings;
    }

    public String getCalendarFillString() {
        return calendarFillString;
    }

    public void setCalendarFillString(String calendarFillString) {
        this.calendarFillString = calendarFillString;
    }

    public String getHeaderDateString() {
        return headerDateString;
    }

    public void setHeaderDateString(String headerDateString) {
        this.headerDateString = headerDateString;
    }

    public String getPeriodStartString() {
        return periodStartString;
    }

    public void setPeriodStartString(String periodStartString) {
        this.periodStartString = periodStartString;
    }

    public String getPeriodEndString() {
        return periodEndString;
    }

    public void setPeriodEndString(String periodEndString) {
        this.periodEndString = periodEndString;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(String shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(String pickedDate) {
        this.pickedDate = pickedDate;
    }

}
