package com.example.android.flowercalendar.calendar;

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
import com.example.android.flowercalendar.mainActivity.MainActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.NonNull;


public class CalendarAdapter extends ArrayAdapter<CalendarViews> {

    private TextView dayNumberTextView;
    private TextView shiftNumberTextView;
    private TextView eventNameTextView;
    private RelativeLayout layout;
    private LocalDate calendarFill;
    private LocalDate headerDate;
    private int month;
    private int year;
    private DayOfWeek headerDateDayOfWeek;
    private Context context;


    CalendarAdapter(Context context, ArrayList<CalendarViews> calendarViews) {
        super(context, 0, calendarViews);
        this.context = context;
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

        double keypadParentHeight = (MainActivity.getScreenHeight() * 0.8);
        double rowHeight = keypadParentHeight / 7;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) rowHeight);


        dayNumberTextView = gridItemView.findViewById(R.id.dayNumber);
        shiftNumberTextView = gridItemView.findViewById(R.id.shiftNumber);
        eventNameTextView = gridItemView.findViewById(R.id.numberOfEvents);
        ImageView periodImage = gridItemView.findViewById(R.id.periodImage);
        layout = gridItemView.findViewById(R.id.singleItem);
        layout.setLayoutParams(params);
        assert views != null;
        calendarFill = views.getmCalendarFill();
        headerDate = views.getmHeaderDate();
        month = headerDate.getMonth().getValue();
        year = headerDate.getYear();
        headerDateDayOfWeek = calendarFill.getDayOfWeek();

        setCalendarColor(
                Color.parseColor("#000000")
        );

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
        eventNameTextView.setTextColor(Color.parseColor("#ffffff"));
    }


    private void todaysDateAtWeekend() {
        dayNumberTextView.setTextColor(Color.parseColor("#000000"));
        shiftNumberTextView.setTextColor(Color.BLACK);
        eventNameTextView.setTextColor(Color.parseColor("#ffffff"));
    }


    private void setCalendarColor(int textColorForCurrentDayNum) {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(R.drawable.weekend_frame);
            weekend();
            shiftNumberTextView.setTextColor(Color.BLACK);
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(textColorForCurrentDayNum);
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
        } else {
            shiftNumberTextView.setTextColor(Color.BLACK);
        }
    }


    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}







