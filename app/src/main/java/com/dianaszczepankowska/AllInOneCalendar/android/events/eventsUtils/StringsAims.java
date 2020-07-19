package com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils;

public class StringsAims {

    private int aimNumber;
    private String aimContent;

    public StringsAims(int aimNumber, String aimContent) {
        this.aimNumber = aimNumber;
        this.aimContent = aimContent;
    }

    public String getAimContent() {
        return aimContent;
    }

    public int getAimNumber() {
        return aimNumber;
    }

}
