package com.example.android.flowercalendar.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DatabaseAdapter extends ArrayAdapter<CalendarEvents> {

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
    private List<CalendarEvents> events;


    DatabaseAdapter(Context context, ArrayList<CalendarEvents> calendarEvents) {
        super(context, 0, calendarEvents);

    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        CalendarEvents views = getItem(position);


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
        String calendarFillString = views.getCalendarFillString();
        String[] parts = calendarFillString.split("-");
        int yearCalendarFill = Integer.parseInt(parts[0]);
        int monthCalendarFill = Integer.parseInt(parts[1]);
        int dayCalendarFill = Integer.parseInt(parts[2]);
        calendarFill = LocalDate.of(yearCalendarFill, monthCalendarFill, dayCalendarFill);
        String headerDateString = views.getHeaderDateString();
        String[] partsHeaderDate = headerDateString.split("-");
        int yearHeader = Integer.parseInt(partsHeaderDate[0]);
        int monthHeader = Integer.parseInt(partsHeaderDate[1]);
        int dayHeader = Integer.parseInt(partsHeaderDate[2]);
        headerDate = LocalDate.of(yearHeader, monthHeader, dayHeader);
        month = headerDate.getMonth().getValue();
        year = headerDate.getYear();
        headerDateDayOfWeek = calendarFill.getDayOfWeek();

        if (views.getColorSettings() == 1) {
            setRed();
        } else if (views.getColorSettings() == 2) {
            setYellow();
        } else if (views.getColorSettings() == 3) {
            setGreen();
        } else if ((views.getColorSettings() == 4)) {
            setBlue();
        } else if ((views.getColorSettings() == 5)) {
            setViolet();
        } else if ((views.getColorSettings() == 6)) {
            setGrey();
        } else {
            setRed();
        }


        dayNumberTextView.setText(String.valueOf(views.getDayNumber()));
        if (views.getShiftNumber() == null) {
            shiftNumberTextView.setText("");
        } else {
            shiftNumberTextView.setText(views.getShiftNumber());
        }
        if (views.getEventName() == null) {
            eventNameTextView.setText("");
        } else {
            eventNameTextView.setText(views.getEventName());
        }

        if(views.getPeriodStartString().equals(calendarFillString) ||
        views.getPeriodEndString().equals(calendarFillString)){
            periodImage.setImageResource(R.mipmap.period_icon_v2);
        }else{
            periodImage.setImageResource(0);
        }


        if (views.getImageResourceId() == 0) {
            periodImage.setVisibility(View.GONE);
        } else {
            periodImage.setVisibility(View.VISIBLE);
        }

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
            singleItem.setElevation(10);
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
        } else {
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
            singleItem.setElevation(10);
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
        } else {
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
            singleItem.setElevation(10);
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
        } else {
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
            singleItem.setElevation(10);
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
        } else {
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
            singleItem.setElevation(10);
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
        } else {
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
            singleItem.setElevation(10);
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
        } else {
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


    public void setEvetsList(List<CalendarEvents> events) {
        this.events = events;
        notifyDataSetChanged();
    }
}

