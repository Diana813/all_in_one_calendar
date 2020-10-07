package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter.getContext;

public class LanguageUtils {

    public static String yearGramma(int howMany) {
        String year;
        if (howMany == 1) {
            year = getContext().getString(R.string.yearLeft);
        } else if (howMany < 5 && howMany > 1) {
            year = getContext().getString(R.string.yearsLeft5);
        } else {
            year = getContext().getString(R.string.yearsLeft);
        }
        return year;
    }

    public static String dayGramma(int howMany, Context context) {
        String day;
        if (howMany == 1) {
            day = context.getString(R.string.dayLeft);
        } else {
            day = context.getString(R.string.daysLeft);
        }
        return day;
    }

    public static String monthGramma(int howMany, Context context) {
        String month;
        if (howMany == 1) {
            month = context.getString(R.string.month);
        } else if (howMany < 5 && howMany > 1) {
            month = context.getString(R.string.monthsPlural);
        } else {
            month = context.getString(R.string.months5);
        }
        return month;
    }

    public static String dayOfWeekShortName(LocalDate date, Context context) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return context.getString(R.string.Monday);
        } else if (date.getDayOfWeek() == DayOfWeek.TUESDAY) {
            return context.getString(R.string.Tuesday);
        } else if (date.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            return context.getString(R.string.Wednesday);
        } else if (date.getDayOfWeek() == DayOfWeek.THURSDAY) {
            return context.getString(R.string.Thursday);
        } else if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            return context.getString(R.string.Friday);
        } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return context.getString(R.string.Saturday);
        } else {
            return context.getString(R.string.Sunday);
        }
    }
}
