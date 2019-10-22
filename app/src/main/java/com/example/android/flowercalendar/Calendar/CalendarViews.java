package com.example.android.flowercalendar.Calendar;

import java.time.LocalDate;

class CalendarViews {

    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    private int mDayNumber;
    private int mShiftNumber;
    private String mEventName;
    private LocalDate mCalendarFill;
    private LocalDate mPeriodStart;
    private LocalDate mPeriodEnd;
    private LocalDate mHeaderDate;
    private int mColorSettings;

    public CalendarViews(int colorSettings, LocalDate calendarFill, LocalDate headerDate, int dayNumber, int shiftNumber, String eventName) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = eventName;
        mCalendarFill = calendarFill;
        mHeaderDate = headerDate;
        mColorSettings = colorSettings;


    }

    public CalendarViews(int colorSettings, LocalDate calendarFill, LocalDate headerDate, LocalDate periodStart, LocalDate periodEnd, int dayNumber, int shiftNumber, String eventName, int imageResourceId) {
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


    public LocalDate getmCalendarFill() {
        return mCalendarFill;
    }

    public int getmDayNumber() {
        return mDayNumber;
    }

    public int getmShiftNumber() {
        return mShiftNumber;
    }

    public String getmEventName() {
        return mEventName;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public LocalDate getmPeriodStart(){
        return mPeriodStart;
    }

    public LocalDate getmPeriodEnd(){
        return mPeriodEnd;
    }

    public LocalDate getmHeaderDate(){
        return mHeaderDate;
    }

    public int getmColorSettings(){
        return mColorSettings;
    }

    public boolean hasPeriod() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public void setmColorSettings (int colorSettings){
        mColorSettings = colorSettings;
    }
}
