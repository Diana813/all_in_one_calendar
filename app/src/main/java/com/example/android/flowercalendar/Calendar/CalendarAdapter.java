package com.example.android.flowercalendar.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarEvents;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.NonNull;


public class CalendarAdapter extends ArrayAdapter<CalendarViews> {

    private TextView dayNumberTextView;
    private TextView shiftNumberTextView;
    private TextView eventNameTextView;
    private RelativeLayout layout;
    private RelativeLayout singleItem;
    private LocalDate calendarFill;
    private LocalDate headerDate;
    private int month;
    private int year;
    private DayOfWeek headerDateDayOfWeek;



    CalendarAdapter(Context context, ArrayList<CalendarViews> calendarViews) {
        super(context, 0, calendarViews);

    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        CalendarViews views = getItem(position);


        View gridItemView = convertView;
        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_calendar_item, parent, false);
        }


        dayNumberTextView = (TextView) gridItemView.findViewById(R.id.dayNumber);
        shiftNumberTextView = (TextView) gridItemView.findViewById(R.id.shiftNumber);
        eventNameTextView = (TextView) gridItemView.findViewById(R.id.eventName);
        ImageView periodImage = (ImageView) gridItemView.findViewById(R.id.periodImage);
        layout = (RelativeLayout) gridItemView.findViewById(R.id.singleItem);
        singleItem = (RelativeLayout) gridItemView.findViewById(R.id.singleItemLayout);
        assert views != null;
        calendarFill = views.getmCalendarFill();
        headerDate = views.getmHeaderDate();
        month = headerDate.getMonth().getValue();
        year = headerDate.getYear();
        headerDateDayOfWeek = calendarFill.getDayOfWeek();

        if (views.getmColorSettings() == 1) {
            setRed();
        } else if (views.getmColorSettings() == 2) {
            setYellow();
        } else if (views.getmColorSettings() == 3) {
            setGreen();
        } else if ((views.getmColorSettings() == 4)) {
            setBlue();
        } else if ((views.getmColorSettings() == 5)) {
            setViolet();
        } else if ((views.getmColorSettings() == 6)) {
            setGrey();
        } else {
            setRed();
        }

        if (views.hasPeriod()) {
            periodImage.setImageResource(views.getmImageResourceId());
            periodImage.setVisibility(View.VISIBLE);
        } else {
            periodImage.setVisibility(View.GONE);
        }

        dayNumberTextView.setText(String.valueOf(views.getmDayNumber()));
        shiftNumberTextView.setText(String.valueOf(views.getmShiftNumber()));
        eventNameTextView.setText(views.getmEventName());
        return gridItemView;
    }


    private void weekend() {
        dayNumberTextView.setTextColor(Color.parseColor("#000000"));
        shiftNumberTextView.setTextColor(Color.BLACK);
        eventNameTextView.setTextColor(Color.parseColor("#BDBDBD"));
    }


    private void todaysDateAtWeekend() {
        dayNumberTextView.setTextColor(Color.parseColor("#000000"));
        shiftNumberTextView.setTextColor(Color.BLACK);
        eventNameTextView.setTextColor(Color.parseColor("#BDBDBD"));
    }


    private void setRed() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame);
            weekend();
            shiftNumberTextView.setTextColor(Color.BLACK);
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            shiftNumberTextView.setTextColor(Color.BLACK);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend);
                weekend();
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));

            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


    private void setYellow() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame_yellow);
            weekend();
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend_yellow);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend_yellow);
                weekend();
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));

            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


    private void setGreen() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame_green);
            weekend();
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend_green);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend_green);
                weekend();
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));


            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


    private void setBlue() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame_blue);
            weekend();
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend_blue);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend_blue);
                weekend();
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));

            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }

    private void setViolet() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame_violet);
            weekend();
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend_violet);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend_violet);
                weekend();
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));


            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }

    private void setGrey() {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame_grey);
            weekend();
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend_grey);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend_grey);
                weekend();
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));


            }
        }else{
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


}







