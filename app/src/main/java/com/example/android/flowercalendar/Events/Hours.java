package com.example.android.flowercalendar.Events;

public class Hours {

    private String hour;
    private String eventText;

    Hours(String hour, String eventText) {
        this.hour = hour;
        this.eventText = eventText;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
}
