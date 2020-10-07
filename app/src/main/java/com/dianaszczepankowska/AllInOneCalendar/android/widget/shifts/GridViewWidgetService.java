package com.dianaszczepankowska.AllInOneCalendar.android.widget.shifts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarViews;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.LanguageUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class GridViewWidgetService extends RemoteViewsService {

    @Override
    public AppWidgetGridView onGetViewFactory(Intent intent) {
        return new AppWidgetGridView(this.getApplicationContext(), WidgetData.widgetData(getApplicationContext()));

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
        dataList = WidgetData.widgetData(context);
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

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget_item_shifts);

        if (position == 0) {
            views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame_current_day);
        } else {
            views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame);
        }
        views.setTextViewText(R.id.dayOfAWeek, LanguageUtils.dayOfWeekShortName(LocalDate.now().plusDays(position), context));

        if (LocalDate.now().getDayOfWeek().plus(position) == DayOfWeek.SATURDAY ||
                LocalDate.now().getDayOfWeek().plus(position) == DayOfWeek.SUNDAY) {
            views.setInt(R.id.dayOfAWeek, "setBackgroundResource", R.drawable.widget_text);
            views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame_weekend);
            views.setTextColor(R.id.dayOfAWeek, Color.BLACK);
            if (position == 0) {
                views.setInt(R.id.singleItem, "setBackgroundResource", R.drawable.widget_frame_today_weekend);
            }
        } else {
            views.setInt(R.id.dayOfAWeek, "setBackgroundResource", R.drawable.widget_text);
            views.setTextColor(R.id.dayOfAWeek, Color.BLACK);
        }

        views.setTextViewText(R.id.dayNumber, String.valueOf(dataList.get(position).getmDayNumber()));
        views.setTextViewText(R.id.shiftNumber, dataList.get(position).getmShiftNumber());
        views.setTextColor(R.id.shiftNumber, Color.BLACK);

        if (dataList.get(position).hasPeriod()) {
            views.setImageViewResource(R.id.periodImage, R.drawable.dot_blue);
            views.setViewVisibility(R.id.periodImage, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.periodImage, View.GONE);
        }

        if (dataList.get(position).getmEventName() != null && !dataList.get(position).getmEventName().equals("")) {
            views.setImageViewResource(R.id.numberOfEvents, R.drawable.dot_red);

        } else {
            views.setViewVisibility(R.id.numberOfEvents, View.GONE);
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
