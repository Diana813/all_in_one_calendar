package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "shift", indices = {@Index(value = "shift_name", unique = true)})
public class Shift {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "shift_name")
    private String shift_name;
    @ColumnInfo(name = "schedule")
    private String schedule;
    @ColumnInfo(name = "alarm")
    private String alarm;

    public Shift(int id, String shift_name, String schedule, String alarm) {
        this.id = id;
        this.shift_name = shift_name;
        this.schedule = schedule;
        this.alarm = alarm;
    }

    public int getId() {
        return id;
    }

    public String getShift_name() {
        return shift_name;
    }

    public String getAlarm() {
        return alarm;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setId(int newId){
        this.id = newId;
    }
    public void setShift_name(String new_shift_name){
       this.shift_name = new_shift_name;
    }
    public void setSchedule(String new_schedule){
        this.schedule = new_schedule;
    }
    public void setAlarm(String new_alarm){
        this.alarm = new_alarm;
    }
}
