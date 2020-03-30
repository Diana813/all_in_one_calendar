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


        dayNumberTextView = (TextView) gridItemView.findViewById(R.id.dayNumber);
        shiftNumberTextView = (TextView) gridItemView.findViewById(R.id.shiftNumber);
        eventNameTextView = (TextView) gridItemView.findViewById(R.id.numberOfEvents);
        ImageView periodImage = (ImageView) gridItemView.findViewById(R.id.periodImage);
        layout = (RelativeLayout) gridItemView.findViewById(R.id.singleItem);
        assert views != null;
        calendarFill = views.getmCalendarFill();
        headerDate = views.getmHeaderDate();
        month = headerDate.getMonth().getValue();
        year = headerDate.getYear();
        headerDateDayOfWeek = calendarFill.getDayOfWeek();

        setCalendarColor(views);

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

    private void setCalendarColor(CalendarViews views){
        if (views.getmColorSettings() == 1) {
            setCalendarColor(R.drawable.weekend_frame,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend,
                    R.drawable.frame_for_other_months_weekend);
        } else if (views.getmColorSettings() == 2) {
            setCalendarColor(R.drawable.weekend_frame_yellow,
                    Color.parseColor("#b71c1c"),
                    Color.parseColor("#000000"),
                    Color.parseColor("#b71c1c"),
                    R.drawable.frame_for_current_day_at_weekend_yellow,
                    R.drawable.frame_for_other_months_weekend_yellow);
        } else if (views.getmColorSettings() == 3) {
            setCalendarColor(R.drawable.weekend_frame_green,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend_green,
                    R.drawable.frame_for_other_months_weekend_green);
        } else if ((views.getmColorSettings() == 4)) {
            setCalendarColor(R.drawable.weekend_frame_blue,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend_blue,
                    R.drawable.frame_for_other_months_weekend_blue);
        } else if ((views.getmColorSettings() == 5)) {
            setCalendarColor(R.drawable.weekend_frame_violet,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend_violet,
                    R.drawable.frame_for_other_months_weekend_violet);
        } else if ((views.getmColorSettings() == 6)) {
            setCalendarColor(R.drawable.weekend_frame_grey,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend_grey,
                    R.drawable.frame_for_other_months_weekend_grey);
        } else {
            setCalendarColor(R.drawable.weekend_frame,
                    Color.BLACK,
                    Color.parseColor("#000000"),
                    Color.BLACK,
                    R.drawable.frame_for_current_day_at_weekend,
                    R.drawable.frame_for_other_months_weekend);
        }
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


    private void setCalendarColor(int backgroundResourceForWeekend, int textColorForWeekendShift, int textColorForCurrentDayNum, int texColorForShiftCurrentDay, int backgroundForCurrentDayWeekend, int backgroundForOtherMonthWeekend) {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            layout.setBackgroundResource(backgroundResourceForWeekend);
            weekend();
            shiftNumberTextView.setTextColor(textColorForWeekendShift);
        }

        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(textColorForCurrentDayNum);
            layout.setBackgroundResource(R.drawable.frame2);
            shiftNumberTextView.setTextColor(texColorForShiftCurrentDay);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(backgroundForCurrentDayWeekend);
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            shiftNumberTextView.setTextColor(Color.parseColor("#BDBDBD"));
            layout.setBackgroundResource(R.drawable.frame_for_other_month_days);
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                layout.setBackgroundResource(backgroundForOtherMonthWeekend);
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







