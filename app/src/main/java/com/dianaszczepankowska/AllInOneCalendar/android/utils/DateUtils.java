package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static String displayHeaderDateInToolbar(String currentDate) {
        //Data wyświetlana w toolbarze
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        String date = refactorStringIntoDate(currentDate).format(sdf);
        String firstLetter = date.substring(0, 1).toUpperCase();
        String restLetters = date.substring(1);
        return firstLetter + restLetters;

    }


    @SuppressLint("SimpleDateFormat")
    public static LocalDate changeLongMonthDateFormatToLocalDate(String dateStr) {

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("dd MMMM, yyyy")
                .toFormatter();

        return LocalDate.parse(dateStr, f);
    }

    @SuppressLint("DefaultLocale")
    public static String formatPeriodDateWithHyphens(String periodDate) {
        if (periodDate == null) {
            return null;
        } else if (periodDate.contains("-")) {
            return periodDate;
        }
        String[] split = periodDate.split(":");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        return String.format("%04d-%02d-%02d", Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    @SuppressLint("SimpleDateFormat")
    public static String fromLongDateFormatToShortDateFormat(String dateStr) {

        String formattedDate;
        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("dd MMMM, yyyy")
                .toFormatter();

        LocalDate parsedDate = LocalDate.parse(dateStr, f);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("MM/d/yyyy");

        formattedDate = parsedDate.format(f2);
        return formattedDate;
    }

    public static String displayDateInALongFormat(Calendar calendar) {
        String myFormat = "dd MMMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static String displayDateInALongFormat(LocalDate date) {
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        return date.format(f2);
    }

    public static long dateStringToMilis(String dateString) {

        String[] parts = dateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        LocalDateTime periodStartDate = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day);
        ZonedDateTime zdt = periodStartDate.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();

    }


    public static LocalDate refactorStringIntoDate(String stringDate) {

        LocalDate searchedDate = null;
        if (stringDate != null && !stringDate.equals("")) {

            String[] split = stringDate.split("-");
            if (Integer.parseInt(split[1]) < 13 && Integer.parseInt(split[2]) < 32) {
                searchedDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
        }

        return searchedDate;
    }

    @SuppressLint("DefaultLocale")
    public static String findEventEndTime(String schedule, int length) {
        String finish;
        int startHour = 0;
        int startminute = 0;
        if (schedule.equals("")) {
            finish = "";
        } else {

            String[] parts = schedule.split(":");

            try {
                startHour = Integer.parseInt(parts[0]);
                startminute = Integer.parseInt(parts[1]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();

            }

            int finishHour = startHour + length;
            if (finishHour == 24) {
                finishHour = 0;
            }
            if (finishHour > 24) {
                finishHour = finishHour - 24;
            }

            finish = String.format("%02d:%02d", finishHour, startminute);
        }
        return finish;
    }
}
