package com.dianaszczepankowska.AllInOneCalendar.android.forGirls;

import java.time.LocalDate;

public class Periods {


    private LocalDate periodStart;
    private int periodLength;
    private int cycleLength;


    Periods(LocalDate periodStart, int periodLength, int cycleLength) {
        this.periodStart = periodStart;
        this.periodLength = periodLength;
        this.cycleLength = cycleLength;
    }


    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public int getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(int periodLength) {
        this.periodLength = periodLength;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }
}
