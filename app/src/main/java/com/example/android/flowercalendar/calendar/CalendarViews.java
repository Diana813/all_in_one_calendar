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

    public CalendarViews(LocalDate calendarFill, LocalDate headerDate, int dayNumber, String shiftNumber, String numberOfEvents) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = numberOfEvents;
        mCalendarFill = calendarFill;
        mHeaderDate = headerDate;
    }

    public CalendarViews(LocalDate calendarFill, LocalDate headerDate, LocalDate periodStart, LocalDate periodEnd, int dayNumber, String shiftNumber, String numberOfEvents, int imageResourceId) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = numberOfEvents;
        mImageResourceId = imageResourceId;
        mCalendarFill = calendarFill;
        mPeriodStart = periodStart;
        mPeriodEnd = periodEnd;
        mHeaderDate = headerDate;
    }


    public LocalDate getmCalendarFill() {
        return mCalendarFill;
    }

    public int getmDayNumber() {
        return mDayNumber;
    }

    public String getmShiftNumber() {
        return mShiftNumber;
    }

    public String getmEventName() {
        return mEventName;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public LocalDate getmPeriodStart() {
        return mPeriodStart;
    }

    public LocalDate getmPeriodEnd() {
        return mPeriodEnd;
    }

    public LocalDate getmHeaderDate() {
        return mHeaderDate;
    }

    public boolean hasPeriod() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

}
