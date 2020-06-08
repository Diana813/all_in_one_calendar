package com.example.android.flowercalendar.events;

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

    public void setAimContent(String aimContent) {
        this.aimContent = aimContent;
    }

    public int getAimNumber() {
        return aimNumber;
    }

    public void setAimNumber(int aimNumber) {
        this.aimNumber = aimNumber;
    }
}
