package com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils;

public class StringsAims {

    private int aimNumber;
    private String aimContent;
    private String id;

    public StringsAims(int aimNumber, String aimContent, String id) {
        this.aimNumber = aimNumber;
        this.aimContent = aimContent;
        this.id = id;
    }

    public String getAimContent() {
        return aimContent;
    }

    public int getAimNumber() {
        return aimNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
