package com.dianaszczepankowska.AllInOneCalendar.android;

import androidx.annotation.NonNull;

public enum EventKind {
    CYCLICAL_EVENTS("Cyclical_events", 0),
    EVENTS("Events", 1),
    WORK("Work", 2),
    SHIFTS("Shifts", 3);

    private String stringValue;
    private int intValue;

    EventKind(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @NonNull
    @Override
    public String toString() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
