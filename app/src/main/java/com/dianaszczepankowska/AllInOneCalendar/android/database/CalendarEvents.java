package com.dianaszczepankowska.AllInOneCalendar.android.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendar_events")
public class CalendarEvents {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "currentMonth")
    private String currentMonth;

    @ColumnInfo(name = "isAlarmOn")
    private boolean isAlarmOn;

    @ColumnInfo(name = "pickedDate")
    private String pickedDate;

    @ColumnInfo(name = "eventsNumber")
    private String eventsNumber;

    @ColumnInfo(name = "shiftNumber")
    private String shiftNumber;

    @ColumnInfo(name = "shiftIdForGoogle")
    private String shiftIdForGoogle;


    public CalendarEvents(String currentMonth, boolean isAlarmOn, String pickedDate, String eventsNumber, String shiftNumber, String shiftIdForGoogle) {
        this.currentMonth = currentMonth;
        this.isAlarmOn = isAlarmOn;
        this.pickedDate = pickedDate;
        this.eventsNumber = eventsNumber;
        this.shiftNumber = shiftNumber;
        this.shiftIdForGoogle = shiftIdForGoogle;
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

    public String getPickedDate() {
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

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        isAlarmOn = alarmOn;
    }

    public String getShiftIdForGoogle() {
        return shiftIdForGoogle;
    }

    public void setShiftIdForGoogle(String shiftIdForGoogle) {
        this.shiftIdForGoogle = shiftIdForGoogle;
    }
}
