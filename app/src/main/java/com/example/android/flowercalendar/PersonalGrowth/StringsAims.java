package com.example.android.flowercalendar.PersonalGrowth;

public class StringsAims {

    private String aimNumber;
    private String aimContent;

    StringsAims(String aimNumber, String aimContent) {
        this.aimNumber = aimNumber;
        this.aimContent = aimContent;
    }

    public String getAimNumber() {
        return aimNumber;
    }

    public void setAimNumber(String aimNumber) {
        this.aimNumber = aimNumber;
    }

    String getAimContent() {
        return aimContent;
    }

    public void setAimContent(String aimContent) {
        this.aimContent = aimContent;
    }
}
