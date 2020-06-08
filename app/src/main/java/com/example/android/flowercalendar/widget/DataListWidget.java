package com.example.android.flowercalendar.widget;

public class DataListWidget {

    private int dayNumber;
    private String shiftNumber;
    private String numberOfEvents;
    private boolean hasPeriod;


    DataListWidget(int dayNumber, String shiftNumber, String numberOfEvents, boolean hasPeriod) {
        this.dayNumber = dayNumber;
        this.shiftNumber = shiftNumber;
        this.numberOfEvents = numberOfEvents;
        this.hasPeriod = hasPeriod;
    }

    int getDayNumber() {
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

    String getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(String numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    boolean isHasPeriod() {
        return hasPeriod;
    }

    public void setHasPeriod(boolean hasPeriod) {
        this.hasPeriod = hasPeriod;
    }
}
