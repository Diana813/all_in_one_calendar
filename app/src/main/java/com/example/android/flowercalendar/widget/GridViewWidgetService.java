package com.example.android.flowercalendar.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.calendar.CalendarViews;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class GridViewWidgetService extends RemoteViewsService {

    @Override
    public AppWidgetGridView onGetViewFactory(Intent intent) {
        return new AppWidgetGridView(this.getApplicationContext(), WidgetData.widgetData());

    }
}

class AppWidgetGridView implements RemoteViewsService.RemoteViewsFactory {

    private List<CalendarViews> dataList;
    private Context context;

    AppWidgetGridView(Context applicationContext, List<CalendarViews> dataList) {
        this.context = applicationContext;
        this.dataList = dataList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        dataList = WidgetData.widgetData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 7;
    }


    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget_item);

        if (position == 0) {
            views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame);
        } else {
            views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame2);
        }
        views.setTextViewText(R.id.dayOfAWeek, String.valueOf(LocalDate.now().getDayOfWeek().plus(position)).substring(0, 3));

        if (LocalDate.now().getDayOfWeek().plus(position) == DayOfWeek.SATURDAY ||
                LocalDate.now().getDayOfWeek().plus(position) == DayOfWeek.SUNDAY) {
            views.setInt(R.id.dayOfAWeek, "setBackgroundResource", R.drawable.widget_text_weekend);
            views.setTextColor(R.id.dayOfAWeek, Color.WHITE);
        } else {
            views.setInt(R.id.dayOfAWeek, "setBackgroundResource", R.drawable.widget_text_frame);
            views.setTextColor(R.id.dayOfAWeek, Color.BLACK);
        }

        views.setTextViewText(R.id.dayNumber, String.valueOf(dataList.get(position).getmDayNumber()));
        views.setTextViewText(R.id.shiftNumber, dataList.get(position).getmShiftNumber());
        views.setTextColor(R.id.shiftNumber, Color.BLACK);
        views.setTextViewText(R.id.numberOfEvents, dataList.get(position).getmEventName());
        views.setTextColor(R.id.numberOfEvents, Color.parseColor("#002171"));

        if (dataList.get(position).hasPeriod()) {
            views.setImageViewResource(R.id.periodImage, R.mipmap.period_icon_v2);
            views.setViewVisibility(R.id.periodImage, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.periodImage, View.GONE);
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("dayNumber", dataList.get(position).getmDayNumber());
        fillInIntent.putExtra("shiftNumber", dataList.get(position).getmShiftNumber());
        fillInIntent.putExtra("numberOfEvents", dataList.get(position).getmEventName());
        fillInIntent.putExtra("hasPeriod", dataList.get(position).hasPeriod());
        views.setOnClickFillInIntent(R.id.singleItem, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {

        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
