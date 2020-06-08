package com.example.android.flowercalendar.calendar;

import java.time.LocalDate;

public class CalendarViews {

    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    private int mDayNumber;
    private String mShiftNumber;
    private String mEventName;
    private LocalDate mCalendarFill;
    private LocalDate mPeriodStart;
    private LocalDate mPeriodEnd;
    private LocalDate mHeaderDate;
    private int mColorSettings;

    CalendarViews(int colorSettings, LocalDate calendarFill, LocalDate headerDate, int dayNumber, String shiftNumber, String eventName) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = eventName;
        mCalendarFill = calendarFill;
        mHeaderDate = headerDate;
        mColorSettings = colorSettings;
    }

    CalendarViews(int colorSettings, LocalDate calendarFill, LocalDate headerDate, LocalDate periodStart, LocalDate periodEnd, int dayNumber, String shiftNumber, String eventName, int imageResourceId) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = eventName;
        mImageResourceId = imageResourceId;
        mCalendarFill = calendarFill;
        mPeriodStart = periodStart;
        mPeriodEnd = periodEnd;
        mHeaderDate = headerDate;
        mColorSettings = colorSettings;
    }


    LocalDate getmCalendarFill() {
        return mCalendarFill;
    }

    int getmDayNumber() {
        return mDayNumber;
    }

    String getmShiftNumber() {
        return mShiftNumber;
    }

    String getmEventName() {
        return mEventName;
    }

    int getmImageResourceId() {
        return mImageResourceId;
    }

    public LocalDate getmPeriodStart() {
        return mPeriodStart;
    }

    public LocalDate getmPeriodEnd() {
        return mPeriodEnd;
    }

    LocalDate getmHeaderDate() {
        return mHeaderDate;
    }

    int getmColorSettings() {
        return mColorSettings;
    }

    boolean hasPeriod() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public void setmColorSettings(int colorSettings) {
        mColorSettings = colorSettings;
    }
}
