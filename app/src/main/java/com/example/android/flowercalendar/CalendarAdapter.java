package com.example.android.flowercalendar;

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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.NonNull;


public class CalendarAdapter extends ArrayAdapter<CalendarViews> {

    private CalendarViews views;
    private TextView dayNumberTextView;
    private TextView shiftNumberTextView;
    private TextView eventNameTextView;
    private RelativeLayout layout;
    private RelativeLayout singleItem;
    private LocalDate calendarFill;
    private LocalDate headerDate;

    CalendarAdapter(Context context, ArrayList<CalendarViews> calendarViews) {
        super(context, 0, calendarViews);

    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        views = getItem(position);

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
        calendarFill = views.getmCalendarFill();
        headerDate = views.getmHeaderDate();

        addCalendarColors();

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

    private void addCalendarColors() {

        LocalDate calendarStart = LocalDate.now();
        assert views != null;
        int day = calendarStart.getDayOfMonth();
        int month = headerDate.getMonth().getValue();
        int year = headerDate.getYear();
        DayOfWeek headerDateDayOfWeek = calendarFill.getDayOfWeek();


        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame);
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            eventNameTextView.setTextColor(Color.parseColor("#BDBDBD"));
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {

            dayNumberTextView.setTextColor(Color.parseColor("#000000"));
            layout.setBackgroundResource(R.drawable.frame2);
            singleItem.setElevation(10);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_current_day_at_weekend);
                dayNumberTextView.setTextColor(Color.parseColor("#ffffff"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                eventNameTextView.setTextColor(Color.parseColor("#BDBDBD"));
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(R.drawable.frame_for_other_months_weekend);
                dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
                eventNameTextView.setTextColor(Color.parseColor("#BDBDBD"));

            }
        }
    }
}
