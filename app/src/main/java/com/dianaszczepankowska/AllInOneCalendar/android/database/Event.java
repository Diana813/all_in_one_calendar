package com.dianaszczepankowska.AllInOneCalendar.android.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "event", indices = {@Index(value = "event_name")})
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "event_name")
    private String event_name;
    @ColumnInfo(name = "schedule")
    private String schedule;
    @ColumnInfo(name = "alarm")
    private String alarm;
    @ColumnInfo(name = "event_length")
    private int event_length;
    @ColumnInfo(name = "position")
    private int position;
    @ColumnInfo(name = "picked_day")
    private String pickedDay;
    @ColumnInfo(name = "eventKind")
    private int eventKind;
    @ColumnInfo(name = "frequency")
    private String frequency;
    @ColumnInfo(name = "term")
    private String term;

    public Event(int position, String event_name, String schedule, String alarm, int event_length, String pickedDay, int eventKind, String frequency, String term) {
        this.position = position;
        this.event_name = event_name;
        this.schedule = schedule;
        this.alarm = alarm;
        this.event_length = event_length;
        this.pickedDay = pickedDay;
        this.eventKind = eventKind;
        this.frequency = frequency;
        this.term = term;
    }

    public int getId() {
        return id;
    }

    public String getAlarm() {
        return alarm;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setSchedule(String new_schedule) {
        this.schedule = new_schedule;
    }

    public void setAlarm(String new_alarm) {
        this.alarm = new_alarm;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getEvent_length() {
        return event_length;
    }

    public void setEvent_length(int event_length) {
        this.event_length = event_length;
    }

    public String getPickedDay() {
        return pickedDay;
    }

    public void setPickedDay(String pickedDay) {
        this.pickedDay = pickedDay;
    }

    public int getEventKind() {
        return eventKind;
    }

    public void setEventKind(int eventKind) {
        this.eventKind = eventKind;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

