package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

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

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarViews;
import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.NonNull;

public class CalendarAdapterNoRota extends ArrayAdapter<CalendarViews> {

    private TextView dayNumberTextView;
    private TextView shiftNumberTextView;
    private RelativeLayout layout;
    private LocalDate calendarFill;
    private LocalDate headerDate;
    private int month;
    private int year;
    private DayOfWeek headerDateDayOfWeek;
    private Context context;
    private boolean isHoliday;


    public CalendarAdapterNoRota(Context context, ArrayList<CalendarViews> calendarViews) {
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
                    R.layout.single_calendar_item_no_rota, parent, false);
        }

        double keypadParentHeight = (MainActivity.getScreenHeight() * 0.8);
        double rowHeight = keypadParentHeight / 7;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) rowHeight);


        dayNumberTextView = gridItemView.findViewById(R.id.dayNumber);
        ImageView eventNameTextView = gridItemView.findViewById(R.id.numberOfEvents);
        ImageView periodImage = gridItemView.findViewById(R.id.periodImage);
        layout = gridItemView.findViewById(R.id.singleItem);
        layout.setLayoutParams(params);
        assert views != null;
        calendarFill = views.getmCalendarFill();
        headerDate = views.getmHeaderDate();
        isHoliday = views.isHoliday();
        month = headerDate.getMonth().getValue();
        year = headerDate.getYear();
        headerDateDayOfWeek = calendarFill.getDayOfWeek();

        setCalendarColor(
                Color.parseColor("#000000")
        );

        if (views.hasPeriod()) {
            periodImage.setBackgroundResource(R.drawable.dot_blue);
            periodImage.setVisibility(View.VISIBLE);
        } else {
            periodImage.setVisibility(View.GONE);
        }

        dayNumberTextView.setText(String.valueOf(views.getmDayNumber()));
        if (views.getmEventName() != null && !views.getmEventName().equals("")) {
            eventNameTextView.setBackgroundResource(R.drawable.dot_red);
        }else{
            eventNameTextView.setVisibility(View.GONE);
        }

        return gridItemView;
    }


    private void todaysDateAtWeekend() {
        dayNumberTextView.setTextColor(context.getColor(R.color.greenZilla));

    }


    private void setCalendarColor(int textColorForCurrentDayNum) {

        //Kolory kalendarza w zależności od dnia tygodnia
        if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
            dayNumberTextView.setTextColor(context.getColor(R.color.greenZilla));
        } else {
            dayNumberTextView.setTextColor(Color.BLACK);
        }


        //Wyróżnienie dziesiejszej daty
        if (headerDate.isEqual(calendarFill)) {
            dayNumberTextView.setTextColor(textColorForCurrentDayNum);
            if (isHoliday) {
                layout.setBackgroundResource(R.drawable.frame);
            } else {
                layout.setBackgroundResource(R.drawable.frame);
            }
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                if (isHoliday) {
                    dayNumberTextView.setTextColor(context.getColor(R.color.greenZilla));
                    layout.setBackgroundResource(R.drawable.frame);

                } else {
                    dayNumberTextView.setTextColor(context.getColor(R.color.greenZilla));
                    layout.setBackgroundResource(R.drawable.frame);
                }
                todaysDateAtWeekend();
            }
        }

        //Wyróżnienie aktualnego miesiąca
        if (month != calendarFill.getMonth().getValue() || year != calendarFill.getYear()) {
            dayNumberTextView.setTextColor(context.getColor(R.color.lightGreyZilla));
            if (headerDateDayOfWeek == DayOfWeek.SATURDAY || headerDateDayOfWeek == DayOfWeek.SUNDAY) {
                dayNumberTextView.setTextColor(context.getColor(R.color.greenZilla));
                dayNumberTextView.setTextColor(context.getColor(R.color.lightGreyZilla));

            }
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







