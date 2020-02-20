package com.example.android.flowercalendar.Events;

public class Hours {

    private String hour;
    private String eventName;

    Hours(String hour, String eventName) {
        this.hour = hour;
        this.eventName = eventName;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
