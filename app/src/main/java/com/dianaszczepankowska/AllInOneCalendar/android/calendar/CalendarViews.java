package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

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
    private boolean isHoliday;

    public CalendarViews(LocalDate calendarFill, LocalDate headerDate, int dayNumber, String shiftNumber, String numberOfEvents, boolean isHoliday) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = numberOfEvents;
        mCalendarFill = calendarFill;
        mHeaderDate = headerDate;
        this.isHoliday = isHoliday;
    }

    public CalendarViews(LocalDate calendarFill, LocalDate headerDate, LocalDate periodStart, LocalDate periodEnd, int dayNumber, String shiftNumber, String numberOfEvents, int imageResourceId, boolean isHoliday) {
        mDayNumber = dayNumber;
        mShiftNumber = shiftNumber;
        mEventName = numberOfEvents;
        mImageResourceId = imageResourceId;
        mCalendarFill = calendarFill;
        mPeriodStart = periodStart;
        mPeriodEnd = periodEnd;
        mHeaderDate = headerDate;
        this.isHoliday = isHoliday;
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

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }
}
