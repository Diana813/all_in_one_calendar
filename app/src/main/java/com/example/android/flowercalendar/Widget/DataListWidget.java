package com.example.android.flowercalendar.Widget;

import java.time.LocalDate;

public class DataListWidget {

    private int dayNumber;
    private String shiftNumber;
    private String numberOfEvents;
    private LocalDate periodStartDate;
    private LocalDate periodFinishDate;


    DataListWidget(int dayNumber, String shiftNumber, String numberOfEvents, LocalDate periodStartDate, LocalDate periodFinishDate) {
        this.dayNumber = dayNumber;
        this.shiftNumber = shiftNumber;
        this.numberOfEvents = numberOfEvents;
        this.periodStartDate = periodStartDate;
        this.periodFinishDate = periodFinishDate;
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

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    LocalDate getPeriodFinishDate() {
        return periodFinishDate;
    }

    public void setPeriodFinishDate(LocalDate periodFinishDate) {
        this.periodFinishDate = periodFinishDate;
    }
}
